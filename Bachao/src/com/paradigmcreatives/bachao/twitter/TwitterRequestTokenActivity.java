package com.paradigmcreatives.bachao.twitter;

import java.util.SortedSet;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager.BadTokenException;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.paradigmcreatives.bachao.R;
import com.paradigmcreatives.bachao.TwitterActivity;
import com.paradigmcreatives.bachao.logging.Logger;
import com.paradigmcreatives.bachao.util.Constants;

/**
 * Prepares a OAuthConsumer and OAuthProvider
 * 
 * OAuthConsumer is configured with the consumer key & consumer secret. OAuthProvider is configured with the 3 OAuth endpoints.
 * 
 * Execute the OAuthRequestTokenTask to retrieve the request, and authorize the request.
 * 
 * After the request is authorized, a callback is made here.
 * 
 */
public class TwitterRequestTokenActivity extends Activity {

	final String TAG = getClass().getName();

	private OAuthConsumer consumer;

	private OAuthProvider provider;

	private WebView webView;

	ProgressDialog dialog;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		webView = (WebView) findViewById(R.id.webview);
		dialog = new ProgressDialog(TwitterRequestTokenActivity.this);
		dialog.setMessage(getResources().getString(R.string.loading_wait));
		dialog.setCancelable(false);
		try {
			dialog.show();
		}
		catch (IllegalArgumentException e) {
			Logger.logStackTrace(e);
		}
		catch (BadTokenException e) {
			Logger.logStackTrace(e);
		}

		try {
			this.consumer = new CommonsHttpOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
			this.provider = new CommonsHttpOAuthProvider(Constants.REQUEST_URL, Constants.ACCESS_URL, Constants.AUTHORIZE_URL);
		}
		catch (Exception e) {
			Log.e(TAG, "Error creating consumer / provider", e);
			Logger.info("SOS_TWITTERError creating consumer / provider", e.getMessage());
		}

		Log.i(TAG, "Starting task to retrieve request token.");
		new AsyncTask <Void, Void, Void>() {

			@SuppressLint("SetJavaScriptEnabled")
			@Override
			protected Void doInBackground (Void... params) {
				try {
					String url = provider.retrieveRequestToken(consumer, Constants.OAUTH_CALLBACK_URL);

					WebSettings settings = webView.getSettings();
					settings.setJavaScriptEnabled(true);
					settings.setDomStorageEnabled(true);
					webView.loadUrl(url);
				}
				catch (OAuthMessageSignerException e) {
				}
				catch (OAuthNotAuthorizedException e) {
				}
				catch (OAuthExpectationFailedException e) {
				}
				catch (OAuthCommunicationException e) {
				}
				return null;
			}

			protected void onPostExecute (Void result) {
				Thread thread = new Thread(new Runnable() {

					@Override
					public void run () {
						try {
							if (dialog != null) {
								dialog.dismiss();
							}
						}
						catch (IllegalArgumentException e) {
							Logger.logStackTrace(e);
						}
						catch (BadTokenException e) {
							Logger.logStackTrace(e);
						}
					}
				});
				try {
					Thread.sleep(2000);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				thread.start();

			};
		}.execute();

		// new OAuthRequestTokenTask(this,consumer,provider).execute();
	}

	/**
	 * Called when the OAuthRequestTokenTask finishes (user has authorized the request token). The callback URL will be intercepted here.
	 */
	@Override
	public void onNewIntent (Intent intent) {
		super.onNewIntent(intent);
		SharedPreferences prefs = getSharedPreferences(Constants.KEY_APP_PREFERENCE_NAME, MODE_PRIVATE);
		final Uri uri = intent.getData();
		if (uri != null && uri.getScheme().equals(Constants.OAUTH_CALLBACK_SCHEME)) {
			Log.i(TAG, "Callback received : " + uri);
			Log.i(TAG, "Retrieving Access Token");
			Logger.info("Retrieving Access Token");

			new RetrieveAccessTokenTask(this, consumer, provider, prefs).execute(uri);
			finish();
		}
	}

	@SuppressLint("NewApi")
	public class RetrieveAccessTokenTask extends AsyncTask <Uri, Void, Void> {

		private Context context;

		private OAuthProvider provider;

		private OAuthConsumer consumer;

		private SharedPreferences prefs;

		public RetrieveAccessTokenTask (Context context, OAuthConsumer consumer, OAuthProvider provider, SharedPreferences prefs) {
			this.context = context;
			this.consumer = consumer;
			this.provider = provider;
			this.prefs = prefs;
		}

		/**
		 * Retrieve the oauth_verifier, and store the oauth and oauth_token_secret for future API calls.
		 */
		@Override
		protected Void doInBackground (Uri... params) {
			final Uri uri = params[0];
			final String oauth_verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);

			try {
				provider.retrieveAccessToken(consumer, oauth_verifier);

				final Editor edit = prefs.edit();
				edit.putString(OAuth.OAUTH_TOKEN, consumer.getToken());
				edit.putString(OAuth.OAUTH_TOKEN_SECRET, consumer.getTokenSecret());
				edit.commit();

				String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
				String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");

				consumer.setTokenWithSecret(token, secret);
				SortedSet <String> screenNameSet = provider.getResponseParameters().get("screen_name");
				String screenName = screenNameSet.first();
				Intent intent = new Intent(context, TwitterActivity.class);
				intent.putExtra("twitter_screen_name", screenName);
				intent.putExtra("app_kill", "kill");
				// intent.addCategory(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
				finish();
				Log.i(TAG, "OAuth - Access Token Retrieved");

				Logger.info("OAuth - Access Token Retrieved");

			}
			catch (Exception e) {
				Log.e(TAG, "OAuth - Access Token Retrieval Error", e);
				Logger.info("SOS_TWITTERError creating consumer / provider", e.getMessage());
			}

			return null;
		}

	}

	@Override
	protected void onRestart () {
		super.onRestart();
		finish();
	}

}
