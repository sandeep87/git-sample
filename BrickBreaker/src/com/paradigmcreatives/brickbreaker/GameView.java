package com.paradigmcreatives.brickbreaker;

import android.content.Context;
import android.graphics.drawable.Drawable.Callback;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Callback, android.view.SurfaceHolder.Callback {
Context mContext;
GameThread mGameThread;
	public GameView(Context context) {
		super(context);
		this.mContext = context;
		getHolder().addCallback(this);
		mGameThread = new GameThread(getHolder(), mContext, new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
			}
		});
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d(this.getClass().toString(), "Surface Changed");
		
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(this.getClass().toString(), "Surface Created");
		mGameThread.running = true;
		mGameThread.start();
		
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(this.getClass().toString(), "Surface Destroyed");
		boolean retry = true;
		mGameThread.running = false;
		while(retry) {
			mGameThread.join();
			retry = false;
		}
		
	}

}
