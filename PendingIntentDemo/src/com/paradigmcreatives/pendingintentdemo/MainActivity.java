package com.paradigmcreatives.pendingintentdemo;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	Button button;
	PendingIntent pIntent;
	Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this.getApplicationContext();
		button = (Button) findViewById(R.id.btn_action);
		Intent intent = getIntent();
		intent.setClass(mContext, PendingActivity.class);
		pIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
		
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				Intent intent = new Intent();
				try {
					pIntent.send(mContext, 0, intent);
				} catch (PendingIntent.CanceledException e) {
					System.out.println("Sending ContentIntent failed");
				}
				
			}
		});
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
