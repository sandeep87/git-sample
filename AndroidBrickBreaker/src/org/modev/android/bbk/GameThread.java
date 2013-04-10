package org.modev.android.bbk;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
	
	private SurfaceHolder mHolder = null;
	
	private long mLastTime = 0;
	
	private int width, height, fps, ifps;
	
	Sprite ball;
	Sprite bar;
	
	private Paint fpaint = new Paint();
	
	private Paint paint = new Paint();
	private Rect rect = new Rect(0, 0, width, height);
	
	boolean running = false;
	
	public GameThread(SurfaceHolder surfaceholder, Context context, Handler handler) {
		this.mHolder = surfaceholder;
		
		ball = new Sprite(BitmapFactory.decodeResource(context.getResources(), R.drawable.ball));
		
		ball.x = 100;
		ball.y = 100;
		
		ball.vx = 12;
		ball.vy = 8;
		
		bar = new Sprite(BitmapFactory.decodeResource(context.getResources(), R.drawable.bar));
		
		bar.x = 100;
		
		ifps = fps = 0;
		
		fpaint.setColor(Color.WHITE);
		paint.setColor(Color.BLACK);
	}
	
	public void doStart() {
		synchronized(mHolder) {
			mLastTime = System.currentTimeMillis() + 100;
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(running) {
			Canvas c = null;	
			try {
				c = mHolder.lockCanvas();
				if(width == 0) {
					width  = c.getWidth();
					height = c.getHeight();
					
					bar.y = height - 100;
				}
				
				synchronized(mHolder) {
					long now = System.currentTimeMillis();
					
					update();
					draw(c);
					ifps++;
					
					if(now > (mLastTime + 1000)) {
						mLastTime = now;
						fps = ifps;
						ifps = 0;
					}
				}
			}
			finally {
				if(c != null) {
					mHolder.unlockCanvasAndPost(c);
				}
			}
		}
	}
	
	public void onTouch(MotionEvent event) {
		if(bar != null)
			bar.x = (int) (event.getX() - bar.getWidth()/2);
	}

	private void update() {
		ball.x += ball.vx;
		ball.y -= ball.vy;
		
		if(ball.x < 0 || ball.x + ball.getWidth() > width)
			ball.vx = -ball.vx;
		
		if(ball.y < 0 || ball.y + ball.getHeight() > height)
			ball.vy = -ball.vy;
		
		if(ball.y + ball.getHeight() >= bar.y) {
			if(ball.x + ball.getWidth() > bar.x && ball.x < bar.x + bar.getWidth()) {
				ball.vy = -ball.vy;
			}
		}
	}

	private void draw(Canvas c) {
		c.drawColor(Color.BLACK);
		c.drawRect(rect, paint);
		
		ball.draw(c);
		bar.draw(c);
		
		// draw FPS
		c.drawText("FPS:" + fps, width/2, 10, fpaint);
	}
}
