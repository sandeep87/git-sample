package com.example.samplelocation;

import android.app.Activity;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

	LocationManager mgr;
	String provider;
	Location mlocation;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mgr = (LocationManager) getSystemService(LOCATION_SERVICE);
        provider = LocationManager.NETWORK_PROVIDER;
        mlocation = mgr.getLastKnownLocation(provider);
        if(mlocation != null)  {
        	
        	System.out.println("latitude:"+mlocation.getLatitude());
        	System.out.println("latitude:"+mlocation.getLongitude());
        }
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
