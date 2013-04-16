package com.paradigmcreatives.bouncingball;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class BouncingBallActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bouncing_ball);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_bouncing_ball, menu);
		return true;
	}

}
