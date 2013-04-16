package com.paradigmcreatives.odyssey;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Odyssey extends Activity {

	private Button button;
	private TextView text;
	boolean value = true;
	LinearLayout linearLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_odyssey);
		button = (Button) findViewById(R.id.imagebutton);
		text = (TextView) findViewById(R.id.textview);
		linearLayout = (LinearLayout) findViewById(R.id.linear_view);

		final Animation animation_min = AnimationUtils.loadAnimation(this,
				R.animator.odyssey_anim);
		final Animation animation_max = AnimationUtils.loadAnimation(this,
				R.animator.odyssey1_anim);
		final Animation anim_clk = AnimationUtils.loadAnimation(this,
				R.animator.rotate_anim);

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
				linearLayout.startAnimation(animation_min);

			}
		}, 2000);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (value) {

					button.startAnimation(anim_clk);
					button.setBackgroundResource(R.drawable.maximize_icon);
					value = false;
					linearLayout.startAnimation(animation_max);

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
					linearLayout.startAnimation(animation_min);
				}

			}

		});

	}

}
