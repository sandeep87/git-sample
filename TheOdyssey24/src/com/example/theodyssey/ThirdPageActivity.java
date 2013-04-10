package com.example.theodyssey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class ThirdPageActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_three);
	}
	
	SimpleOnGestureListener listener = new SimpleOnGestureListener() {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,	float velocityY) {
			
			float sensitvity = 50;
			
			if ((e1.getX() - e2.getX()) > sensitvity) {
				
			} else if ((e2.getX() - e1.getX()) > sensitvity) {
				/*SwipeRight();*/
				SwipeLeft();
			}

			return true;
		}

	};

	GestureDetector geusture = new GestureDetector(listener);

/*	private void SwipeRight() {
        Intent intent = new Intent(ThirdPageActivity.this, SecondActivity.class);
        startActivity(intent);
	}*/

	private void SwipeLeft() {
		finish();
		Intent intent = new Intent(ThirdPageActivity.this, SecondPageActivity.class);
		startActivity(intent);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return geusture.onTouchEvent(event);
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		SwipeLeft();
		this.finish();
		/*Intent intentBack = new Intent(ThirdPageActivity.this , MainActivity.class);
		startActivity(intentBack);*/
	}
}
