package com.paradigmcreatives.drawimage;


import android.R.color;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable.Callback;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawCanvas extends SurfaceView implements Callback {

	private CanvasThread canvasThread;

	public DrawCanvas(Context context) {
		super(context);

	}

	public DrawCanvas(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.getHolder()
				.addCallback((android.view.SurfaceHolder.Callback) this);
		this.canvasThread = new CanvasThread(getHolder());
		this.setFocusable(true);
	}

	public DrawCanvas(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub

	}

	public void startDrawImage() {
		canvasThread.setRunning(true);
		canvasThread.start();

	}

	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

	}

	public void surfaceDestroyed(SurfaceHolder arg0) {
		boolean retry = true;
		canvasThread.setRunning(false);

		while (retry) {
			try {
				canvasThread.join();
				retry = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.sq);
		canvas.drawColor(color.black);
		canvas.drawBitmap(bitmap, 0, 0, null);
	}

	private class CanvasThread extends Thread {

		private SurfaceHolder surfaceHolder;
		private boolean isRun = false;

		public CanvasThread(SurfaceHolder holder) {
			this.surfaceHolder = holder;
		}

		public void setRunning(boolean b) {
			this.isRun = b;

		}

		public void run() {
			Canvas c;
			while (isRun) {
				c = null;

				try {
					c = this.surfaceHolder.lockCanvas(null);
					synchronized (this.surfaceHolder) {
						DrawCanvas.this.onDraw(c);

					}
				} finally {
					surfaceHolder.unlockCanvasAndPost(c);
				}
			}
		}

	}
}
