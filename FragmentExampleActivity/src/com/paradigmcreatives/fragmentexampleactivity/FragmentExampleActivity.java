package com.paradigmcreatives.fragmentexampleactivity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class FragmentExampleActivity extends Activity {

	int mStackLevel = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment_example);
		
		Button button = (Button) findViewById(R.id.new_fragment);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addFragmentStack();
			}
		});
		if (savedInstanceState == null) {
			Fragment newFragment = CountingFragment.newInstance(mStackLevel);
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.add(R.id.fragment1, newFragment).commit();
		} else {
			mStackLevel = savedInstanceState.getInt("level");
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("level", mStackLevel);
	}
	protected void addFragmentStack() {
		 mStackLevel++;
	        Fragment newFragment = CountingFragment.newInstance(mStackLevel);
	        FragmentTransaction ft = getFragmentManager().beginTransaction();
	        ft.setCustomAnimations(R.animator.fragment_slide_left_enter,
	                R.animator.fragment_slide_left_exit,
	                R.animator.fragment_slide_right_enter,
	                R.animator.fragment_slide_right_exit);
	        ft.replace(R.id.simple_fragment, newFragment);
	        ft.addToBackStack(null);
	        ft.commit();
	}

}
