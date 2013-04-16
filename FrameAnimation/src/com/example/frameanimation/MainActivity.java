package com.example.frameanimation;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Button mButton;
	private TextView mTextView;
	private ImageView mImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.setUpButton();
	}

	private void setUpButton() {
		mButton = (Button) this.findViewById(R.id.animated_btn);
		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				parentButtonClicked(v);
			}
		});
	}

	protected void parentButtonClicked(View v) {
		animate();
	}

	private void animate() {
		mImageView = (ImageView) findViewById(R.id.animated_image);
		mImageView.setBackgroundResource(R.drawable.anim);
		AnimationDrawable frameAnimation = (AnimationDrawable) mImageView
				.getBackground();
		if (frameAnimation.isRunning()) {
			frameAnimation.stop();
			mButton.setText("start");
		} else {
			frameAnimation.start();
			mButton.setText("stop");
		}

	}

}
