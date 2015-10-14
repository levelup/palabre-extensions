package com.levelup.twitterforpalabre.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.levelup.palabre.api.utils.PalabreUtils;
import com.levelup.twitterforpalabre.R;
import com.levelup.twitterforpalabre.core.utils.SharedPreferencesKeys;
import com.levelup.twitterforpalabre.core.twitter.TwitterUtil;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;


public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    TextView textWelcome;
    private Toolbar toolbar;
    private View normalContent;
    private View firstLaunchContainer;
    Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //new File(this.getDatabasePath(RSSSQLiteOpenHelper.getInstance(this).getDbName()).getPath()).delete();
        //DBUtils.exportDBToFile(this);

        loginButton = (Button) findViewById(R.id.button_login);
        textWelcome = (TextView) findViewById(R.id.text_welcome);

        firstLaunchContainer = findViewById(R.id.first_launch_container);
        normalContent = findViewById(R.id.normal_content);


        final Switch syncLists = (Switch) findViewById(R.id.sync_lists);
        View syncListsContainer = findViewById(R.id.sync_lists_container);

        syncLists.setChecked(PreferenceManager.getDefaultSharedPreferences(this).getBoolean(SharedPreferencesKeys.SYNC_LISTS, false));
        syncLists.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putBoolean(SharedPreferencesKeys.SYNC_LISTS, isChecked).apply();
            }
        });
        syncListsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncLists.setChecked(!syncLists.isChecked());
            }
        });


        View manageSearch = findViewById(R.id.manage_search);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TwitterAuthenticateTask().execute();
            }
        });

        manageSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ManageSearchesActivity.class));
            }
        });

        manageIntent(getIntent());
    }

    private void manageIntent(Intent intent) {
        Uri uri = intent.getData();
        if (uri != null && uri.toString().startsWith(TwitterUtil.CALLBACK_URL)) {
            Log.d(TAG, "Callback intent");
            String verifier = uri.getQueryParameter(TwitterUtil.URL_PARAMETER_TWITTER_OAUTH_VERIFIER);
            new TwitterGetAccessTokenTask().execute(verifier);
            firstLaunchContainer.setVisibility(View.GONE);
            normalContent.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "No Intent");
            if (isLoggedIn()) {
                Log.d(TAG, "Reuse saved token");
                new TwitterGetAccessTokenTask().execute("");
                firstLaunchContainer.setVisibility(View.GONE);
                normalContent.setVisibility(View.VISIBLE);
            } else {
                firstLaunchContainer.setVisibility(View.VISIBLE);
                normalContent.setVisibility(View.GONE);
            }
        }
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

    private boolean isLoggedIn() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return (sharedPreferences.getBoolean(TwitterUtil.PREFERENCE_TWITTER_IS_LOGGED_IN, false));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    class TwitterAuthenticateTask extends AsyncTask<String, String, RequestToken> {

        @Override
        protected void onPostExecute(RequestToken requestToken) {
            if (requestToken!=null)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL()));
                startActivity(intent);
            }
        }

        @Override
        protected RequestToken doInBackground(String... params) {
            return TwitterUtil.getInstance().getRequestToken();
        }
    }

    class TwitterGetAccessTokenTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPostExecute(String userName) {
            if (userName != null) {
                textWelcome.setText(getResources().getString(R.string.welcome) + " " + userName);
                Log.d(TAG, "onPostExecute sending palabre://extauth");
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("palabre://extauth"));
                    startActivity(intent);
                    MainActivity.this.finish();
                } catch (Exception e) {
                    // palabre not installed or too old
                    Snackbar.make(loginButton, R.string.intent_error, Snackbar.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {

            Twitter twitter = TwitterUtil.getInstance().getTwitter();
            RequestToken requestToken = TwitterUtil.getInstance().getRequestToken();
            if (!isNullOrEmpty(params[0])) {
                try {
                    AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, params[0]);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(TwitterUtil.PREFERENCE_TWITTER_OAUTH_TOKEN, accessToken.getToken());
                    editor.putString(TwitterUtil.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, accessToken.getTokenSecret());
                    editor.putBoolean(TwitterUtil.PREFERENCE_TWITTER_IS_LOGGED_IN, true);
                    editor.commit();



                    return twitter.showUser(accessToken.getUserId()).getScreenName();
                } catch (TwitterException e) {
                    e.printStackTrace();
                }
            } else {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String accessTokenString = sharedPreferences.getString(TwitterUtil.PREFERENCE_TWITTER_OAUTH_TOKEN, "");
                String accessTokenSecret = sharedPreferences.getString(TwitterUtil.PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");
                Log.d(TAG, "User is back");

                AccessToken accessToken = new AccessToken(accessTokenString, accessTokenSecret);
                TwitterUtil.getInstance().setTwitterFactory(accessToken);
                //return TwitterUtil.getInstance().getTwitter().showUser(accessToken.getUserId()).getScreenName();
            }

            return null;
        }

        public boolean isNullOrEmpty(String s) {
            return s == null || s.length() == 0;
        }

        public boolean isNullOrWhitespace(String s) {
            return s == null || isWhitespace(s);

        }
        private boolean isWhitespace(String s) {
            int length = s.length();
            if (length > 0) {
                for (int i = 0; i < length; i++) {
                    if (!Character.isWhitespace(s.charAt(i))) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
    }

}
