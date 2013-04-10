package com.paradigmcreatives.mapoverlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.paradigmcreatives.activity.R;

public class CurrentLocOverlay extends Overlay{
	private Context myOverlayContext;
	private GeoPoint myCurrentGeoPoints;
	private Bitmap bitmap;
	private Point myCurrentLocScreenPoints;

	public CurrentLocOverlay(Context mContext, GeoPoint currentGeoPoints) {
		myOverlayContext = mContext;
		myCurrentGeoPoints = currentGeoPoints;
		myCurrentLocScreenPoints = new Point();
		bitmap =BitmapFactory.decodeResource(myOverlayContext.getResources(), R.drawable.pin);
	}

	public boolean draw(Canvas canvas, MapView mapView, boolean shadow,long when) {
		mapView.getProjection().toPixels(myCurrentGeoPoints, myCurrentLocScreenPoints);
		canvas.drawBitmap(bitmap, myCurrentLocScreenPoints.x-25 , myCurrentLocScreenPoints.y-42, null);
		super.draw(canvas, mapView, shadow, when);
		return false;
	}

	protected boolean isRouteDisplayed() {
		return false;
	}
}
