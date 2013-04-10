package com.paradigmcreatives.mapoverlay;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.paradigmcreatives.activity.R;

public class RouteOverlay extends Overlay{
	
	

	private ArrayList<GeoPoint> mGeoPoints;
	private Context mContext;
	private Point screenSrtPts = new Point();
	private Point screenEndPts = new Point();
	private GeoPoint srtLoc;
	private GeoPoint endLoc;
	private Bitmap bitmap ;
	private Paint paint;
	private Point point;
	private GeoPoint mCurrentGeoPoint;
	/*public RouteOverlay(GeoPoint geoPoint, GeoPoint geoPoint2, int clr){
		mPoint1=geoPoint;
		mPoint2= geoPoint2;
		mColor = clr;
		mStrokeWidth = 5;
	}*/
	
	 /*  public RouteOverlay(GeoPoint geoPoint, Context context, boolean routeMap){
		   mRouteMap = routeMap;
		   mContext = context;
		   mCurrentGeoPoint = geoPoint;
		   bitmap =BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pin);
	   }*/
	   
	
/*	public RouteOverlay(Context context, MapView mapView) {
		super(context, mapView);
		// TODO Auto-generated constructor stub
	}*/
	
	public RouteOverlay(ArrayList<GeoPoint>  geoPoints,Context context, MapView mapView){
		//super(context, mapView);
		mGeoPoints = geoPoints;
		mContext = context;
		initilizationOfObjects(); 
	}
	
	private void initilizationOfObjects() {
		// initilization of the all the object which are required to draw a direction on Map.
		 	srtLoc = mGeoPoints.get(0);
		 	endLoc = mGeoPoints.get(mGeoPoints.size()-1);
		 	
		 	//System.out.println(" srtloc : "+srtLoc.getLatitudeE6()+ "Lng :"+srtLoc.getLongitudeE6());
		 	//System.out.println(" endLoc : "+endLoc.getLatitudeE6()+ "Lng :"+endLoc.getLongitudeE6());
		 	
		 	bitmap =BitmapFactory.decodeResource(mContext.getResources(), R.drawable.pin);
		 	paint = new Paint();
			paint.setColor(Color.RED);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeJoin(Paint.Join.ROUND);
			paint.setStrokeCap(Paint.Cap.ROUND);
			//paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(3);
			point = new Point();
			
			/* Paint   mPaint = new Paint();
		        mPaint.setDither(true);
		        mPaint.setColor(Color.RED);
		        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		        mPaint.setStrokeJoin(Paint.Join.ROUND);
		        mPaint.setStrokeCap(Paint.Cap.ROUND);
		        mPaint.setStrokeWidth(2);
		        point = new Point();*/

	}

	/*@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		Projection projection = mapView.getProjection();
		Paint paint = new Paint();
		Point point = new Point();
		Point point2 = new Point();

		paint.setColor(mColor);
		paint.setStrokeWidth(mStrokeWidth);
		paint.setAlpha(120);
		
		projection.toPixels(mPoint1, point);
		projection.toPixels(mPoint2, point2);
		
		canvas.drawLine(point.x, point.y, point2.x, point2.y, paint);
		super.draw(canvas, mapView, shadow);
	}*/
	
	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow,long when) {
	   super.draw(canvas, mapView, shadow, when);
	   //i am trying to draw pin in current location
		   mapView.getProjection().toPixels(srtLoc, screenSrtPts);
	       mapView.getProjection().toPixels(endLoc, screenEndPts);
	       drawPath(mapView, canvas);
	       canvas.drawBitmap(bitmap, screenSrtPts.x-25 , screenSrtPts.y-42, null);
	       canvas.drawBitmap(bitmap, screenEndPts.x-25 , screenEndPts.y-42, null);
	   return false;
	}
	
	public void drawPath(MapView mv, Canvas canvas) {
		int x1 = -1, y1 = -1, x2 = -1, y2 = -1;
		for (int i = 0; i < mGeoPoints.size(); i++) {
			mv.getProjection().toPixels(mGeoPoints.get(i), point);
			x2 = point.x;
			y2 = point.y;
			if (i > 0) {
				canvas.drawLine(x1, y1, x2, y2, paint);
				//canvas.drawLine(x1,y1,x2,y2, paint);
				
			}
			  
			x1 = x2;
			y1 = y2;
		}
		
	}
	
	protected boolean isRouteDisplayed() {
		return true;
	}
	
		
}
