package com.paradigmcreatives.tabbar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class SecondTab extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		TextView textView = new TextView(this);
		textView.setText("Second Tab");
		setContentView(textView);
	}

}
