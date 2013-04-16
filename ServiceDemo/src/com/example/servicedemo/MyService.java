package com.example.servicedemo;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
	
	private static final String TAG = "MyService";
	MediaPlayer mediaPlayer;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(this, "My service created", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onCreate");
		mediaPlayer = MediaPlayer.create(this, R.raw.police);
		mediaPlayer.setLooping(false);
	}
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Toast.makeText(this, "My service started", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onStart");
		mediaPlayer.start();
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Toast.makeText(this, "My service stopped", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onStop");
		mediaPlayer.stop();
	}

}
