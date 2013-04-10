package com.paradigmcreatives.activity;

import java.io.IOException;

import java.util.Timer;
import java.util.TimerTask;

import com.paradigmcreatives.crashreport.ParadigmExceptionHandler;
import com.paradigmcreatives.database.Config;
import com.paradigmcreatives.service.AlertService;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class AlarmActivity extends Activity{

	private Vibrator vib;
	private Context mContext;
	private MediaPlayer mMediaPlayer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = AlarmActivity.this;
		
		PendingIntent intent1 = PendingIntent.getActivity(getApplication().getBaseContext(), 0, new Intent(getIntent()), getIntent().getFlags());
		Thread.setDefaultUncaughtExceptionHandler(new ParadigmExceptionHandler(this, intent1));
		
		Intent intent = getIntent();
		double alertTime = intent.getIntExtra("alertTimeTag", 0);
		
		if (alertTime == 0) {
			alertTime = AlertService.alertTime;
		}
		
		setContentView(R.layout.main);
				
		ImageView view = (ImageView)findViewById(R.id.alarm_anim_view);
		view.setBackgroundResource(R.anim.alarm_animation);
		Button stopButton = (Button)findViewById(R.id.stop_alarm);
		TextView textView = (TextView)findViewById(R.id.destinationstring);
	
		Config mConfig = Config.Instance();
		mConfig.dropCurrentLocTable();
		mConfig.dropStatusTable();
		mConfig.dropGeoLocationTable();
		
		if (TabViewActivity.mCurrentLocationNavigation != null) {
			TabViewActivity.mCurrentLocationNavigation.disableCompass();
			TabViewActivity.mCurrentLocationNavigation.disableMyLocation();
		}else{
			System.out.println(" COMPASS disable unsuccuessful");
		}
		
		
		String s = " Your Destination is approaching in "+ Double.toString(alertTime)+ " Mins.";
		textView.setText(s);
		
		MyAnimationRoutine mar = new MyAnimationRoutine();
		final Timer t = new Timer(false);
		t.schedule(mar, 500);
		
//		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//		System.out.println(alert.);
		Uri alert = Uri.parse(MapViewActivity.alarmTone);
		 mMediaPlayer = new MediaPlayer();
		try {
			mMediaPlayer.setDataSource(this, alert);
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		final AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
		mMediaPlayer.setLooping(true);
		
		try {
			mMediaPlayer.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		mMediaPlayer.start();
		  vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
   	      vib.vibrate(1000);
   	 
		}
		
		stopButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 mMediaPlayer.stop();
				 vib.cancel();
				 t.cancel();
				 com.paradigmcreatives.database.Config.Instance().dropCurrentLocTable();
				 Intent serviceIntent = new Intent(mContext, AlertService.class);
					if (serviceIntent != null) {
						stopService(serviceIntent);
					}
				 Intent intent = new Intent(AlarmActivity.this, MainActivity.class);
				 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				 startActivity(intent);
			}
		});
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (mMediaPlayer != null) {
			mMediaPlayer.release();
		}
		super.onDestroy();
	}


	class MyAnimationRoutine extends TimerTask
	{
	MyAnimationRoutine()
	{
		
	}

	public void run()
	{
	ImageView img = (ImageView)findViewById(R.id.alarm_anim_view);
	// Get the background, which has been compiled to an AnimationDrawable object.
	AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();

	// Start the animation (looped playback by default).
	frameAnimation.start();
	}
	}
}
