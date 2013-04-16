package com.paradigmcreatives.pendingintentdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class PendingActivity extends Activity {
	
	TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pending_intent);
		tv = (TextView) findViewById(R.id.text_view);
		tv.setTextSize(30);
		tv.setText("Welcome to Pending Intent");
		
	}

}
