package com.paradigmcreatives.fadeinoutanimationsample;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

//	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

//	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void scaleupAnimation(View view) {
		ActivityOptions opts = ActivityOptions.makeScaleUpAnimation(view, 0, 0,
				view.getWidth(), view.getHeight());
		startActivity(new Intent(MainActivity.this, AnimationActivity.class));
	}

//	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void thumbNailScaleAnimation(View view) {
		view.setDrawingCacheEnabled(true);
		view.setPressed(false);
		view.refreshDrawableState();
		Bitmap bitmap = view.getDrawingCache();
		ActivityOptions opts = ActivityOptions.makeThumbnailScaleUpAnimation(
				view, bitmap, 0, 0);
		startActivity(new Intent(MainActivity.this, AnimationActivity.class),
				opts.toBundle());
		view.setDrawingCacheEnabled(false);
	}

//	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void fadeAnimation(View view) {
		ActivityOptions opts = ActivityOptions.makeCustomAnimation(
				MainActivity.this, R.anim.fade_in, R.anim.fade_out);
		startActivity(new Intent(MainActivity.this, AnimationActivity.class),
				opts.toBundle());
	}
}
