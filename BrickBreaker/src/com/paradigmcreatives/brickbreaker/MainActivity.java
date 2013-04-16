package com.paradigmcreatives.brickbreaker;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity {
	
	private Button btn_startgame;
	private Activity activity;
	private GameView mGameView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		activity = this;
		setContentView(R.layout.activity_main);
		
		btn_startgame = (Button) findViewById(R.id.start_game);
		btn_startgame.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mGameView = new GameView(activity);
				setContentView(mGameView);
				mGameView.mThread.doStart();
				
			}
		});
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGameView.mThread.onTouch(event);
		return true;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

}
