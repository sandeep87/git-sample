package com.paradigmcreaitves.viewanimation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {
	
	private Button button;
	ImageView animationTarget;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		 animationTarget = (ImageView)findViewById(R.id.image_view);
		
		button = (Button) findViewById(R.id.testButton);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Animation newAnimation = new RotateAnimation(0, 360, 50, 50);
				newAnimation.setDuration(700);
				animationTarget.startAnimation(newAnimation);
				
				
			}
		});
		

		

	}

}
