package com.paradigmcreatives.lbsgeofinder;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class LbsGeoFinderActivity extends Activity {
	/** Called when the activity is first created. */
	private LocationManager mLocationManager;
	private String provider;
	private Location recLoc;
	private TextView mLatitude;
	private TextView mLongitude;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		provider = LocationManager.GPS_PROVIDER;
		recLoc = mLocationManager.getLastKnownLocation(provider);

		if (recLoc != null) {
			mLatitude = (TextView) findViewById(R.id.latitude);
			mLatitude.setText("Latitiude:"
					+ Double.toString(recLoc.getLatitude()));
			mLongitude = (TextView) findViewById(R.id.longitude);
			mLongitude.setText("longitude:"
					+ Double.toString(recLoc.getLongitude()));
		}

	}
}