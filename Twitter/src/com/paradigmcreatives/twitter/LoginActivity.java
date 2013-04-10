package com.paradigmcreatives.twitter;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends Activity {

	private Button mTweet;
	private OAuthConsumer mConsumer;
	private OAuthProvider mProvider;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		mConsumer = new CommonsHttpOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		mProvider = new CommonsHttpOAuthProvider(Constants.REQUEST_URL, Constants.ACCESS_URL, Constants.AUTHORIZE_URL);
		
		mTweet = (Button) findViewById(R.id.tweet);
		mTweet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				storeToken();
				AccessToken a = getAccessToken();
				Twitter twitter = new TwitterFactory().getInstance();
				twitter.setOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
				twitter.setOAuthAccessToken(a);

				try {
					twitter.getAccountSettings();
				} catch (TwitterException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private AccessToken getAccessToken() {
		SharedPreferences tokenPreferences = getPreferences(MODE_PRIVATE);
		String token = tokenPreferences.getString("Token", "");
		String secretToken = tokenPreferences.getString("Secret_key", "");
		if (token != null && secretToken != null) {
			return new AccessToken(token, secretToken);
		} else {
			return null;
		}
	}
	
	private void storeToken(){
		SharedPreferences storePrefs = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = storePrefs.edit();
		editor.putString("Token", mConsumer.getToken());
		editor.putString("Secret_key", mConsumer.getTokenSecret());
		editor.commit();
	}
}
