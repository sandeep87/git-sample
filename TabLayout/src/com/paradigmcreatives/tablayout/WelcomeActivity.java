package com.paradigmcreatives.tablayout;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class WelcomeActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView textView = new TextView(this);
		textView.setText("Welcome");
		setContentView(textView);
	}

}
