package com.paradigmcreatives.soundpool;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class SoundpoolActivity extends Activity implements OnTouchListener {
	
	private SoundPool soundpool;
	private int soundId;
	boolean loaded = false;
	private TextView mTextView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mTextView = (TextView) findViewById(R.id.textview);
        mTextView.setOnTouchListener(this);
        
        //set the hardware buttons to control the music
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        
        //Load the sound
        soundpool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundpool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			
			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				loaded = true;
				
			}
		});
        soundId = soundpool.load(this, R.raw.police, 1);
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
			float actualvolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			float maxvolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			float volume = actualvolume / maxvolume;
			
			if(loaded) {
				soundpool.play(soundId, volume, volume, 1, 0, 1f);
				Log.e("Test", "Play Sound");
			}
		}
		return false;
		
	}
}