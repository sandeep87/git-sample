package com.paradigmcreatives.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.paradigmcreatives.activity.AlarmActivity.MyAnimationRoutine;
import com.paradigmcreatives.crashreport.ParadigmExceptionHandler;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Class responsible for showing splash screen
 * @author Robin
 */
public class StationAlert extends Activity {
	
	protected boolean _active = true;
	protected int _splashTime = 2500;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		PendingIntent intent = PendingIntent.getActivity(getApplication().getBaseContext(), 0, new Intent(getIntent()), getIntent().getFlags());
		Thread.setDefaultUncaughtExceptionHandler(new ParadigmExceptionHandler(this, intent));
		
		setContentView(R.layout.splash);
		
		//setContentView(R.layout.main);
		
		ImageView view = (ImageView)findViewById(R.id.splash_animation);
		//TextView textView = (TextView)findViewById(R.id.destinationstring);
		//Button stopButton = (Button)findViewById(R.id.stop_alarm);
		
		//textView.setVisibility(View.GONE);
		//stopButton.setVisibility(View.GONE);
		
		view.setBackgroundResource(R.anim.alarm_animation);

		//Setting Exception handler for uncaught exceptions
		//		Thread.setDefaultUncaughtExceptionHandler(new PercipiExceptionHandler());

		//Getting the activity manager
		//ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//		PercipiNavigator.setActivityManager(activityManager);


		// thread for displaying the SplashScreen
		Thread splashTread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					while(_active && (waited < _splashTime)) {
						sleep(100);
						if(_active) {
							waited += 100;
							MyAnimationRoutine mar = new MyAnimationRoutine();
							final Timer t = new Timer(false);
							t.schedule(mar, 100);
							 Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
							 vibrator.vibrate(100);
							
						}
					}
				} catch(InterruptedException e) {
					// do nothing
				} finally {
					finish();
					Intent i = new Intent();
					i.setClassName("com.paradigmcreatives.activity", "com.paradigmcreatives.activity.MainActivity");
					//                    Intent i = new Intent(Percipi.this, FBReader.class);
					//            		i.putExtra("BookPath", "/sdcard/Books/sherlock.epub");                    
					startActivity(i);
					StationAlert.this.finish();
				}
			}
		};
		splashTread.start();
		
	}
	
	
	class MyAnimationRoutine extends TimerTask
	{
	MyAnimationRoutine()
	{
	}

	public void run()
	{
	ImageView img = (ImageView)findViewById(R.id.splash_animation);
	// Get the background, which has been compiled to an AnimationDrawable object.
	AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();

	// Start the animation (looped playback by default).
	frameAnimation.start();
	}
	}
}