package org.modev.android.bbk;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class GameView extends SurfaceView implements Callback {

	Context mContext;
	GameThread mThread;
	
	public GameView(Context context) {
		super(context);
		this.mContext = context;
		
		getHolder().addCallback(this);
		
		mThread = new GameThread(getHolder(), mContext, new Handler() {
			@Override
            public void handleMessage(Message m) {
            	// Use for pushing back messages.
            }
		});
		
		setFocusable(true);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
		Log.d(this.getClass().toString(), "in SurfaceChanged()");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(this.getClass().toString(), "in SurfaceCreated()");
		mThread.running = true;
		mThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(this.getClass().toString(), "in SurfaceDestroyed()");
		
		boolean retry = true;
        mThread.running = false;
        while (retry) {
            try {
                mThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
	}

}
