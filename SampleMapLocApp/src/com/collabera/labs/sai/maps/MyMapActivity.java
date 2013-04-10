package com.collabera.labs.sai.maps;


import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MyMapActivity extends MapActivity implements LocationListener {
	/** Called when the activity is first created. */
	
	TextView		myLoc			= null;
	
	MapView			myMapView		= null;
	
	MapController	myMC			= null;
	
	GeoPoint		geoPoint		= null;
	
	double			latitude		= 12.937875, longitude = 77.622313;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// Creating TextBox displaying Latitude, Longitude
		myLoc = (TextView) findViewById(R.id.id1);
		String currentLocation = "My location is: Lat: " + latitude + " Lng: " + longitude;
		myLoc.setText(currentLocation);
		
		// Creating and initializing Map
		myMapView = (MapView) findViewById(R.id.myGMap);
		geoPoint = new GeoPoint((int) (latitude * 1000000), (int) (longitude * 1000000));
		myMapView.setSatellite(false);
		
		//Getting the MapController to fine tune settings
		myMC = myMapView.getController();
		myMC.setCenter(geoPoint);
		myMC.setZoom(15);
		
		// Add a location mark
		MyLocationOverlay myLocationOverlay = new MyLocationOverlay();
		List<Overlay> list = myMapView.getOverlays();
		list.add(myLocationOverlay);
		
		// Adding zoom controls to Map
		myMapView.setBuiltInZoomControls(true);
        myMapView.displayZoomControls(true);
				
		// Getting locationManager and reflecting changes over map if distance travel by
		// user is greater than 500m from current location.
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f, this);
	}
	
	
	public void onLocationChanged(Location location) {
		if (location != null) {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			String currentLocation = "The location is changed to Lat: " + lat + " Lng: " + lng;
			myLoc.setText(currentLocation);
			geoPoint = new GeoPoint((int) lat * 1000000, (int) lng * 1000000);
			myMC.animateTo(geoPoint);
		}
	}
	
	public void onProviderDisabled(String provider) {
		// required for interface, not used
	}
	
	public void onProviderEnabled(String provider) {
		// required for interface, not used
	}
	
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// required for interface, not used
	}
	
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_I) {
			myMapView.getController().setZoom(myMapView.getZoomLevel() + 1);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_O) {
			myMapView.getController().setZoom(myMapView.getZoomLevel() - 1);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_S) {
			myMapView.setSatellite(true);
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_M) {
			myMapView.setSatellite(false);
			return true;
		}
		return false;
	}
	
	/* Class overload draw method which actually plot a marker,text etc. on Map */
	protected class MyLocationOverlay extends com.google.android.maps.Overlay {
		
		@Override
		public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
			Paint paint = new Paint();
			
			super.draw(canvas, mapView, shadow);
			// Converts lat/lng-Point to OUR coordinates on the screen.
			Point myScreenCoords = new Point();
			mapView.getProjection().toPixels(geoPoint, myScreenCoords);
			
			paint.setStrokeWidth(1);
			paint.setARGB(255, 255, 255, 255);
			paint.setStyle(Paint.Style.STROKE);
			
			Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
			
			canvas.drawBitmap(bmp, myScreenCoords.x, myScreenCoords.y, paint);
			canvas.drawText("I am here...", myScreenCoords.x, myScreenCoords.y, paint);
			return true;
		}
	}
}