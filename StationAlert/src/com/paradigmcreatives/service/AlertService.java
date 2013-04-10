package com.paradigmcreatives.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.paradigmcreatives.activity.AlarmActivity;
import com.paradigmcreatives.activity.MapViewActivity;
import com.paradigmcreatives.activity.TabViewActivity;
import com.paradigmcreatives.database.Config;
import com.paradigmcreatives.util.Utility;

public class AlertService extends Service implements LocationListener{

	private  static double  srtLat;
	private static double srtLng;
	private double endLat;
	private double endLng;
	public static double getSrtLat() {
		return srtLat;
	}

	public static void setSrtLat(double srtLat) {
		AlertService.srtLat = srtLat;
	}

	public static double getSrtLng() {
		return srtLng;
	}

	public static void setSrtLng(double srtLng) {
		AlertService.srtLng = srtLng;
	}
	public static double alertTime;
	private Config mConfig;
	private LocationManager mLocationManager;
	private ArrayList<Double> averageSpeedArray;
	//private String endAddress;
	//private long oldTime;
	//private WebDrivingDirectionService webService;
	//private String remainingDistance;
	//private float remDisInFLOAT;
	private static final int TWO_MINUTES = 1000 * 60 * 2;
	private static final int ONE_MINUTES = 1000 * 60 * 1;
	private Location oldLocation;
	private Location newlocation;
	private Location updateLocation;
	private long startTimeOfLocation;
	private long endTimeOfLocation;
	public static Handler handlerService;
	private boolean firstTime;
	private Timer timer;
	private String endAddress;
	private boolean stopListner;
	private double showRadiusDistance;
	private double updatePastDistance;
	public static double finalAlertRadius;
	private double avg ;
	public static boolean firstTimeRadiusFlag =false;
	@Override
	public IBinder onBind(Intent intent) {
		//System.out.println(" onbind called now");
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		//System.out.println(" Started service ");

		mConfig =  Config.Instance();
		//webService = new WebDrivingDirectionService();
		firstTime = true;
		//mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		//mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TWO_MINUTES, 0, AlertService.this);

		/*mLocationManager = (LocationManager)AlertService.this.getSystemService(Context.LOCATION_SERVICE);
		mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, AlertService.this);*/

		stopListner = false;
		timer = new Timer();

		Cursor cursor= mConfig.getCurrentStatusTable();
		if (cursor.getCount() >0 ) {
			cursor.moveToFirst();
			do{  
				srtLat = cursor.getDouble(cursor.getColumnIndex("curlat"));
				srtLng = cursor.getDouble(cursor.getColumnIndex("curlng"));
				endLat = cursor.getDouble(cursor.getColumnIndex("endlat"));
				endLng = cursor.getDouble(cursor.getColumnIndex("endlng"));
				endAddress = cursor.getString(cursor.getColumnIndex("endaddress"));
				alertTime = cursor.getDouble(cursor.getColumnIndex("alertTime"));
				//remainingDistance = cursor.getString(cursor.getColumnIndex("remainingDisatance"));
			}while(cursor.moveToNext());

			/*try{
				remainingDistance = remainingDistance.replace("km", " ");
				remainingDistance = remainingDistance.trim();
				remDisInFLOAT  = new Float(remainingDistance);

			}catch (Exception e) {
				// TODO: handle exception
				System.out.println(" converting sting to float "+e);
				//remDisInFLOAT = 0.0f;
			}*/
		}

		double FixedFinalDistance = Utility.distance(srtLat, srtLng, endLat, endLng);
		System.out.println(" FixedFinalDistance: "+(int)(FixedFinalDistance/1000));
		
		if (!Double.isNaN(FixedFinalDistance)) {
			// showing the radius region after 50% of the distance. ( 50/100)
			showRadiusDistance = (0.5 * FixedFinalDistance);

		}

		System.out.println(" half distance : "+showRadiusDistance);
		startTimeOfLocation = System.currentTimeMillis();



		timer.scheduleAtFixedRate(timerTask, 1*60*1000, 1*60*1000);

		/*	 handlerService  = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

			}
		};*/



		//System.out.println(" aleret time  : "+alertTime);

		/*mConfig =  Config.Instance();
		averageSpeedArray = new ArrayList<Integer>();
		webService = new WebDrivingDirectionService();
		mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
		System.out.println(" Started service ");
		mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, AlertService.this);
		oldTime = System.currentTimeMillis();*/
	}


