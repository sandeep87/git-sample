package org.modev.android.bbk;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Sprite {
	int x, y, vx, vy;
	private Bitmap texture;
	
	public Sprite(Bitmap texture) {
		this.texture = texture;
	}
	
	public int getWidth()  { return texture.getWidth();  }
	public int getHeight() { return texture.getHeight(); }

	public void draw(Canvas c) {
		c.drawBitmap(texture, x, y, null);
	}
}
