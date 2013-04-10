	package org.modev.android.bbk;

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
	
	Button btn_startGame;
	Activity activity;
	
	GameView mGameView;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        activity = this;
        
        setContentView(R.layout.main);
        
        btn_startGame = (Button) findViewById(R.id.btnStartGame);
        btn_startGame.setOnClickListener(new OnClickListener() {
			
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
    	try {
    		mGameView.mThread.onTouch(event);
    	} catch(Exception e) {}
    	
    	return true;
    }
    
    @Override 
	public void onConfigurationChanged(Configuration newConfig) { 
	  // ignore orientation/keyboard change 
	  super.onConfigurationChanged(newConfig); 
	} 
}
