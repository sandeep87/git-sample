package com.paradigmcreatives.bachao;

import oauth.signpost.OAuth;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.paradigmcreatives.bachao.logging.Logger;
import com.paradigmcreatives.bachao.twitter.TwitterRequestTokenActivity;
import com.paradigmcreatives.bachao.util.Constants;
import com.paradigmcreatives.bachao.util.Util;

public class TwitterActivity extends Activity implements OnClickListener {

	private Button twitter_login_btn;

	private Button twitter_logout_btn;

	private EditText customMessage;

	private CheckBox twitter_checkbox;

	private SharedPreferences twitterPrefs;

	private String twitterScreenName;

	private Button twitterSave;

	private String info;

	private boolean preferenceCheck;

	private SharedPreferences.Editor edit_check;

	public static TwitterActivity mInstance;

	public static TwitterActivity getInstance () {
		if (mInstance == null)
			mInstance = new TwitterActivity();
		return mInstance;

	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_twitter);

		twitter_login_btn     = (Button) findViewById(R.id.twitter_login);
		twitter_logout_btn    = (Button) findViewById(R.id.twitter_logout);
		customMessage         = (EditText) findViewById(R.id.post_message);
		twitter_checkbox      = (CheckBox) findViewById(R.id.twitter_check);
		twitterSave           = (Button) findViewById(R.id.twitter_save);

