package com.paradigmcreatives.loginscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class WelcomeActivity1 extends Activity {
	private TextView mTextView;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		mTextView = (TextView)findViewById(R.id.textView);
		Intent intent = getIntent();
		String res = intent.getStringExtra("key");
		mTextView.setText("Welcome "+res);
}
}
