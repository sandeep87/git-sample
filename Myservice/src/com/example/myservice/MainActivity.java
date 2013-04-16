package com.example.myservice;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.webkit.WebView.FindListener;
import android.widget.Button;

public class MainActivity extends Service {
	
	private Button btn_start;
	private Button btn_stop;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d("onCreate()", "Service created");
		
		
		btn_start = (Button)findViewById(R.id.start_btn);
		btn_stop = (Button)findViewById(R.id.stop_btn);
		
		btn_start.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			
				
				
			}
		});
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.d("onStart()", "Service started");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("onDestroy()", "Service destroyed");
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d("onBind()", "Service bound");
		return null;
	}

}
