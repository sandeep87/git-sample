package com.example.flip3d;

import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class SwapViews implements Runnable {
	
	private boolean misFirstView;
	private ImageView imageView01;
	private ImageView imageView02;

	public SwapViews(boolean mCurrentView, ImageView image1, ImageView image2) {
		misFirstView = mCurrentView;
		imageView01 = image1;
		imageView02 = image2;
	}

	@Override
	public void run() {
	final float centerX = imageView01.getWidth() / 2.0f;
	final float centerY = imageView02.getWidth() / 2.0f;
	Flip3dAnimation rotation;
	
	if (misFirstView) {
		imageView01.setVisibility(View.GONE);
		imageView02.setVisibility(View.VISIBLE);
		imageView02.requestFocus();
		rotation = new Flip3dAnimation(-90, 0, centerX, centerY);
	}
	else {
		imageView02.setVisibility(View.GONE);
		imageView01.setVisibility(View.VISIBLE);
		imageView01.requestFocus();
		rotation = new Flip3dAnimation(90, 0, centerX, centerY);
	}
	rotation.setDuration(500);
	rotation.setFillAfter(true);
	rotation.setInterpolator(new DecelerateInterpolator());
	
	if (misFirstView) {
		imageView02.startAnimation(rotation);
	}
	else {
		imageView01.startAnimation(rotation);
	}
	}

}
