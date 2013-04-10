package com.paradigmcreatives.util;

public class Utility {

	public static double distance(double lat1, double lon1, double lat2, double lon2) {
		  double theta = lon1 - lon2;
		  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		  dist = Math.acos(dist);
		  dist = rad2deg(dist);
		  dist = dist * 60 * 1.1515;//its in miles
		  dist = dist * 1.609344;//its in kilometers
		  dist = dist * 1000;// its in meter
		  return (Math.abs(dist));
		}
	
	private static double deg2rad(double deg) {
		  return (deg * Math.PI / 180.0);
		}
	
	private static double rad2deg(double rad) {
		  return (rad * 180.0 / Math.PI);
		}
	
	
	// we are having alert time in minutues, so multiply with 60 to make in sec
	public static double findRadius(double alertTime, double avgSpeed) {
		return (alertTime * 60) * avgSpeed;
	}
	
	public static double remainingTime(double avgSpeed, double futureDistance) {
		double result = 0;
		try{
			result = futureDistance /avgSpeed;
		}catch (Exception e) {
			result =0;
		}
		return result;
	}
	
	public static String remainigDistanceInString(double futureDistance){
		String result;
		try{
			futureDistance = Math.round(futureDistance/1000);
			result = Double.toString(futureDistance)+ " Km";
			
		}catch (Exception e) {
			result = "could not estimate";
		}
		return result;
	}
	
	public static String secondToHoursAndMin(double seconds){
		
		String result;
		try{
			
			int hours = (int) (seconds/3600);
			int minutes = (int)(seconds % 3600) / 60;
			int sec = (int)seconds % 60;
				result=  hours +"Hrs " +minutes + "Min "+sec+"Sec";
			
		}catch (Exception e) {
			result = "could not estimate";
		}
		
		return result;
	}
	
	/*private static float Round(float Rval, int Rpl) {
		  float p = (float)Math.pow(10,Rpl);
		  Rval = Rval * p;
		  float tmp = Math.round(Rval);
		  return (float)tmp/p;
		  }*/
	
}
