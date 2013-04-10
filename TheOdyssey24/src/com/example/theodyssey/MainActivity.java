package com.example.theodyssey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView text;
	Button button;
	boolean value = true;
	LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		text = (TextView) findViewById(R.id.text);
		button = (Button) findViewById(R.id.button);
		layout = (LinearLayout) findViewById(R.id.linear_id);

		final Animation animation_min = AnimationUtils.loadAnimation(this, R.anim.scale_min);
		final Animation animation_max = AnimationUtils.loadAnimation(this, R.anim.scale_max);
		final Animation anim_clk = AnimationUtils.loadAnimation(this, R.anim.btn_rotate);

		animation_min.reset();
		animation_max.reset();
		anim_clk.reset();

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {

				button.setVisibility(View.VISIBLE);
				button.startAnimation(anim_clk);
                
				button.setBackgroundResource(R.drawable.minimize_icon);
           
				text.setVisibility(View.VISIBLE);
				layout.startAnimation(animation_min);

			}
		}, 2000);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (value) {

					button.startAnimation(anim_clk);
					button.setBackgroundResource(R.drawable.maximize_icon);
					value = false;
					layout.startAnimation(animation_max);

					AnimationListener listener = new AnimationListener() {

						@Override
						public void onAnimationEnd(Animation animation) {
							text.setVisibility(View.INVISIBLE);
						}

						@Override
						public void onAnimationRepeat(Animation animation) {

						}

						@Override
						public void onAnimationStart(Animation animation) {

						}
					};
					animation_max.setAnimationListener(listener);

				} else {
					button.startAnimation(anim_clk);
				    button.setBackgroundResource(R.drawable.minimize_icon);
					value = true;
					text.setVisibility(View.VISIBLE);
					layout.startAnimation(animation_min);
				}

			}
		});

	}
	
	SimpleOnGestureListener listener = new SimpleOnGestureListener() {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,	float velocityY) {
			
			float sensitvity = 50;
			
			if ((e1.getX() - e2.getX()) > sensitvity) {
				/*SwipeLeft();*/
				SwipeRight();
			} else if ((e2.getX() - e1.getX()) > sensitvity) {
				
			}

			return true;
		}

	};

	GestureDetector geusture = new GestureDetector(listener);

	private void SwipeRight() {
		finish();
        Intent intent = new Intent(MainActivity.this, SecondPageActivity.class);
        startActivity(intent);
	}

	/*private void SwipeLeft() {
		Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
		startActivity(intent);
	}*/
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return geusture.onTouchEvent(event);
	}
	
	

}
