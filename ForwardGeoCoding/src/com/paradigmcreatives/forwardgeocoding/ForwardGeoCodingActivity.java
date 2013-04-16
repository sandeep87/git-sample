package com.paradigmcreatives.forwardgeocoding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ForwardGeoCodingActivity extends Activity
{
	LocationManager locManager;
	LocationListener mlocListener;
	Geocoder fgeocoder;
	
	EditText d_from,d_to;
	TextView total_dist;
	
	double lat1;
	double lng1;
	double lat2;
	double lng2;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		
		d_from= (EditText) findViewById(R.id.from_edt);
		d_to = (EditText) findViewById(R.id.to_edt);
		total_dist = (TextView) findViewById(R.id.klo_met);
		
		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
     	mlocListener = new MyLocListener();
     	
     	locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,mlocListener);
     	
        fgeocoder = new Geocoder(this,Locale.getDefault());
  
        
    
	}	
	
	@Override 
	protected void onDestroy() 
    {
		locManager.removeUpdates(mlocListener);
		super.onDestroy();
	}
			

	public void calDistance(View v)	
	{
		
	
    try
    {
		String  from_add = d_from.getText().toString();        
        List<Address> address1 = fgeocoder.getFromLocationName(from_add,3);
        
        if(address1 != null && address1.size() > 0)
        {
        	 lat1 = address1.get(0).getLatitude();
        	 lng1 = address1.get(0).getLongitude();
        }
     }
    catch (IOException e1) 
    {
		e1.printStackTrace();
	} 
    
    try
    {
    	String  to_add = d_to.getText().toString();    	
    	List<Address> address2 = fgeocoder.getFromLocationName(to_add,3);
    	
    	if(address2 != null && address2.size() > 0)
    	{
    		 lat2 = address2.get(0).getLatitude();
    		 lng2 = address2.get(0).getLongitude();
    	}  	
     }
    catch (IOException e2)
    {
    	e2.printStackTrace();		
	}
    
    
      
     Toast.makeText(getApplication(), "LATITUDE1=="+ lat1 +"",Toast.LENGTH_SHORT).show();   
        		
  }
	
	
public void soDirection(View v) 
	{
		//Toast.makeText(getBaseContext(), "This show the direction",Toast.LENGTH_SHORT).show();
		
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
		Uri.parse("http://maps.google.com/maps?saddr="+d_from.getText().toString()+"&daddr="+d_to.getText().toString()));
		startActivity(intent);
				
	}

public class MyLocListener implements LocationListener
        {	 

	    @Override
	     public void onLocationChanged(Location location)
	     {		
		
		
	      }

          @Override
	      public void onProviderDisabled(String provider)
	     {
	     	Toast.makeText(getApplicationContext(),"GPS service is Disabled", Toast.LENGTH_SHORT).show();
	 	
	     }

	   @Override
	   public void onProviderEnabled(String provider)
	    {
		Toast.makeText(getApplicationContext(),"GPS service is Enabled", Toast.LENGTH_SHORT).show();
		
	    }

	   @Override
	   public void onStatusChanged(String provider, int status, Bundle extras)
	    {
		// TODO Auto-generated method stub
		
	    }	
	 
    }

}

