package com.paradigmcreatives.mapview;

import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapViewActivity extends MapActivity {
	/** Called when the activity is first created. */
	private MapView mapView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);

		List<Overlay> mapOverlays = mapView.getOverlays();
		Drawable drawable = this.getResources().getDrawable(R.drawable.ic_launcher);
		ItemizedOverlay itemizedOverlay = new ItemizedOverlay(drawable, this);

		GeoPoint point = new GeoPoint(17385812, 78480667);
		OverlayItem overLayItem = new OverlayItem(point, "Welcome",
				"We are in Hyderabad,India");

		GeoPoint point1 = new GeoPoint(17705339,85584095);
		OverlayItem overLayItem1 = new OverlayItem(point1, "Welcome",
				"We are in Vizag,India");

		itemizedOverlay.addOverlay(overLayItem);
		itemizedOverlay.addOverlay(overLayItem1);

		mapOverlays.add(itemizedOverlay);
	}

	public boolean isRouteDisplayed() {
		return false;

	}
}