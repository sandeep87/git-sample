package com.paradigmcreatives.alarmpendingintent;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class AlarmService extends Service {
	
	@Override
	public void onCreate() {
		Toast.makeText(this, "AlarmService.crate()", Toast.LENGTH_LONG).show();
	}

	@Override
	public IBinder onBind(Intent intent) {
		Toast.makeText(this, "AlarmService.onBind()", Toast.LENGTH_LONG).show();
		return null;
	}
	
	@Override
	public void onDestroy() {
		Toast.makeText(this, "AlarmService.onDestroy()", Toast.LENGTH_LONG).show();
		
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		Toast.makeText(this, "AlarmService.onStart()", Toast.LENGTH_LONG).show();
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		Toast.makeText(this, "AlarmService.onUnbind()", Toast.LENGTH_LONG).show();
		return super.onUnbind(intent);
	}

}
