package com.paradigmcreatives.intentdemo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class MainActivity extends Activity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		String url = "http://www.gmail.com";
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		//i.setData(Uri.parse(url));
		startActivity(i);
		
	}

	
}
