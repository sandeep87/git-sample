package com.paradigmcreatives.serviceexample;

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class Service extends android.app.Service {

	@Override
	public IBinder onBind(Intent intent) {
				return null;
	}
	
	@Override
	public void onCreate() {
		Toast.makeText(getApplicationContext(), "Service onCreate", Toast.LENGTH_LONG).show();
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		Toast.makeText(getApplicationContext(), "Service onDestroy", Toast.LENGTH_LONG).show();
		super.onDestroy();
	}
	
	@Override
	public void onLowMemory() {
		Toast.makeText(getApplicationContext(), "Service onLowMemory", Toast.LENGTH_LONG).show();
		super.onLowMemory();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		Toast.makeText(getApplicationContext(), "Service onStart", Toast.LENGTH_LONG).show();
		super.onStart(intent, startId);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(getApplicationContext(), "Service onStartCommand", Toast.LENGTH_LONG).show();
		ThreadDemo td = new ThreadDemo();
		td.start();
		return super.onStartCommand(intent, flags, startId);
	}
	
	private class ThreadDemo extends Thread {
		@Override
		public void run() {
			super.run();
			try {
				sleep(70*1000);
				handler.sendEmptyMessage(0);
				} catch (InterruptedException e) {
				e.getMessage();
			}
		}
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
		};
		
	};

}
