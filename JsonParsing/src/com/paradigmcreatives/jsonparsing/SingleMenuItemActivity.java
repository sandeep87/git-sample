package com.paradigmcreatives.jsonparsing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SingleMenuItemActivity extends Activity {
	
	private static final String TAG_NAME = "name";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_PHONE_MOBILE = "mobile";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.singlelist_item);
			
			Intent intent = getIntent();
			
			String name = intent.getStringExtra(TAG_NAME);
			String cost = intent.getStringExtra(TAG_EMAIL);
			String description = intent.getStringExtra(TAG_PHONE_MOBILE);
			
			TextView lbl_name = (TextView) findViewById(R.id.name_label);
			TextView lbl_email = (TextView) findViewById(R.id.email_label);
			TextView lbl_mobile = (TextView) findViewById(R.id.mobile_label);
			
			lbl_name.setText(name);
			lbl_email.setText(cost);
			lbl_mobile.setText(description);
	}

}
