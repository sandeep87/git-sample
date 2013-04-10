package com.example.flip3d;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

public class MainActivity extends Activity {
	
	private ImageView imageView1;
	private ImageView imageView2;
	private boolean isFirstImage = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		imageView1 = (ImageView)findViewById(R.id.image_view1);
		imageView2 = (ImageView)findViewById(R.id.image_view2);
		imageView2.setVisibility(View.GONE);
		imageView1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isFirstImage) {
					applyRotation(0, 90);
					isFirstImage = !isFirstImage;
				}
				else {
					applyRotation(0, -90);
					isFirstImage = !isFirstImage;
				}
			}
		});
	}

	protected void applyRotation(float start, float end) {
		final float centerX = imageView1.getWidth() / 2.0f;
		final float centerY = imageView2.getWidth() / 2.0f;
		final Flip3dAnimation rotation = new Flip3dAnimation(start, end, centerX, centerY);
		rotation.setDuration(500);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new DisplayNextView(isFirstImage, imageView1, imageView2));
		
		if(isFirstImage) {
			imageView1.startAnimation(rotation);
		}
		else {
			imageView2.startAnimation(rotation);
		}
	}

	
}
