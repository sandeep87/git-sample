package com.paradigmcreatives.snakeandladder;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class HelpActivity extends Activity {
	
	private TextView mTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		mTextView = (TextView) findViewById(R.string.helptxt);
		
	}
}