		twitterPrefs          = getSharedPreferences(Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
		edit_check            = twitterPrefs.edit();

		// check box logic
		preferenceCheck = twitterPrefs.getBoolean("TWITTER_CHECK", false);

		if (preferenceCheck == true) {
			twitter_checkbox.setChecked(true);
		}

		if (getIntent().hasExtra("app_kill")) {
			finish();
		}
		String msg = twitterPrefs.getString("TWITTER_MESSAGE", null);
		if (msg != null) {
			customMessage.setText(msg);
		}
		else {
			customMessage.setText("");

		}

		// set cursor position to end of the field
		int position = customMessage.getText().length();
		Editable etext = customMessage.getText();
		Selection.setSelection(etext, position);

		twitterPrefs = getSharedPreferences(Constants.KEY_APP_PREFERENCE_NAME, MODE_PRIVATE);

		twitter_login_btn.setOnClickListener(this);
		twitter_logout_btn.setOnClickListener(this);
		twitterSave.setOnClickListener(this);

		if (getIntent().hasExtra("twitter_screen_name")) {
			twitterScreenName = getIntent().getStringExtra("twitter_screen_name");
			if (twitterScreenName != null) {
				SharedPreferences.Editor edit = twitterPrefs.edit();
				String name = getResources().getString(R.string.logged_text)+"  "+ twitterScreenName;
				twitter_logout_btn.setTextColor(getResources().getColor(R.color.White));
				twitter_logout_btn.setText(name);
				edit.putString("twitter_userName", name);
				edit.commit();

			}
		}

		twitter_checkbox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick (View v) {
				if (twitter_checkbox.isChecked()) {
					twitter_checkbox.setButtonDrawable(R.drawable.check);
				}
				else {
					twitter_checkbox.setButtonDrawable(R.drawable.add);
				}
			}
		});

	}

	@Override
	public void onBackPressed () {

		if (twitter_checkbox.isChecked()) {
			edit_check.putBoolean("TWITTER_CHECK", true);
		}
		else {
			edit_check.putBoolean("TWITTER_CHECK", false);
		}
		edit_check.commit();

		info = customMessage.getText().toString().trim();
		String prefInfo = twitterPrefs.getString("TWITTER_MESSAGE", null);
		if (twitter_checkbox.isChecked()) {
			if (!twitterAuthenticate()) {
				Toast.makeText(TwitterActivity.this, getResources().getString(R.string.twitter_error_message), Toast.LENGTH_SHORT).show();
			}
			else {
				if (prefInfo != null && !info.equalsIgnoreCase(prefInfo)) {
					showAlertDialog();
				}
				else if (prefInfo == null && info.length() != 0) {
					showAlertDialog();
				}
				else {
					super.onBackPressed();
				}
			}
		}
		else {
			if (prefInfo != null && !info.equalsIgnoreCase(prefInfo)) {
				showAlertDialog();
			}
			else if (prefInfo == null && info.length() != 0) {
				showAlertDialog();
			}
			else {
				super.onBackPressed();
			}
		}
	}

	@Override
	protected void onResume () {
		super.onResume();
		updateLoginStatus();

	}

	public boolean twitterAuthenticate () {
		String token = twitterPrefs.getString(OAuth.OAUTH_TOKEN, null);
		String token_secret = twitterPrefs.getString(OAuth.OAUTH_TOKEN_SECRET, null);
		if (token != null && token_secret != null) {
			return true;
		}
		else {
			return false;
		}
	}

	public void updateLoginStatus () {
		if (twitterAuthenticate()) {
			Logger.info("TWITTER_LOGIN", "SUCCES");
			twitter_logout_btn.setVisibility(View.VISIBLE);
			twitter_logout_btn.setTextColor(getResources().getColor(R.color.White));
			String name = twitterPrefs.getString("twitter_userName", null);
			if (name != null) {
				twitter_logout_btn.setText(name);
			}
			else {
				twitter_logout_btn.setText("Log Out");
			}

			twitter_login_btn.setVisibility(View.INVISIBLE);

		}
		else {
			twitter_logout_btn.setVisibility(View.INVISIBLE);
			twitter_login_btn.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick (View v) {
		int id = v.getId();
		switch (id) {
			case R.id.twitter_login:
				if (Util.getInstance().isOnline(TwitterActivity.this)) {
					twitterAuthentication();
				}
				else {
					Toast.makeText(TwitterActivity.this, getResources().getString(R.string.network_connection), Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.twitter_logout:
				AlertDialog.Builder builder = new AlertDialog.Builder(TwitterActivity.this);

				builder.setMessage(getResources().getString(R.string.logout_text)).setCancelable(true).setPositiveButton(getResources().getString(R.string.log_out), new DialogInterface.OnClickListener() {

					public void onClick (DialogInterface dialog, int which) {
						clearCredentials();

						updateLoginStatus();
						if (twitter_checkbox.isChecked()) { // when logged out and not unchecked, uncheck it
							twitter_checkbox.setChecked(false);
							edit_check.putBoolean("TWITTER_CHECK", false);
							edit_check.commit();
							twitter_checkbox.setButtonDrawable(R.drawable.add);
						}
					}
				}).setNegativeButton(getResources().getString(R.string.cancel), null);
				builder.create().show();

				break;
			case R.id.twitter_save:
				info = customMessage.getText().toString().trim();

				if (twitter_checkbox.isChecked()) {
					edit_check.putBoolean("TWITTER_CHECK", true);
				}
				else {
					edit_check.putBoolean("TWITTER_CHECK", false);
				}

				if (twitterAuthenticate() && twitter_checkbox.isChecked()) {
					if (info.length() > 100) {
						Toast.makeText(TwitterActivity.this, getResources().getString(R.string.tweet_exceed_message), Toast.LENGTH_SHORT).show();
					}
					else {
						edit_check.putString("TWITTER_MESSAGE", info);
						edit_check.commit();
						finish();
					}
				}
				else if (!twitterAuthenticate() && twitter_checkbox.isChecked()) {
					edit_check.commit();
					Toast.makeText(TwitterActivity.this, getResources().getString(R.string.twitter_error_message), Toast.LENGTH_SHORT).show();
				}
				else {
					if (info.length() > 100) {
						Toast.makeText(TwitterActivity.this, getResources().getString(R.string.tweet_exceed_message), Toast.LENGTH_SHORT).show();
					}
					else {
						edit_check.putString("TWITTER_MESSAGE", info);
						edit_check.commit();
						finish();
					}
				}

			default:
				break;
		}
	}

	private void clearCredentials () {
		final Editor edit = twitterPrefs.edit();
		edit.remove(OAuth.OAUTH_TOKEN);
		edit.remove(OAuth.OAUTH_TOKEN_SECRET);

		CookieSyncManager.createInstance(TwitterActivity.this);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		edit.commit();
		Logger.info("TWITTER", "Logged Out");
	}

	private void twitterAuthentication () {

		if (!twitterAuthenticate()) {
			Intent i = new Intent(getApplicationContext(), TwitterRequestTokenActivity.class);
			startActivity(i);

		}

	}



	private void showAlertDialog () {
		
		AlertDialog.Builder build = new AlertDialog.Builder(TwitterActivity.this);
		build.setTitle(getResources().getString(R.string.abandom_text));
		build.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

			@Override
			public void onClick (DialogInterface dialog, int which) {
				String message = customMessage.getText().toString().trim();
				String prefMessage = twitterPrefs.getString("TWITTER_MESSAGE", null);
				if (prefMessage == null && message.length() != 0) {
					customMessage.setText("");
				}
				else if (prefMessage != null && !message.equalsIgnoreCase(prefMessage)) {
					customMessage.setText(prefMessage);
				}
				TwitterActivity.this.finish();
			}
		});

		build.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

			@Override
			public void onClick (DialogInterface dialog, int which) {
				dialog.dismiss();
				Toast.makeText(TwitterActivity.this, getResources().getString(R.string.call_error_tap), Toast.LENGTH_SHORT).show();
			}
		});

		AlertDialog alert = build.create();
		alert.show();
	}

	

}
