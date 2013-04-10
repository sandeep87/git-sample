package com.paradigmcreatives.mapoverlay;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class CircleOverlay extends Overlay{

	private double mdestLat;
	private double mdestLng;
	private Paint circlePainter;
	private GeoPoint geoDestPoint;
	private Point screenDestPoint;
	private int mMeters;
	private Projection projection;

	public CircleOverlay(int meters, double destLat, double destLng) {
		mMeters=meters;
		mdestLat = destLat;
		mdestLng = destLng;
		
		System.out.println(" before lat : "+mdestLat);
		System.out.println(" before lng : "+mdestLng);
		
		circlePainter = getBorderPaint();
		//circlePainter.setAntiAlias(true);
		//circlePainter.setStrokeWidth(2.0f);
		//circlePainter.setColor(0xff6666ff);
		//circlePainter.setColor(0xff0000);

		//circlePainter.setStyle(Style.FILL_AND_STROKE);
		//circlePainter.setAlpha(70);
		
		geoDestPoint = new GeoPoint((int)(destLat * 1e6), (int)(destLng * 1e6));
		
		System.out.println(" After lat : "+geoDestPoint.getLatitudeE6());
		System.out.println(" After lng : "+geoDestPoint.getLongitudeE6());
		
		screenDestPoint = new Point();
	}

	//draw boarder of circle(outer circle)
	public Paint getBorderPaint() {
	    if (circlePainter == null) {
	    	circlePainter = new Paint();
	    	System.out.println(" setting paint OBJ");
	    	circlePainter.setARGB(150, 000, 000, 000);     //black
	    	circlePainter.setAntiAlias(true);
	    	circlePainter.setStyle(Style.STROKE);
	    	circlePainter.setStrokeWidth(2);
	    }
	    return circlePainter;
	}
	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		// TODO Auto-generated method stub
		
		projection = mapView.getProjection();
		
		// Project the gps coordinate to screen coordinate
		projection.toPixels(geoDestPoint, screenDestPoint);
		
		 int radius = (int) (mapView.getProjection().metersToEquatorPixels(mMeters) * (1/ Math.cos(Math.toRadians(mdestLat/1000000))));
		 canvas.drawCircle(screenDestPoint.x, screenDestPoint.y, radius, circlePainter);
		super.draw(canvas, mapView, shadow);
	}
	
	
	
}
