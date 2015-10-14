package com.levelup.twitterforpalabre.core.twitter;


import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by ludo on 16/07/15.
 */
public class TwitterUtil {


    public static String CALLBACK_URL = "twitterforpalabre://authorize";
    public static String URL_PARAMETER_TWITTER_OAUTH_VERIFIER = "oauth_verifier";

    // SharedPrefs
    public static String PREFERENCE_TWITTER_OAUTH_TOKEN = "TWITTER_OAUTH_TOKEN";
    public static String PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET = "TWITTER_OAUTH_TOKEN_SECRET";
    public static String PREFERENCE_TWITTER_IS_LOGGED_IN = "TWITTER_IS_LOGGED_IN";
    public static String PREFERENCE_USERNAME = "TWITTER_USERNAME";

    private RequestToken requestToken = null;
    private TwitterFactory twitterFactory = null;
    private Twitter twitter;

    private TwitterUtil() {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        // We can not release our Twitter API keys so you need to create this class with those two consts
        // using your own Twitter API keys
        configurationBuilder.setOAuthConsumerKey(TwitterKeys.CONSUMER_KEY);
        configurationBuilder.setOAuthConsumerSecret(TwitterKeys.CONSUMER_SECRET);
        Configuration configuration = configurationBuilder.build();
        twitterFactory = new TwitterFactory(configuration);
        twitter = twitterFactory.getInstance();
    }

    public TwitterFactory getTwitterFactory()
    {
        return twitterFactory;
    }

    public void setTwitterFactory(AccessToken accessToken)
    {
        twitter = twitterFactory.getInstance(accessToken);
    }

    public Twitter getTwitter()
    {
        return twitter;
    }
    public RequestToken getRequestToken() {
        if (requestToken == null) {
            try {
                requestToken = twitterFactory.getInstance().getOAuthRequestToken(CALLBACK_URL);
            } catch (TwitterException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return requestToken;
    }

    public void getTimeline() throws TwitterException {



    }

    static TwitterUtil instance = new TwitterUtil();

    public static TwitterUtil getInstance() {
        return instance;
    }


    public void reset() {
        instance = new TwitterUtil();
    }

}
