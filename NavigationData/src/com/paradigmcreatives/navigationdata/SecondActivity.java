package com.paradigmcreatives.navigationdata;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends Activity {
	private TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.secondactivity);
		textView = (TextView) findViewById(R.id.textView);
		Intent intent = getIntent();
		String value = intent.getStringExtra("key");
		textView.setText(value);
	}

}
