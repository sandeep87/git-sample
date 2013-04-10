package com.paradigmcreatives.bachao;

import java.util.ArrayList;
import java.util.Arrays;

import oauth.signpost.OAuth;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.paradigmcreatives.bachao.logging.Logger;
import com.paradigmcreatives.bachao.logging.ParadigmExceptionHandler;
import com.paradigmcreatives.bachao.util.AppPropertiesUtil;
import com.paradigmcreatives.bachao.util.Constants;
import com.paradigmcreatives.bachao.util.Util;

public class BachaoActivity extends Activity {

	private Button Sos;

	private Button settings;

	private Button info;

	private SharedPreferences preferences;

	private String mapUrl;

	private String address_location = "";

	private String[] num = new String[] { null, null, null, null, null };

	private ArrayList <String> phoneNumbers = new ArrayList <String>(Arrays.asList(num));

	private String message;

	private Vibrator viberator;

	private Boolean facebook_status;

	private Boolean twitter_status;

	private Boolean sms_status;

	private Boolean call_status;


	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_home);
		Sos             = (Button) findViewById(R.id.sos);
		settings        = (Button) findViewById(R.id.settings);
		info            = (Button) findViewById(R.id.info);		
		preferences     = getApplicationContext().getSharedPreferences(Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
		viberator       = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		initAppComponents(BachaoActivity.this);
		Sos.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick (View v) {
				viberator.vibrate(100);
				status();
				return true;
			}
		});

		settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick (View v) {
				Intent intent = new Intent(BachaoActivity.this, SettingsActivity.class);
				startActivity(intent);
			}
		});

		info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick (View v) {

				Intent intent = new Intent(BachaoActivity.this, InfoDetailsActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
			}
		});

	}

	private boolean initAppComponents (Context ctx) {
		boolean initialized = true;

		Context context = ctx;

		// Initializing the logger and app properties
		initialized = AppPropertiesUtil.initAppDirectory(context) && Logger.init(context) && AppPropertiesUtil.init(context);

		// Initializing the Exception Handler and setting it as the default for
		// all the uncaught exceptions
		//
		String logDirectoryPath = AppPropertiesUtil.getAppDirectory(context) + getResources().getString(R.string.log_folder);

		ParadigmExceptionHandler paradigmException = new ParadigmExceptionHandler(this, logDirectoryPath);

		Thread.setDefaultUncaughtExceptionHandler(paradigmException);
		Logger.info("Initialization components " + initialized);

		return initialized;
	}

	public void status () {

		facebook_status     = preferences.getBoolean("FACEBOOK_CHECK", false);
		sms_status          = preferences.getBoolean("SMS_CHECK", false);
		twitter_status      = preferences.getBoolean("TWITTER_CHECK", false);
		call_status         = preferences.getBoolean("CALL_CHECK", false);

		if (!sms_status && !call_status && !twitter_status && !facebook_status) {
			Toast.makeText(BachaoActivity.this, getResources().getString(R.string.error_msg), Toast.LENGTH_SHORT).show();
		}
		else if (!(call_status && !twitter_status && !facebook_status && !sms_status)) {
			new AsyncTask <Void, Void, Void>() {

				@Override
				protected Void doInBackground (Void... params) {
					mapUrl = Util.getInstance().getLatLong(BachaoActivity.this);
					address_location = Util.getInstance().getDetailedAddress(BachaoActivity.this);
					return null;
				}

				@Override
				protected void onPostExecute (Void result) {
					sendAndCall();
				}

			}.execute();

		}

	}

	public void sendAndCall () {
		new Thread(new Runnable() {

			@Override
			public void run () {
				Looper.prepare();
				if (call_status) {
					call();
					if (facebook_status) {
						if (isOnline()) {
							FacebookActivity fb = new FacebookActivity();
							fb.onClickPostStatusUpdate(BachaoActivity.this);
						}
						else {
							Toast.makeText(BachaoActivity.this, getResources().getString(R.string.network_connection), Toast.LENGTH_SHORT).show();
						}
					}
				}

				if (!call_status && facebook_status) {
					if (isOnline()) {
						FacebookActivity fb = new FacebookActivity();
						fb.onClickPostStatusUpdate(BachaoActivity.this);
					}
					else {
						Toast.makeText(BachaoActivity.this, getResources().getString(R.string.network_connection), Toast.LENGTH_SHORT).show();
					}
				}

				Looper.loop();
			}
		}).start();

		new AsyncTask <Void, Void, Void>() {

			@Override
			protected Void doInBackground (Void... params) {
				if (sms_status != null && sms_status) {
					
					for (int i = 0; i < 5; i++) {
						String number_i = preferences.getString("NUMBER_" + i, null);
						if (number_i != null) {
							phoneNumbers.set(i, number_i);
						}
					}

					String pref_msg = preferences.getString("SMS_MESSAGE", null);
					if (pref_msg != null && pref_msg.length() > 0) {
						message = pref_msg;
					}
					else if (pref_msg.length() == 0) {
						message = getResources().getString(R.string.message_one);
					}
					for (int i = 0; i < phoneNumbers.size(); i++) {

						String phone = phoneNumbers.get(i);

						String msg = message + "." + "\n" + getResources().getString(R.string.message_two) + " " + address_location + " " + mapUrl;
						if (phone != null) {
						
							
							Util.getInstance().sendSms(phone, msg, BachaoActivity.this);
						}
					}
				}
				
				if (twitter_status) {
					if (Util.getInstance().isOnline(BachaoActivity.this)) {
						tweet();
					}
					else {
						Toast.makeText(BachaoActivity.this, getResources().getString(R.string.network_connection), Toast.LENGTH_SHORT).show();
					}

				}

				return null;
			}

		}.execute();
	}

	public void sendTweet (final String msg) {

		new Thread(new Runnable() {

			@Override
			public void run () {
				Looper.prepare();
				if (Util.getInstance().isOnline(BachaoActivity.this)) {

					String token = preferences.getString(OAuth.OAUTH_TOKEN, "");
					String secret = preferences.getString(OAuth.OAUTH_TOKEN_SECRET, "");

					AccessToken a = new AccessToken(token, secret);
					Twitter twitter = new TwitterFactory().getInstance();
					twitter.setOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
					twitter.setOAuthAccessToken(a);
					try {
						twitter.updateStatus(msg);
						Toast.makeText(BachaoActivity.this, getResources().getString(R.string.twitter_succes_message), Toast.LENGTH_SHORT).show();
						Logger.info("TWITTER", getResources().getString(R.string.twitter_succes_message));
					}
					catch (TwitterException e) {
						Toast.makeText(BachaoActivity.this, getResources().getString(R.string.twitter_duplicate_message), Toast.LENGTH_SHORT).show();
						Logger.logStackTrace(e);
					}
				}
				else {
					Toast.makeText(BachaoActivity.this,getResources().getString(R.string.network_connection), Toast.LENGTH_SHORT).show();
				}
				Looper.loop();
			}
		}).start();

	}

	public void call () {

		// calling to configured phone number
		String prefsNumber = preferences.getString("key_number", null);
		if (prefsNumber != null && prefsNumber.length() > 0) {
			try {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:" + prefsNumber));
				startActivity(callIntent);
			}
			catch (ActivityNotFoundException e) {
				Log.e("helloandroid dialing example", "Call failed", e);
			}
			catch (Exception e) {
				Toast.makeText(BachaoActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
		else {
			Toast.makeText(BachaoActivity.this, getResources().getString(R.string.call_error_message), Toast.LENGTH_SHORT).show();
		}
	}

	public void tweet () {
		String twitterMessage = preferences.getString("TWITTER_MESSAGE", null);
		if (twitterMessage != null && twitterMessage.length() > 0) {

			String message = twitterMessage + "." + "\n" + getResources().getString(R.string.message_two) + " " + mapUrl;
			try {
				sendTweet(message);
			}
			catch (Exception e) {
				Logger.logStackTrace(e);
			}
		}
		else {
			String message = getResources().getString(R.string.message_one) + "." + "\n" + getResources().getString(R.string.message_two) + " " + mapUrl;
			try {
				sendTweet(message);
			}
			catch (Exception e) {
				Logger.logStackTrace(e);
			}
		}
	}

	public boolean isOnline () {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

}
