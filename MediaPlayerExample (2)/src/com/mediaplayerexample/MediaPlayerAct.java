package com.mediaplayerexample;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.SeekBar;

public class MediaPlayerAct extends Activity implements OnClickListener {
	private SeekBar mBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media_player);
		findViewById(R.id.playResouce).setOnClickListener(this);
		findViewById(R.id.playLocal).setOnClickListener(this);
		findViewById(R.id.playOnline).setOnClickListener(this);
		findViewById(R.id.pause).setOnClickListener(this);
		findViewById(R.id.button3).setOnClickListener(this);
		findViewById(R.id.stop).setOnClickListener(this);
		mBar = (SeekBar) findViewById(R.id.progress);
	}

	MediaPlayer mediaPlayer;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_media_player, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.playResouce:
			PlayResource();
			break;
		case R.id.playLocal:
			PlayLocalUrl();
			break;
		case R.id.playOnline:
			PlayOnlineUrl();
			break;
		case R.id.pause:
			if (mediaPlayer != null) {
				pause = true;
				mediaPlayer.pause();
			}
			break;
		case R.id.button3:
			if (mediaPlayer != null) {
				pause = false;
				mediaPlayer.start();
			}
			break;
		case R.id.stop:
			if (mediaPlayer != null) {
				mediaPlayer.stop();
				mediaPlayer = null;
				timer.cancel();
				task.cancel();
			}
			break;

		default:
			break;
		}
	}

	private void PlayResource() {
		mediaPlayer = MediaPlayer.create(this, R.raw.sor);
		mediaPlayer.start();
		mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				// Now dismis progress dialog, Media palyer will start playing
				mp.start();
				int duration = mp.getDuration();
				int period = duration / 1000;
				task = new TimerTask() {

					@Override
					public void run() {
						mBar.post(new Runnable() {
							@Override
							public void run() {
								if (!pause) {
									progresss++;
									mBar.setProgress(progresss);
								}
							}
						});
					}
				};
				timer = new Timer();
				timer.schedule(task, 0, period * 10);

			}
		});
	}

	private boolean pause = false;
	int progresss = 0;
	private TimerTask task;
	private Timer timer = null;

	/**
	 * Give the Path of your file from sdcard.
	 */
	private void PlayLocalUrl() {
		String url = Environment.getExternalStorageDirectory()
				+ "/yourfile.mp3"; // your URL here
		MediaPlayer mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mediaPlayer.setDataSource(url);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mediaPlayer.prepareAsync();
		// You can show progress dialog here untill it prepared to play
		mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				// Now dismis progress dialog, Media palyer will start playing
				mp.start();
			}
		});
		mediaPlayer.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// dissmiss progress bar here. It will come here when
				// MediaPlayer
				// is not able to play file. You can show error message to user
				return false;
			}
		});
	}

	/**
	 * Give the online url
	 */
	private void PlayOnlineUrl() {
		String url = "http://www.gaana.mp3"; // your URL here
		MediaPlayer mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mediaPlayer.setDataSource(url);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mediaPlayer.prepareAsync();
		// You can show progress dialog here untill it prepared to play
		mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				// Now dismis progress dialog, Media palyer will start playing
				mp.start();
			}
		});
		mediaPlayer.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				// dissmiss progress bar here. It will come here when
				// MediaPlayer
				// is not able to play file. You can show error message to user
				return false;
			}
		});
	}

}
