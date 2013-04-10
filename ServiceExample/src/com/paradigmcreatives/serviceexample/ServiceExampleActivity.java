package com.paradigmcreatives.serviceexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ServiceExampleActivity extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	private Button start;
	private Button stop;
	private Intent mIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		start = (Button) findViewById(R.id.start);
		start.setOnClickListener(this);

		stop = (Button) findViewById(R.id.stop);
		stop.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start:
			mIntent = new Intent(getApplicationContext(), Service.class);
			startService(mIntent);
			break;

		case R.id.stop:
			mIntent = new Intent(getApplicationContext(), Service.class);
			startService(mIntent);
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}