	@Override
	public void onLocationChanged(Location location) {
		//System.out.println(" on location change in service ");
		try{	
			if (firstTime) {
				//System.out.println(" First time noted location ");
				oldLocation = location;
				startTimeOfLocation = System.currentTimeMillis();
				firstTime = false;

			}else{
				newlocation = location;
				//startTimeOfLocation = oldLocation.getTime();
				//srtLat = oldLocation.getLatitude();
				//srtLng = oldLocation.getLongitude();
				setSrtLat(oldLocation.getLatitude());
				setSrtLng(oldLocation.getLongitude());

				endTimeOfLocation = System.currentTimeMillis();

				// System.out.println("******** log *********************");

				/* System.out.println(" oldLocation time : "+startTimeOfLocation);
	    System.out.println(" newlocation time : "+endTimeOfLocation);
	    System.out.println(" old lat and lng " + srtLat + "lng : "+srtLng);
	    System.out.println(" new lat and lng " + newlocation.getLatitude() + "lng : "+newlocation.getLongitude());*/


				// Check whether the new location fix is newer or older
				//long timeDelta = oldLocation.getTime() - newlocation.getTime();
				long timeDelta = startTimeOfLocation - endTimeOfLocation;
				// System.out.println(" time delta  : "+timeDelta);

				boolean isSignificantlyNewer = timeDelta > ONE_MINUTES;
				boolean isSignificantlyOlder = timeDelta < -ONE_MINUTES;
				boolean isNewer = timeDelta > 0;

				/*System.out.println("isSignificantlyNewer "+ isSignificantlyNewer);
	    System.out.println("isSignificantlyOlder "+ isSignificantlyOlder);
	    System.out.println("isNewer "+ isNewer);*/

				// Check whether the new location fix is more or less accurate
				int accuracyDelta = (int) (oldLocation.getAccuracy() - newlocation.getAccuracy());
				boolean isLessAccurate = accuracyDelta > 0;
				boolean isMoreAccurate = accuracyDelta < 0;
				boolean isSignificantlyLessAccurate = accuracyDelta > 200;


				/* System.out.println("isLessAccurate "+ isLessAccurate);
	    System.out.println("isMoreAccurate "+ isMoreAccurate);
	    System.out.println("isSignificantlyLessAccurate "+ isSignificantlyLessAccurate);*/

				// System.out.println(" difference time : "+(endTimeOfLocation - startTimeOfLocation));
				// if (isSignificantlyNewer) {
				double totalDistance = Utility.distance(getSrtLat(), getSrtLng(), endLat, endLng);
				double pastDistance = Utility.distance(srtLat, srtLng, newlocation.getLatitude(), newlocation.getLongitude());

				double futureDistance = Utility.distance(newlocation.getLatitude(), newlocation.getLongitude(), endLat, endLng);
				final double totalDistanceInKm = (totalDistance)/1000;
				if (updateLocation != null){

					updatePastDistance = Utility.distance( updateLocation.getLatitude(), updateLocation.getLongitude(),newlocation.getLatitude(), newlocation.getLongitude());
					double pastDistance1InKm = (updatePastDistance)/1000;
					//System.out.println("pastdistance1:"+pastDistance1InKm);
				}

				if (!Double.isNaN(pastDistance) & !Double.isNaN(futureDistance)) {

					long i =(Math.abs((endTimeOfLocation - startTimeOfLocation))/1000);
					//System.out.println(" past distance "+pastDistance);
					//System.out.println(" past time in sec: "+i);
					if (updatePastDistance != 0) {
						avg = updatePastDistance/i;
					}else {
						avg = pastDistance/i; 
					}
					//double avg = pastDistance/i;

					System.out.println(" avg : "+avg);

					if (averageSpeedArray == null) {
						averageSpeedArray = new ArrayList<Double>();
						averageSpeedArray.add(avg);
					}else{
						averageSpeedArray.add(avg);
					}

					double avgSpeed = calculateAvgSpeed();

					System.out.println(" avg SPEED : "+avgSpeed);


					// get the radius NOTE alert time is in minutes convert to seconds and get value.
					finalAlertRadius = Utility.findRadius(alertTime, avgSpeed);

					double remainingTime = Utility.remainingTime(avgSpeed, futureDistance);

					String remainingTimeInString;

					if (remainingTime >0) {
						remainingTimeInString = Utility.secondToHoursAndMin(remainingTime);
					}else{
						remainingTimeInString = "could not estimate";
					}

					String remainingDistanceInString = Utility.remainigDistanceInString(futureDistance);


					System.out.println(" future distance  "+futureDistance);
					System.out.println(" final Alert distance  "+finalAlertRadius);

					if (TabViewActivity.UiFlag) {
						if (futureDistance < showRadiusDistance) {
							// Now Show the radius
							System.out.println(" sending UI command ");
							TabViewActivity.UiHandler.sendEmptyMessage(0);
						}
					}


					if (futureDistance <= finalAlertRadius) {

						fireALERT();
					}

					String currrentLoc = getCurrentlocationAddress(newlocation.getLatitude(), newlocation.getLongitude());
		System.out.println(" current loc "+currrentLoc);
		System.out.println(" avg Speed in service :"+avgSpeed);
		System.out.println(" remaining distance :"+ remainingDistanceInString);
		System.out.println(" remaining time :"+remainingTimeInString);

					mConfig.dropCurrentLocTable();
					mConfig.setValuesToCurrentLoc(newlocation.getLatitude(), newlocation.getLongitude(), endLat, endLng, currrentLoc, endAddress, alertTime, (avgSpeed * 3.6), remainingDistanceInString,remainingTimeInString);

					//oldLocation = newlocation;
					updateLocation = newlocation;
					startTimeOfLocation = endTimeOfLocation;

					if (TabViewActivity.UiFlag) {
			System.out.println(" SENDING UI ALERT");
			TabViewActivity.UiHandler.sendEmptyMessage(0);
		}

				}
				 else{
	   	System.out.println("significately not new location");
	    }

			}
			mLocationManager.removeUpdates(this);
			stopListner = false;

		}

		catch (Exception e) {
			System.err.println(" ***************Failed in service****************"+e);
		}

		/*String durationInminutes= "unknown";
		String distanceInKm="unknown";
		int duration=0;
		int avgSpeed;
		 float floatAvgValue = 0;

		String currentAddress=" Fetching Newest Current Location";
		List<Address> addresses= null;
		double currentLat = location.getLatitude();
		double currentLng = location.getLongitude();
		float averageSpeed = 0;

		Cursor cursor= mConfig.getCurrentStatusTable();
		if (cursor.getCount() >0 ) {
			cursor.moveToFirst();
			do{
				srtLat = cursor.getDouble(cursor.getColumnIndex("curlat"));
				srtLng = cursor.getDouble(cursor.getColumnIndex("curlng"));
				endLat = cursor.getDouble(cursor.getColumnIndex("endlat"));
				endLng = cursor.getDouble(cursor.getColumnIndex("endlng"));
				endAddress = cursor.getString(cursor.getColumnIndex("endaddress"));
				alertTime = (int) cursor.getDouble(cursor.getColumnIndex("alertTime"));
			}while(cursor.moveToNext());

			try{

			 String url = webService.buildURLBYLatAndLng(currentLat, currentLng, endLat, endLng);
			 webService.getNavigationDataSet(url);
			 durationInminutes = webService.getDurationInMinutesInString();
			 distanceInKm = webService.getDistanceInKm();
			 duration = webService.getDurationInMinutes();

			 if (location.getSpeed() > 0) {
				 averageSpeed = location.getSpeed();
				   averageSpeedArray.add((int)averageSpeed);
					 floatAvgValue =calculateAvgSpeed(); 
					floatAvgValue = Round(floatAvgValue, 2);
			}else{
				averageSpeed = findAverageSpeed(currentLat, currentLng);
				averageSpeedArray.add((int)averageSpeed);
				 floatAvgValue =calculateAvgSpeed(); 
				floatAvgValue = Round(floatAvgValue, 2);
			}

			}catch (Exception e) {
				// No data from webservice. Trying another logic.
				System.out.println(" Error Getting Data From Webserver ");
				duration = findDurationToDestination(currentLat, currentLng, endLat, endLng, averageSpeed);
			}
			//Toast.makeText(AlertService.this, "moved ro", Toast.LENGTH_SHORT).show();
			System.out.println(" From service : "+ durationInminutes);
			System.out.println(" from service : "+distanceInKm);
			System.out.println(" from service int duration :"+duration);
			System.out.println(" average speed : "+averageSpeed);

			try{

			if (duration >= 120) {
				mLocationManager.removeUpdates(this);
				mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Math.round((duration*60)/3), 0, AlertService.this);
			}else if (duration>=60 & duration<120) {
				mLocationManager.removeUpdates(this);
				mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Math.round((duration*60)/4), 0, AlertService.this);
			}else if (duration>=20 & duration<60) {
				mLocationManager.removeUpdates(this);
				mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Math.round((duration*60)/5), 0,  AlertService.this);
			}else if (duration >=10 & duration<20) {
				mLocationManager.removeUpdates(this);
				mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, Math.round((duration*60)/6), 0,  AlertService.this);
			}else if (duration <10) {
				if (alertTime <= duration) {
					fireALERT();
				}

				mLocationManager.removeUpdates(this);
				mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, AlertService.this);
			}
			}catch (Exception e) {
				System.out.println("[User Log] Error In the Duration Time in SERVICE ");
			}

			mConfig.dropCurrentLocTable();

			try{
			addresses= getAddressFromLocation(currentLat, currentLng);
			if (addresses != null) {
				Address add = addresses.get(0);
				currentAddress =getDetailedAddress(add);
			}else{
				currentAddress = "Canot Find Exact Location";
			}
			}catch (Exception e) {
				currentAddress = "Canot Find Exact Location";
			}
			Config.Instance().setValuesToCurrentLoc(currentLat, currentLng, endLat, endLng, currentAddress, endAddress, alertTime, floatAvgValue, distanceInKm,durationInminutes);

			System.gc();
			System.gc();
			System.gc();
		}*/
	}


