package com.paradigmcreatives.frameanimation;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class FrameAnimator extends Activity {
	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_frame_animator);
		this.setupButton();
	}

	private void setupButton() {
		button = (Button) findViewById(R.id.start);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				parentButtonClicked(v);
				
			}

			});
		
	}
	private void parentButtonClicked(View v) {
		animate();
		
	}

	private void animate() {
		ImageView imageView = (ImageView) findViewById(R.id.animatedimage);
		imageView.setVisibility(ImageView.VISIBLE);
		imageView.setBackgroundResource(R.drawable.ani);
		AnimationDrawable frameAnimation = (AnimationDrawable) imageView.getBackground();
		if(frameAnimation.isRunning()) {
			frameAnimation.stop();
			button.setText("Start");
		}
		else {
			frameAnimation.start();
			button.setText("Stop");
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_frame_animator, menu);
		return true;
	}

}
