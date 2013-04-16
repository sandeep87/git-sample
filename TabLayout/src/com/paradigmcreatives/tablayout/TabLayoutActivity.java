package com.paradigmcreatives.tablayout;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class TabLayoutActivity extends TabActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tablayout);
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent = new Intent().setClass(this, WelcomeActivity.class);
		spec = tabHost
				.newTabSpec("Welcome")
				.setIndicator("Welcome",
				getResources().getDrawable(R.drawable.ic_launcher))
				.setContent(intent);
		tabHost.addTab(spec);
	}
}