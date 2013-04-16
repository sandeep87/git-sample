package com.paradigmcreatives.brickbreaker;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
	private SurfaceHolder mHolder = null;
	private long mLastTime = 0;
	private int width, height, fps, ifps;
	Sprite ball;
	Sprite bar;
	private Paint fPaint = new Paint();
	private Paint paint = new Paint();
	private Rect rect = new Rect(0, 0, width, height);
	boolean running = false;
	

	public GameThread(SurfaceHolder holder, Context mContext, Handler handler) {
		this.mHolder = holder;
		ball = new Sprite(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ball));
		ball.x = 100;
		ball.y = 100;
		ball.vx = 12;
		ball.vy = 8;
		
		bar = new Sprite(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.bar));
		
	}

}
