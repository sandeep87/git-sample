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

public class PinsOverlay extends Overlay {

	private Context myPinsOverlayContext;
	private GeoPoint myCurrentGeoPoints;
	private GeoPoint myDestinationGeopoints;
	private Bitmap bitmap;
	private Point myCurrentLocScreenPoints;
	private Point myDestinationLocScreenPoints;
	
	public PinsOverlay(Context mContext, GeoPoint currentGeoPoints, GeoPoint destGeoPoints) {
		myPinsOverlayContext = mContext;
		myCurrentGeoPoints = currentGeoPoints;
		myDestinationGeopoints = destGeoPoints;
		
		myCurrentLocScreenPoints = new Point();
		myDestinationLocScreenPoints = new Point();
		
		bitmap =BitmapFactory.decodeResource(myPinsOverlayContext.getResources(), R.drawable.pin);
	}
	
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow,long when) {
		
		mapView.getProjection().toPixels(myCurrentGeoPoints, myCurrentLocScreenPoints);
		mapView.getProjection().toPixels(myDestinationGeopoints, myDestinationLocScreenPoints);
		canvas.drawBitmap(bitmap, myCurrentLocScreenPoints.x-25 , myCurrentLocScreenPoints.y-42, null);
		canvas.drawBitmap(bitmap, myDestinationLocScreenPoints.x-25 , myDestinationLocScreenPoints.y-42, null);
		
		super.draw(canvas, mapView, shadow, when);
		return false;
	}

	protected boolean isRouteDisplayed() {
		return false;
	}
	
}
