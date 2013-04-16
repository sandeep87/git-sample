package com.paradigmcreatives.viewswitcher;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ViewSwitcher;

public class ViewSwitcherActivity extends Activity {
    /** Called when the activity is first created. */
	private ViewSwitcher mViewSwitcher;
	private Button mPreviousbtn;
	private Button mNextbtn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mViewSwitcher = (ViewSwitcher) findViewById(R.id.viewswitcher);
        mPreviousbtn = (Button) findViewById(R.id.previous_btn);
        mPreviousbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AnimationUtils();
				mViewSwitcher.setAnimation(AnimationUtils.makeInAnimation(getApplicationContext(), true));
				mViewSwitcher.showNext();
				
			}
		});
        
        mNextbtn = (Button) findViewById(R.id.next_btn);
        mNextbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new AnimationUtils();
				mViewSwitcher.setAnimation(AnimationUtils.makeInAnimation(getApplicationContext(), true));
				mViewSwitcher.showNext();
				
			}
		});
    }
}