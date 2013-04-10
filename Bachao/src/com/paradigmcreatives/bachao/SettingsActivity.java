package com.paradigmcreatives.bachao;

import com.paradigmcreatives.bachao.util.Constants;
import com.paradigmcreatives.bachao.util.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SettingsActivity extends Activity implements OnClickListener {

	private RelativeLayout itemSmsLayout;

	private RelativeLayout itemCallLayout;

	private RelativeLayout itemFacebookLayout;

	private RelativeLayout itemTwitterLayout;

	private SharedPreferences preferences;

	private TextView smsBoolean;

	private TextView callBoolean;

	private TextView facebookBoolean;

	private TextView twitterBoolean;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_settings);
		
		itemSmsLayout       = (RelativeLayout) findViewById(R.id.id_list_one);
		itemCallLayout      = (RelativeLayout) findViewById(R.id.id_list_two);
		itemFacebookLayout  = (RelativeLayout) findViewById(R.id.id_list_three);
		itemTwitterLayout   = (RelativeLayout) findViewById(R.id.id_list_four);
		smsBoolean          = (TextView) findViewById(R.id.sms_bool);
		callBoolean         = (TextView) findViewById(R.id.call_bool);
		facebookBoolean     = (TextView) findViewById(R.id.facebook_bool);
		twitterBoolean      = (TextView) findViewById(R.id.twitter_bool);
		
		itemSmsLayout.setOnClickListener(this);
		itemCallLayout.setOnClickListener(this);
		itemFacebookLayout.setOnClickListener(this);
		itemTwitterLayout.setOnClickListener(this);

		preferences = getApplicationContext().getSharedPreferences(Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);

	}

	@Override
	public void onClick (View v) {
		int id = v.getId();
		switch (id) {
			case R.id.id_list_one:
				Intent intentSms = new Intent(SettingsActivity.this, SMSSettingsActivity.class);
				startActivityForResult(intentSms, 1);
				break;
			case R.id.id_list_two:
				Intent intentCall = new Intent(SettingsActivity.this, CallActivity.class);
				startActivityForResult(intentCall, 2);
				break;
			case R.id.id_list_three:
				if (Util.getInstance().isOnline(SettingsActivity.this)) {
					Intent intentFacebook = new Intent(SettingsActivity.this, FacebookActivity.class);
					startActivityForResult(intentFacebook, 3);
				}
				else {
					Toast.makeText(SettingsActivity.this, getResources().getString(R.string.network_connection), Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.id_list_four:
				if (Util.getInstance().isOnline(SettingsActivity.this)) {
					Intent intentTwitter = new Intent(SettingsActivity.this, TwitterActivity.class);
					startActivityForResult(intentTwitter, 4);
				}
				else {
					Toast.makeText(SettingsActivity.this, getResources().getString(R.string.network_connection), Toast.LENGTH_SHORT).show();
				}
				break;
		}

	}

	@Override
	protected void onResume () {

		super.onResume();
		Boolean facebook = preferences.getBoolean("FACEBOOK_CHECK", false);
		Boolean sms      = preferences.getBoolean("SMS_CHECK", false);
		Boolean twitter  = preferences.getBoolean("TWITTER_CHECK", false);
		Boolean call     = preferences.getBoolean("CALL_CHECK", false);

		// check facebook on or off
		if (facebook) {
			facebookBoolean.setText(getResources().getString(R.string.on));
			facebookBoolean.setTextColor(getResources().getColor(R.color.green));
		}
		else {
			facebookBoolean.setText(getResources().getString(R.string.list_one_boolean));
			facebookBoolean.setTextColor(getResources().getColor(R.color.red));
		}

		// check sms on or off
		if (sms) {
			smsBoolean.setText(getResources().getString(R.string.on));
			smsBoolean.setTextColor(getResources().getColor(R.color.green));
		}
		else {
			smsBoolean.setText(getResources().getString(R.string.list_one_boolean));
			smsBoolean.setTextColor(getResources().getColor(R.color.red));
		}

		// check twitter on or off
		if (twitter) {
			twitterBoolean.setText(getResources().getString(R.string.on));
			twitterBoolean.setTextColor(getResources().getColor(R.color.green));
		}
		else {
			twitterBoolean.setText(getResources().getString(R.string.list_one_boolean));
			twitterBoolean.setTextColor(getResources().getColor(R.color.red));
		}

		// check call on or off
		if (call) {
			callBoolean.setText(getResources().getString(R.string.on));
			callBoolean.setTextColor(getResources().getColor(R.color.green));
		}
		else {
			callBoolean.setText(getResources().getString(R.string.list_one_boolean));
			callBoolean.setTextColor(getResources().getColor(R.color.red));
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
