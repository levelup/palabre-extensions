package com.levelup.palabre.inoreaderforpalabre.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.levelup.palabre.api.datamapping.Category;
import com.levelup.palabre.api.datamapping.Source;
import com.levelup.palabre.api.utils.PalabreUtils;
import com.levelup.palabre.inoreaderforpalabre.BuildConfig;
import com.levelup.palabre.inoreaderforpalabre.InoreaderExtension;
import com.levelup.palabre.inoreaderforpalabre.R;
import com.levelup.palabre.inoreaderforpalabre.core.SharedPreferenceKeys;
import com.levelup.palabre.inoreaderforpalabre.inoreader.InoreaderLoginService;
import com.levelup.palabre.inoreaderforpalabre.inoreader.InoreaderService;
import com.levelup.palabre.inoreaderforpalabre.inoreader.data.addsubscription.AddSubscriptionResponse;
import com.levelup.palabre.inoreaderforpalabre.inoreader.data.userinfo.UserInfo;
import com.levelup.palabre.inoreaderforpalabre.ui.adapter.CategorySourceAdapter;
import com.levelup.palabre.inoreaderforpalabre.ui.adapter.CategorySourceItemTouchCallback;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();

    @InjectView(R.id.username)
    TextInputLayout username;
    @InjectView(R.id.password)
    TextInputLayout password;
    @InjectView(R.id.login)
    AppCompatButton loginButton;
    @InjectView(R.id.recycler_view)
    RecyclerView catSourceList;
    @InjectView(R.id.login_container)
    View loginContainer;
    @InjectView(R.id.search_bar)
    View searchBar;
    @InjectView(R.id.add_feed)
    View addFeed;
    @InjectView(R.id.add_feed_text)
    EditText addFeedText;
    @InjectView(R.id.add_feed_progress)
    View addFeedprogress;
    @InjectView(R.id.progress)
    ProgressBar progress;
    @InjectView(R.id.failure)
    AppCompatImageButton failureBtn;

    private CategorySourceAdapter adapter;
    private MenuItem logoutMenu;
    private MenuItem addMenu;
    private boolean inSearchMode;
    private String searchText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        password.setHint(getString(R.string.password));
        username.setHint(getString(R.string.username));

        addFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFeed();
            }
        });


        final TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                startLogin();
            }
        };
        username.getEditText().addTextChangedListener(watcher);
        password.getEditText().addTextChangedListener(watcher);
        password.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login();
                    return true;
                }
                return false;
            }
        });


        TextView explanation = (TextView) findViewById(R.id.auth_explanation);
        explanation.setText(Html.fromHtml(getString(R.string.auth_explanation)));
        explanation.setMovementMethod(LinkMovementMethod.getInstance());


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        initList();


        manageIntent(getIntent());


    }

    private void login() {
        final String usernameS = username.getEditText().getText().toString();
        final String passwordS = password.getEditText().getText().toString();

        boolean error = false;
        if (TextUtils.isEmpty(usernameS)) {
            username.setError(getString(R.string.error_empty));
            error = true;
        } else if (!isValidEmail(usernameS)) {
            username.setError(getString(R.string.error_email));
            error = true;

        }
        if (TextUtils.isEmpty(passwordS)) {
            password.setError(getString(R.string.error_empty));
            error = true;
        }


        if (error) {
            return;
        }


        startProgress();
        hideKeyboard(username.getEditText());


        final Call<String> request = InoreaderLoginService.getInstance(MainActivity.this).login(usernameS, passwordS);
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (!response.isSuccessful()) {
                    Snackbar
                            .make(loginButton, R.string.login_error, Snackbar.LENGTH_LONG)
//                                .setAction(R.string.snackbar_action, myOnClickListener)
                            .show(); // Don’t forget to show!
                    startFailure();
                    return;
                }

                String result = response.body();

                if (BuildConfig.DEBUG) Log.d(TAG, "Result: " + result);
                String lines[] = result.split("\\r?\\n");
                for (String line : lines) {

                    if (BuildConfig.DEBUG) Log.d(TAG, "Result line: " + line);
                    if (line.startsWith("Auth")) {
                        String authSplit[] = line.split("=");
                        if (BuildConfig.DEBUG) Log.d(TAG, "token: " + authSplit[1]);
                        InoreaderService.getInstance(MainActivity.this).resetAdapters();
                        PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString(SharedPreferenceKeys.TOKEN, authSplit[1]).apply();

                        Call<UserInfo> userInfoRequest = InoreaderService.getInstance(MainActivity.this).getUserInformation();
                        userInfoRequest.enqueue(new Callback<UserInfo>() {
                            @Override
                            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                                if (!response.isSuccessful()) {
                                    startLogin();
                                    return;
                                }
                                UserInfo result = response.body();
                                PreferenceManager.getDefaultSharedPreferences(MainActivity.this)
                                        .edit()
                                        .putString(SharedPreferenceKeys.USER_ID, result.getUserId())
                                        .putString(SharedPreferenceKeys.USER_NAME, result.getUserName())
                                        .apply();

                                InoreaderExtension.refreshCategoriesAndSources(MainActivity.this, new InoreaderExtension.OnCategoryAndSourceRefreshed() {
                                    @Override
                                    public void onFinished() {
                                        initList();
                                        startLogin();
                                        try {
                                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("palabre://extauth"));
                                            startActivity(intent);
                                            MainActivity.this.finish();
                                        } catch (Exception e) {
                                            // Palabre is not installed
                                            Snackbar.make(loginButton, R.string.intent_error, Snackbar.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable throwable) {
                                        startFailure();
                                    }

                                    @Override
                                    public void onprogressChanged(int progress) {

                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<UserInfo> call, Throwable t) {
                                startLogin();

                            }
                        });

                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Snackbar
                        .make(loginButton, R.string.login_error, Snackbar.LENGTH_LONG)
//                                .setAction(R.string.snackbar_action, myOnClickListener)
                        .show(); // Don’t forget to show!
                startFailure();
            }
        });



    }

    private void startLogin() {
        loginButton.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        failureBtn.setVisibility(View.GONE);
    }

    private void startFailure() {
        loginButton.setVisibility(View.GONE);
        progress.setVisibility(View.GONE);
        failureBtn.setVisibility(View.VISIBLE);    }

    private void startProgress() {
        loginButton.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        failureBtn.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        PalabreUtils.checkPalabreInstallationAndWarn(this);
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        manageIntent(intent);
        super.onNewIntent(intent);
    }

    private void manageIntent(Intent intent) {
        String action = intent.getAction();
        String type = intent.getType();


        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (BuildConfig.DEBUG) Log.d(TAG, "Extra: " + intent.getStringExtra(Intent.EXTRA_TEXT));

            //Menu not inflated yet
            if (logoutMenu == null) {
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
        Call<AddSubscriptionResponse> request = InoreaderService.getInstance(this).addSubscription(addFeedText.getText().toString());
        request.enqueue(new Callback<AddSubscriptionResponse>() {
            @Override
            public void onResponse(Call<AddSubscriptionResponse> call, Response<AddSubscriptionResponse> response) {
                if (!response.isSuccessful()) {
                    switchSearchModeProgress();
                Snackbar.make(loginButton, R.string.add_error, Snackbar.LENGTH_LONG).show();
                } else {
                    AddSubscriptionResponse result = response.body();
                    if (result.getNumResults() > 0) {

                        InoreaderExtension.refreshCategoriesAndSources(MainActivity.this, new InoreaderExtension.OnCategoryAndSourceRefreshed() {
                            @Override
                            public void onFinished() {
                                adapter.reload();
                                switchSearchMode();
                                switchSearchModeProgress();
                                addFeedText.setText("");
                                catSourceList.smoothScrollToPosition(adapter.getItemCount());
                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                switchSearchModeProgress();
                                Snackbar.make(loginButton, R.string.add_error, Snackbar.LENGTH_LONG).show();
                            }

                            @Override
                            public void onprogressChanged(int progress) {

                            }
                        });


                    } else {
                        switchSearchModeProgress();
                        Snackbar.make(loginButton, R.string.add_error, Snackbar.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AddSubscriptionResponse> call, Throwable t) {
                switchSearchModeProgress();
                Snackbar.make(loginButton, R.string.add_error, Snackbar.LENGTH_LONG).show();
            }
        });

    }


    private void initList() {

        CategorySourceItemTouchCallback categorySourceItemTouchCallback = new CategorySourceItemTouchCallback(this, ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, catSourceList);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(categorySourceItemTouchCallback);
        adapter = new CategorySourceAdapter(this, itemTouchHelper);

        categorySourceItemTouchCallback.setAdapter(adapter);

        catSourceList.setLayoutManager(new LinearLayoutManager(this));
        catSourceList.setAdapter(adapter);





        itemTouchHelper.attachToRecyclerView(catSourceList);

        catSourceList.addItemDecoration(itemTouchHelper);



        if (!TextUtils.isEmpty(PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString(SharedPreferenceKeys.USER_ID, ""))) {
            loginContainer.setVisibility(View.GONE);
            catSourceList.setVisibility(View.VISIBLE);
        } else {
            catSourceList.setVisibility(View.GONE);
            loginContainer.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        logoutMenu = menu.findItem(R.id.action_logout);
        addMenu = menu.findItem(R.id.action_add);

        if (!TextUtils.isEmpty(searchText)) {
            launchAutoSearch(searchText);
            searchText = "";
        }

        return true;
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
        } else if (id == R.id.action_logout) {

            PreferenceManager.getDefaultSharedPreferences(this).edit()
                    .remove(SharedPreferenceKeys.USER_NAME)
                    .remove(SharedPreferenceKeys.TOKEN)
                    .remove(SharedPreferenceKeys.USER_ID)
                    .apply();

            List<Category> cats = Category.getAll(this);
            for (Category cat : cats) {
                cat.delete(this);
            }

            List<Source> sources = Source.getAll(this);
            for (Source source : sources) {
                source.delete(this);
            }

            catSourceList.setVisibility(View.GONE);
            loginContainer.setVisibility(View.VISIBLE);

        } else if (id == R.id.action_add) {
            switchSearchMode();
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchSearchMode() {
        inSearchMode = !inSearchMode;
        logoutMenu.setVisible(!inSearchMode);
        addMenu.setVisible(!inSearchMode);
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

    @Override
    public void onBackPressed() {
        if (inSearchMode) {
            switchSearchMode();
        } else {
            super.onBackPressed();
        }
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

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