	private String getCurrentlocationAddress(double latitude, double longitude) {
		String result;
		try{
			List<Address> addresses = getAddressFromLocation(latitude, longitude);
			if (addresses != null) {
				Address add = addresses.get(0);
				result =getDetailedAddress(add);
			}else{
				result = "No Location at this movement";
			}
		}catch (Exception e) {
			result = "No Location at this movement";
		}
		return result;
	}


	TimerTask timerTask = new TimerTask() {
		@Override
		public void run() {
			handler.sendEmptyMessage(0);
		}
	};

	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			System.out.println(" activated request in service ");
			mLocationManager = (LocationManager)AlertService.this.getSystemService(Context.LOCATION_SERVICE);
			mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, AlertService.this);
			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, AlertService.this);
			stopListner = true;
		}
	};



	/*private double findRadius(double alertTime, double avgSpeed) {
		// we are having alert time in minutues, so multiply with 60 to make in sec
		return (alertTime * 60) * avgSpeed;
	}*/

	private double calculateAvgSpeed() {
		double result = 0;
		for (Double i : averageSpeedArray) {
			result = result+i;
		}
		return result/averageSpeedArray.size();
	}




	/*	// TODO Auto-generated method stub
	private int findDurationToDestination(double currentLat,
			double currentLng, double endLat2, double endLng2,float averageSpeed2) {
		float distance;
		int duration = 0;
		float [] distancePredictedToDestinationInMeters = new float[3];
		Location.distanceBetween(currentLat, currentLng, endLat2, endLng2, distancePredictedToDestinationInMeters);
		if (distancePredictedToDestinationInMeters.length >1) {
			distance = distancePredictedToDestinationInMeters[0];
			duration = (int) (distance/averageSpeed2);
		}
		return duration;
	}*/

	/*	// TODO Auto-generated method stub
	private float findAverageSpeed(double currentLat, double currentLng) {
		float [] distanceTraveledInMeters = new float[3];
		float avgSpeed = 0;
		float distance;
		long currentTime = System.currentTimeMillis();
		long travelTimeInSeconds = currentTime - oldTime;
		oldTime = currentTime;
		Location.distanceBetween(srtLat, srtLng, currentLat, currentLng, distanceTraveledInMeters);

		if (distanceTraveledInMeters.length >1) {
			distance = distanceTraveledInMeters[0];
			avgSpeed = distance/travelTimeInSeconds;
		}
		return avgSpeed;
	}*/

	/*	// Iterate the array list and calculate the average speed
	private float calculateAvgSpeed() {
		float result = 0;
		for (Integer i : averageSpeedArray) {
			result = result+i;
		}
		return result/averageSpeedArray.size();
	}*/

	/*private  float Round(float Rval, int Rpl) {
		  float p = (float)Math.pow(10,Rpl);
		  Rval = Rval * p;
		  float tmp = Math.round(Rval);
		  return (float)tmp/p;
		  }*/


	// Stop all the service and clear all the db and move to alertActivity as a new task Activity.
	private void fireALERT() {
		mConfig.dropCurrentLocTable();
		mConfig.dropStatusTable();
		mConfig.dropGeoLocationTable();
		mConfig.dropMaxMinLocationTable();

		timer.cancel();

		try{
			mLocationManager.removeUpdates(AlertService.this);
		}catch (Exception e) {
		}

		Intent intent = new Intent(AlertService.this, AlarmActivity.class);
		intent.putExtra("alertTimeTag", alertTime);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		stopSelf();
		startActivity(intent);
	}

	@Override
	public void onProviderDisabled(String provider) {
		//System.out.println("----service onProviderDisabled");
	}

	@Override
	public void onProviderEnabled(String provider) {
		//System.out.println("----service onProviderEnabled");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		//System.out.println("----service onStatusChanged");
		switch (status) {
		case LocationProvider.OUT_OF_SERVICE:
			//System.out.println("OUT_OF_SERVICE");
			break;
		case LocationProvider.TEMPORARILY_UNAVAILABLE:
			//System.out.println("TEMPORARILY_UNAVAILABLE");
			break;
		case LocationProvider.AVAILABLE:
		System.out.println("AVAILABLE");
			break;
		}
	}

	public List<Address> getAddressFromLocation(double currentLat, double currentLng) {
		Geocoder gcd = new Geocoder(AlertService.this, Locale.getDefault());
		List<Address> addresses = null;
		try {
			addresses = gcd.getFromLocation(currentLat, currentLng, 3);
		} catch (IOException e) {
			//might be not connected to net
			System.err.println("getAddressFromLocation Failed in Service "+e);
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

	@Override
	public void onDestroy() {
		//System.out.println(" on destory -------- service ");
		try{
			timer.cancel();
			if (stopListner) {
				//System.out.println(" removed now in service");
				mLocationManager.removeUpdates(AlertService.this);
			}else {
				//System.out.println(" no need to remove already removed");
			}
		}catch (Exception e) {

			e.printStackTrace();
		}
		super.onDestroy();
	}

	/*private int getRadius(){
		int radius = 0;
		int time;
		int presentDist;
		int dist;
		if(lastDistance == -1 && lastDistance==-1)
		{
			presentDist = getRemainingDistance();
			time = getRemainingTime();
			radius = (presentDist/time)*alertTime;
			lastDistance = presentDist;
			lastTime = System.currentTimeMillis();
			return radius;
		}
		else
		{
			time = (int) (System.currentTimeMillis() - lastTime)/1000;
			dist = lastDistance - getRemainingDistance();
			int speed = dist/time;
			radius = speed*alertTime;
			lastDistance = getRemainingDistance();
			lastTime = System.currentTimeMillis();
			return radius;
		}
	}*/
}