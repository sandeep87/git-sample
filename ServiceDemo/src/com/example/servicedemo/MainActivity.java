package com.example.servicedemo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private Button start_btn;
	private Button stop_btn;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		start_btn = (Button) findViewById(R.id.str_btn);
		stop_btn = (Button) findViewById(R.id.stp_btn);
		start_btn.setOnClickListener((OnClickListener) this);
		stop_btn.setOnClickListener((OnClickListener) this);
	}
		
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.str_btn :
				Log.d("onClick()", "Starting Service");
				startService(new Intent(this, MyService.class));
				break;
				
			case R.id.stp_btn :
				Log.d("onClick()", "Stopping Service");
				stopService(new Intent(this, MyService.class));
				break;
			
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
