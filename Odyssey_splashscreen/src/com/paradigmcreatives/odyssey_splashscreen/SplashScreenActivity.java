package com.paradigmcreatives.odyssey_splashscreen;

import android.os.Bundle;
import android.app.Activity;

import android.widget.MediaController;
import android.widget.VideoView;

public class SplashScreenActivity extends Activity {

	private VideoView mVideoView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		mVideoView = (VideoView) findViewById(R.id.splash_video);
		mVideoView.setMediaController(new MediaController(this));
	    mVideoView.setVideoPath("android.resource://com.paradigmcreatives.odyssey_splashscreen/raw/splash");
		mVideoView.start();
		
	}

}
