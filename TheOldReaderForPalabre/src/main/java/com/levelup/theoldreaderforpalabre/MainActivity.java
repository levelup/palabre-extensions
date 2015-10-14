package com.levelup.theoldreaderforpalabre;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.levelup.palabre.api.datamapping.Category;
import com.levelup.palabre.api.datamapping.Source;
import com.levelup.palabre.api.utils.PalabreUtils;
import com.levelup.theoldreaderforpalabre.ui.fragment.LoginFragment;
import com.levelup.theoldreaderforpalabre.ui.fragment.ManageSourceFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoginReceivedListener {
    public static final String CATEGORY_SOURCE_REFRESHED = "com.levelup.theoldreaderforpalabre.MainActivity";
    private MenuItem addAction;
    private MenuItem logOutAction;
    private State currentState = State.MANAGE_SOURCE;
    private SharedPreferences sharedPref;
    private boolean inSearchMode;
    private View searchBar;
    private View addFeed;
    private EditText addFeedText;
    private View addFeedprogress;
    private String authKey;
    private String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        authKey = sharedPref.getString(SharedPreferenceKeys.AUTH, null);

        searchBar = findViewById(R.id.search_bar);

        addFeed = findViewById(R.id.add_feed);
        addFeedText = (EditText) findViewById(R.id.add_feed_text);
        addFeedprogress = findViewById(R.id.add_feed_progress);

        addFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFeed();
            }
        });


        Fragment fragment;
        if (!TextUtils.isEmpty(authKey)) {

            fragment = ManageSourceFragment.newInstance();
        } else {
            currentState = State.LOGIN;
            fragment = LoginFragment.newInstance();

        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.setCustomAnimations(R.animator.fade_in, R.animator.slide_out_right);
                ft.replace(R.id.content, fragment)
                .commit();

        manageIntent(getIntent());


    }

    @Override
    protected void onNewIntent(Intent intent) {
        manageIntent(intent);
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {
        PalabreUtils.checkPalabreInstallationAndWarn(this);
        super.onResume();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        addAction = menu.findItem(R.id.action_add);
        logOutAction = menu.findItem(R.id.action_logout);

        switchMenuState();

        if (!TextUtils.isEmpty(searchText)) {
            launchAutoSearch(searchText);
            searchText = "";
        }

        return true;
    }


    @Override
    public void onBackPressed() {
        if (inSearchMode) {
            switchSearchMode();
        } else {
            super.onBackPressed();
        }
    }

    private void switchMenuState() {
        if (logOutAction != null && addAction != null) {

            boolean visible = true;
            if (currentState == State.LOGIN) {
                visible = false;
            }
            logOutAction.setVisible(visible);
            addAction.setVisible(visible);


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {

            if (inSearchMode) {
                switchSearchMode();
            } else {

                finish();
            }
        } else if (id == R.id.action_add) {
            switchSearchMode();
            return true;
        } else if (id == R.id.action_logout) {

            PreferenceManager.getDefaultSharedPreferences(this).edit()
                    .remove(SharedPreferenceKeys.AUTH)
                    .apply();

            List<Category> cats = Category.getAll(this);
            for (Category cat : cats) {
                cat.delete(this);
            }

            List<Source> sources = Source.getAll(this);
            for (Source source : sources) {
                source.delete(this);
            }

            switchToState(State.LOGIN);


            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchSearchMode() {
        inSearchMode = !inSearchMode;
        logOutAction.setVisible(!inSearchMode);
        addAction.setVisible(!inSearchMode);
        if (inSearchMode) {
            searchBar.setVisibility(View.VISIBLE);

        } else {
            searchBar.setVisibility(View.GONE);

        }
    }

    private void switchSearchModeProgress() {
        if (addFeed.getVisibility() == View.VISIBLE) {
            addFeed.setVisibility(View.GONE);
            addFeedprogress.setVisibility(View.VISIBLE);
        } else {
            addFeed.setVisibility(View.VISIBLE);
            addFeedprogress.setVisibility(View.GONE);

        }
    }

    private void manageIntent(Intent intent) {
        String action = intent.getAction();
        String type = intent.getType();


        if (Intent.ACTION_SEND.equals(action) && type != null) {

            //Menu not inflated yet
            if (addAction == null) {
                searchText = intent.getStringExtra(Intent.EXTRA_TEXT);
            } else {
                String feedUrl = intent.getStringExtra(Intent.EXTRA_TEXT);
                launchAutoSearch(feedUrl);
            }
        }
    }

    private void launchAutoSearch(String feedUrl) {
        switchSearchMode();
        addFeedText.setText(feedUrl);
        addFeed();
    }

    private void addFeed() {

        switchSearchModeProgress();
        hideKeyboard(addFeedText);

        Ion.with(this).load("POST", "https://theoldreader.com/reader/api/0/subscription/quickadd?quickadd=blog.theoldreader.com")
                .setHeader("Authorization: GoogleLogin auth", authKey)
                .addQuery("output", "json")
                .addQuery("quickadd", addFeedText.getText().toString())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                            if (e != null || ( result.get("numResults") != null && result.get("numResults").getAsInt() > 0)) {
                                TheOldReaderExtension.fetchCategories(MainActivity.this, authKey, new TheOldReaderExtension.OnCategoryAndSourceRefreshed() {
                                    @Override
                                    public void onFinished() {

                                        Intent intent = new Intent(CATEGORY_SOURCE_REFRESHED);
                                        sendBroadcast(intent);

                                        switchSearchMode();
                                        switchSearchModeProgress();
                                        addFeedText.setText("");

                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        switchSearchModeProgress();
                                        Snackbar.make(addFeed, R.string.add_error, Snackbar.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onProgressChanged(int progress) {

                                    }
                                });
                            } else {
                                switchSearchModeProgress();
                                Snackbar.make(addFeed, R.string.add_error, Snackbar.LENGTH_LONG).show();
                            }
                    }
                });



    }

    @Override
    public void onLoginReceived() {

        switchToState(State.MANAGE_SOURCE);

    }

    private void switchToState(State state) {

        Fragment fragment;
        if (state == State.LOGIN) {
            fragment = LoginFragment.newInstance();

        } else {
            fragment = ManageSourceFragment.newInstance();
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right);
        ft.replace(R.id.content, fragment)
                .commit();

        currentState = state;
        switchMenuState();
    }

    private enum State {
        LOGIN, MANAGE_SOURCE
    }

    /**
     * Hides the keyboard.
     *
     * @param view
     */
    private void hideKeyboard(View view) {
        Context context = view.getContext();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
