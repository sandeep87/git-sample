package com.paradigmcreatives.tabbar;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class TabBarActivity extends TabActivity {
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabbar);
		TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);

		TabSpec fisrtTabSpec = tabHost.newTabSpec("first");
		TabSpec secondTabSpec = tabHost.newTabSpec("second");

		fisrtTabSpec.setIndicator("First tab name").setContent(
				new Intent(this, FirstTab.class));
		secondTabSpec.setIndicator("Second tab name").setContent(
				new Intent(this, SecondTab.class));
		tabHost.addTab(fisrtTabSpec);
		tabHost.addTab(secondTabSpec);
	}

}