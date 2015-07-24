package com.levelup.palabre.flickrforpalabre;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.levelup.palabre.api.datamapping.Category;
import com.levelup.palabre.api.datamapping.Source;
import com.levelup.palabre.flickrforpalabre.data.UniqueIds;
import com.levelup.palabre.flickrforpalabre.flickr.FlickrService;
import com.levelup.palabre.flickrforpalabre.flickr.FlickrServiceInterface;
import com.levelup.palabre.flickrforpalabre.flickr.FlickrUtils;
import com.levelup.palabre.flickrforpalabre.flickr.data.UserLoginResponse;
import com.levelup.palabre.flickrforpalabre.flickr.data.userresponse.UserResponse;
import com.levelup.palabre.flickrforpalabre.flickr.utils.SharedPreferenceKeys;

import org.scribe.exceptions.OAuthException;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by nicolas on 13/04/15.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Toolbar toolbar;
    private RecyclerView subscriptionList;
    private CategorySourceAdapter adapter;
    private SharedPreferences sharedPreferences;
    private MenuItem logoutMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (BuildConfig.DEBUG)
            Log.d(TAG, "oauth: " + sharedPreferences.getString(SharedPreferenceKeys.LOGIN_SECRET, ""));
        if (BuildConfig.DEBUG)
            Log.d(TAG, "oauth: " + sharedPreferences.getString(SharedPreferenceKeys.TOKEN_TOKEN, ""));

        subscriptionList = (RecyclerView) findViewById(R.id.subscription_list);

        subscriptionList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CategorySourceAdapter(this,  new CategorySourceAdapter.OnLoginClickListener() {
            @Override
            public void onLoginClicked() {
                manageLoginClickListener();
            }
        });
        subscriptionList.setAdapter(adapter);




    }


    @Override
    protected void onNewIntent(Intent intent) {
        final String myScheme = intent.getScheme();
        if (BuildConfig.DEBUG) Log.d(TAG, "Scheme: " + myScheme);
        if (myScheme != null && myScheme.equals("flickrforpalabre")) {
            logUser(intent.getDataString());
        }
        super.onNewIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        logoutMenu = menu.findItem(R.id.menu_logout);
        if (TextUtils.isEmpty(sharedPreferences.getString(SharedPreferenceKeys.LOGIN_USERNAME, ""))) {
            logoutMenu.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.interesting) {

            List<Category> cats = Category.getAll(this);
            Category otherCat = null;
            for (Category cat : cats) {
                if (cat.getUniqueId().equals(UniqueIds.CATEGORY_OTHERS)) {
                    otherCat = cat;
                }
            }

            if (otherCat == null) {
                Category category = new Category().title(getString(R.string.others)).uniqueId(UniqueIds.CATEGORY_OTHERS);
                category.save(this);

                Set<Category> categories = new HashSet<>();
                categories.add(category);
                Source source = new Source()
                        .title(getString(R.string.interesting))
                        .uniqueId(UniqueIds.SOURCE_INTERRESTING)
                        .iconUrl("android.resource://com.levelup.palabre.flickrforpalabre/" + R.drawable.ic_interresting)
                        .categories(categories);
                source.save(this);
            } else {
                List<Source> sources = Source.getAllWithCategories(this);
                for (Source source : sources) {
                    for (Category category : source.getCategories()) {
                        if (category.getUniqueId().equals(UniqueIds.CATEGORY_OTHERS)) {
                            source.delete(this);
                        }
                    }
                }
                otherCat.delete(this);
            }

            loadUserSubscriptions();
        } else  if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId() == R.id.menu_logout) {
            logout();
        }
        return super.onOptionsItemSelected(item);
    }


    private void loadUserSubscriptions() {

        adapter.reload();

    }

    private void oAuth() {
        // Replace these with your own api key and secret
        OAuthService service = FlickrForPalabreApplication.getoAuthService();
        Token mRequestToken = FlickrForPalabreApplication.getRequestToken();

        if (mRequestToken == null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, getString(R.string.oauth_connection_error), Toast.LENGTH_LONG).show();
                }
            });
            return;
        }

        final String authorizationUrl = service.getAuthorizationUrl(mRequestToken);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
        editor.putString(SharedPreferenceKeys.TOKEN_TOKEN, mRequestToken.getToken())
                .putString(SharedPreferenceKeys.TOKEN_SECRET, mRequestToken.getSecret())
                .putString(SharedPreferenceKeys.TOKEN_RESPONSE, mRequestToken.getRawResponse())
                .apply();

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(authorizationUrl)));

    }


    private void manageLoginClickListener() {
        if (TextUtils.isEmpty(sharedPreferences.getString(SharedPreferenceKeys.LOGIN_USERNAME, ""))) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    oAuth();
                }
            }).start();
        } else {


            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(sharedPreferences.getString(SharedPreferenceKeys.LOGIN_URL, ""))));

        }
    }

    private void logout() {
        MainActivity.this.sharedPreferences.edit()
                .remove(SharedPreferenceKeys.LOGIN_USERNAME)
                .remove(SharedPreferenceKeys.LOGIN_REAL_NAME)
                .remove(SharedPreferenceKeys.LOGIN_URL)
                .remove(SharedPreferenceKeys.LOGIN_AVATAR)
                .remove(SharedPreferenceKeys.LOGIN_NSID)
                .remove(SharedPreferenceKeys.LOGIN_SECRET)
                .remove(SharedPreferenceKeys.LOGIN_TOKEN)
                .remove(SharedPreferenceKeys.TOKEN_RESPONSE)
                .remove(SharedPreferenceKeys.TOKEN_SECRET)
                .remove(SharedPreferenceKeys.TOKEN_TOKEN)
                .apply();

        FlickrForPalabreApplication.resetRequestToken();

        adapter.reload();
        FlickrService.getInstance(MainActivity.this).resetAdapter();
        List<Category> cats = Category.getAll(this);
        for (Category cat : cats) {
            cat.delete(this);
        }

        List<Source> sources = Source.getAll(this);
        for (Source source : sources) {
            source.delete(this);
        }
        unLogUser();
        logoutMenu.setVisible(false);
    }


    private void unLogUser() {
        adapter.reload();
    }

    private void logUser(String url) {
        Uri uri = Uri.parse(url);
        logoutMenu.setVisible(true);


        String verifierS = uri.getQueryParameter("oauth_verifier");
        final Verifier verifier = new Verifier(verifierS);

        // Trade the Request Token and Verfier for the Access Token

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    OAuthService service = FlickrForPalabreApplication.getoAuthService();
                    Token mRequestToken = FlickrForPalabreApplication.getRequestToken();

                    if (mRequestToken == null) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, getString(R.string.oauth_connection_error), Toast.LENGTH_LONG).show();
                            }
                        });
                        return;
                    }

                    Token accessToken = service.getAccessToken(mRequestToken, verifier);
