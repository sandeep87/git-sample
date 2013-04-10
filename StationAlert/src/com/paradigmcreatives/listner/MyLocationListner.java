package com.paradigmcreatives.listner;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.paradigmcreatives.activity.MainActivity;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;

public class MyLocationListner {

	private Context mContext;
	private LocationManager mLocationManager;
	private double mSrtLat;
	private double mEndLng;
	public Listen mListen;

	public MyLocationListner(Context context) {
		mContext = context;
	}

	public boolean getLocationFromProvider(String provider) {
		boolean flag =false;
		//System.out.println(" provider for loc Manger :"+provider);
		mLocationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
		mListen = new Listen();
		mLocationManager.requestLocationUpdates(provider, 0, 0, mListen);
		Location location = mLocationManager.getLastKnownLocation(provider);
		try{
			 mSrtLat=location.getLatitude();
			 mEndLng =location.getLongitude();
			 	//System.out.println(" Name : "+provider + mSrtLat);
				//System.out.println(" Name : "+provider + mEndLng);
				
			 flag = true;
		}catch (NullPointerException e) {
			//System.out.println(" failed provider :"+provider);
			flag = false;
		}
		//System.out.println(" result :"+flag);
		return flag;
	}
	
	public List<Address> getAddressFromLocation() {
		Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
		List<Address> addresses = null;
		try {
			addresses = gcd.getFromLocation(mSrtLat, mEndLng, 3);
		} catch (IOException e) {
			//might be not connected to net
			System.err.println("Parsing Response Failed "+e);
			addresses = null;
		}
		return addresses;
	}

	public String getDetailedAddress(Address add) {
		StringBuffer temp = new StringBuffer();
		//System.out.println("address max indexes :"+add.getMaxAddressLineIndex());
		if (add.getAddressLine(0) != null) {
			temp.append(add.getAddressLine(0)).append("\n");
		} else if (add.getLocality() != null) {
			temp.append(add.getLocality()).append("\n");
		}else if (add.getPostalCode() != null) {
			temp.append(add.getPostalCode()).append("\n");
		}else if (add.getCountryName() != null) {
			temp.append(add.getCountryName());
		}
		return temp.toString();
	}

	public String finalTry() {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		String provider = mLocationManager.getBestProvider(criteria, true);
		return provider;
	}

	public double getlat(){
		return mSrtLat;
	}

	public double getlng(){
		return mEndLng;
	}

	public String getBestAddress(List<Address> addresses, String mAddress){
		if (mAddress != null) {
			if (mAddress.length()<= 5) {
				String temp1 =getDetailedAddress(addresses.get(1));
				if (temp1 != null) {
					mAddress = temp1;
					if (temp1.length() <= 5) {
						String temp2 = getDetailedAddress(addresses.get(2));
						if (temp2 !=null) {
							mAddress = temp2;
						}else{
							mAddress = "Not able to determine current location";
						}
					}
				}else{
					mAddress = "Not able to determine current location";
				}
			}
		}else{
			mAddress = "Not able to determine current location";
		}
		return mAddress;
	}

	public void stopLocationListner(){
		//System.out.println("i am stoppoing the listener of locationlistner");
		if (mListen !=null) {
			mLocationManager.removeUpdates(mListen);
		}
	}


	class Listen implements LocationListener{
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			//System.out.println(" on status changed santhu");
			switch (status) {
			case LocationProvider.OUT_OF_SERVICE:
				//System.out.println("OUT_OF_SERVICE");
				break;
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				//System.out.println("TEMPORARILY_UNAVAILABLE");
				break;
			case LocationProvider.AVAILABLE:
				//System.out.println("AVAILABLE");
				break;
			}
		}

		@Override
		public void onProviderEnabled(String provider) {
			//System.out.println(" on provider enabled santhu");
		}

		@Override
		public void onProviderDisabled(String provider) {
			//System.out.println("on provider disabled santhu");
		}

		@Override
		public void onLocationChanged(Location location) {
			try{
				
				MainActivity.mHandler.sendEmptyMessage(1);
				
				mSrtLat=location.getLatitude();
				mEndLng =location.getLongitude();
				//System.out.println(" on location changed santhu");
			}catch (Exception e) {
				//System.out.println(" on location changed Failed ROO");
			}

		}

	}
}