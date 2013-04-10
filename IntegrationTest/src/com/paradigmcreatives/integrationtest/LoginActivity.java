package com.paradigmcreatives.integrationtest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class LoginActivity extends Activity {

	public static Facebook mFacebook;
	private ConnectivityManager mCmgr;
	private NetworkInfo mNinfo;
	private boolean isAvailable;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mFacebook = new Facebook("250823521707781");
		mCmgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		mNinfo = mCmgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		Button mbutton = (Button) findViewById(R.id.login);
		mbutton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				isAvailable = mNinfo.isAvailable();
				if (isAvailable) {
					authorize();
				} else {

					Toast.makeText(LoginActivity.this,
							getResources().getString(R.string.ninfo),
							Toast.LENGTH_SHORT).show();

				}

			}
		});

	}

	private void authorize() {

		if (mFacebook.getAccessToken() == null) {

			String[] permissions = new String[] { "publish_stream", "email" };
			mFacebook.authorize(LoginActivity.this, permissions,
					new DialogListener() {

						public void onFacebookError(FacebookError e) {
							// TODO Auto-generated method stub

						}

						public void onError(DialogError e) {
							// TODO Auto-generated method stub

						}

						public void onComplete(Bundle values) {
							// TODO Auto-generated method stub
							Toast.makeText(
									LoginActivity.this,
									getResources().getString(R.string.token)
											+ mFacebook.getAppId(),
									Toast.LENGTH_SHORT).show();
							Intent mIntent = new Intent(LoginActivity.this,
									ShareActivity.class);
							startActivity(mIntent);
							finish();

						}

						public void onCancel() {
							// TODO Auto-generated method stub

						}
					});

		}  else  {
			
			
			Intent mIntent = new Intent(LoginActivity.this,
					ShareActivity.class);
			startActivity(mIntent);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}
}