//                    mRequestToken = new Token(FlickrMuzeiApplication.getSettings().getString(PreferenceKeys.TOKEN_TOKEN, ""),FlickrMuzeiApplication.getSettings().getString(PreferenceKeys.TOKEN_SECRET, ""),FlickrMuzeiApplication.getSettings().getString(PreferenceKeys.TOKEN_RESPONSE, ""));


                    final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    final SharedPreferences.Editor editor = settings.edit();
                    editor.putString(SharedPreferenceKeys.LOGIN_TOKEN, accessToken.getToken());
                    editor.putString(SharedPreferenceKeys.LOGIN_SECRET, accessToken.getSecret());
                    editor.apply();


                    //Get the user information
                    FlickrService.getInstance(MainActivity.this).getLogin(new FlickrServiceInterface.IRequestListener<UserLoginResponse>() {
                        @Override
                        public void onFailure() {
                            Log.e(TAG, "Can't get username");

                        }

                        @Override
                        public void onSuccess(final UserLoginResponse userLoginResponse) {
                            if (BuildConfig.DEBUG)
                                Log.d(TAG, "Looking for user");


                            //The user has not been found
                            if (userLoginResponse == null || userLoginResponse.getUser() == null || userLoginResponse.getUser().getUsername() == null) {
                                Log.e(TAG, "Can't get username");
                            } else {
                                if (BuildConfig.DEBUG)
                                    Log.d(TAG, "User found: " + userLoginResponse.getUser().getId());
                                editor.putString(SharedPreferenceKeys.LOGIN_USERNAME, userLoginResponse.getUser().getUsername().get_content());
                                editor.putString(SharedPreferenceKeys.LOGIN_NSID, userLoginResponse.getUser().getId());
                                editor.apply();

                                //Get the avatar
                                FlickrService.getInstance(MainActivity.this).getUser(userLoginResponse.getUser().getId(), new FlickrServiceInterface.IRequestListener<UserResponse>() {
                                    @Override
                                    public void onFailure() {

                                    }

                                    @Override
                                    public void onSuccess(UserResponse response) {

                                        //First load for the sources
                                        FlickrExtension.loadSources(MainActivity.this, new FlickrExtension.OnTaskFinishedListener() {
                                            @Override
                                            public void onTaskFinished() {
                                                FlickrUtils.loadLicences(MainActivity.this);

                                                loadUserSubscriptions();
                                            }
                                        });
                                        editor.putString(SharedPreferenceKeys.LOGIN_REAL_NAME, response.getPerson().getRealname().get_content());
                                        editor.putString(SharedPreferenceKeys.LOGIN_URL, response.getPerson().getProfileurl().get_content());
                                        editor.putString(SharedPreferenceKeys.LOGIN_AVATAR, "http://farm" + response.getPerson().getIconfarm() + ".staticflickr.com/" + response.getPerson().getIconserver() + "/buddyicons/" + userLoginResponse.getUser().getId() + ".jpg");
                                        editor.apply();
                                        adapter.reload();
                                        loadUserSubscriptions();
                                    }
                                });

                            }


                        }
                    });
                } catch (OAuthException e) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, getString(R.string.oauth_error), Toast.LENGTH_LONG).show();
                        }
                    });

                }


            }
        }).start();

    }
}
