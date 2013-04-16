package com.paradigmcreatives.alarmpendingintent;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private PendingIntent pIntent;
	private Button btn_start;
	private Button btn_cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn_start = (Button) findViewById(R.id.start_btn);
		btn_cancel = (Button) findViewById(R.id.cancel_btn);
		
		btn_start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent mIntent = new Intent(MainActivity.this, AlarmService.class);
				PendingIntent pIntent = PendingIntent.getService(MainActivity.this, 0, mIntent, 0);
				
				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());
				calendar.add(Calendar.SECOND, 10);
				alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
				Toast.makeText(MainActivity.this, "Start Alarm", Toast.LENGTH_LONG).show();
				
			}
		});
		
		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				alarmManager.cancel(pIntent);
				
				Toast.makeText(MainActivity.this, "Cancel Alarm", Toast.LENGTH_LONG).show();
				
			}
		});
	}

  
}
