package com.paradigmcreatives.drivingDirection;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import android.R.integer;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.paradigmcreatives.globalconfig.GlobalConfiguration;



public class MyDrivingDirection {

	//private Map<Integer, Map<String, String>> directionData = new HashMap<Integer, Map<String, String>>();
	private ArrayList<String> mTempDirectionData = new ArrayList<String>();
	private ArrayList<GeoPoint> finalGeoPoints = new ArrayList<GeoPoint>();
	private int setduration;
	
	private String durationInMinutesFormat;
	private String distanceInKmFormat;
	
	public String statusCode;
	
	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	//private String disString;
	private int setdistance;
	private boolean queryOverLimit;
     public MyDrivingDirection(){
     }

	public String buildURLByName(String FromAddress, String ToAddress) {
		StringBuffer urlBuffer = new StringBuffer();
		urlBuffer.append("http://maps.googleapis.com/maps/api/directions/json?");
		urlBuffer.append("origin=");
		urlBuffer.append(FromAddress);
		urlBuffer.append(",");
		urlBuffer.append("&destination=");
		urlBuffer.append(ToAddress);
		urlBuffer.append(",");
		urlBuffer.append("&sensor=");
		urlBuffer.append("false");
		return urlBuffer.toString();
	}
	
	public String buildURLByLocation(double srtLat, double srtLng, double endLat, double endLng) {
		StringBuffer urlBuffer = new StringBuffer();
		urlBuffer.append("http://maps.googleapis.com/maps/api/directions/json?");
		urlBuffer.append("origin=");
		urlBuffer.append(srtLat);
		urlBuffer.append(",");
		urlBuffer.append(srtLng);
		urlBuffer.append("&destination=");
		urlBuffer.append(endLat);
		urlBuffer.append(",");
		urlBuffer.append(endLng);
		urlBuffer.append("&sensor=");
		urlBuffer.append("false");
		//System.out.println(" url :"+urlBuffer.toString());
		return urlBuffer.toString();
	}
     
	
	public InputStream getConnection(String url) {
		InputStream is = null;
		try {
			URLConnection conn = new URL(url).openConnection();
				is = conn.getInputStream();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(e.getMessage(), "InputStream, getConnection Failed");
		}
		return is;
	}
	
	
	public  StringBuilder convertStreamToString(final InputStream input) {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        StringBuilder sBuf = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sBuf.append(line);
            }
        } catch (IOException e) {
        	Log.e(e.getMessage(), "Google parser, stream2string");
        	return sBuf= null;
        } finally {
            try {
                input.close();
            } catch (IOException e) {
            	Log.e(e.getMessage(), "Google parser, stream2string");
            	return sBuf= null;
            }
        }
        return sBuf;
    }
	
	
	public void jsonDataparsing(String jsonString){
		try{
			
		final JSONObject json = new JSONObject(jsonString);
		
		final String statusCode = json.getString("status");
		
		
		final JSONObject jsonRoute = json.getJSONArray("routes").getJSONObject(0);
		final JSONObject leg = jsonRoute.getJSONArray("legs").getJSONObject(0);
		
		setdistance  = leg.getJSONObject("distance").getInt("value");
		setduration = leg.getJSONObject("duration").getInt("value");
		//System.out.println(" from class set duration :"+setduration);
		//directionData.put(0, new WeakestHashMap("You Are At", leg.getString("start_address")));
		//directionData.put(1, new WeakestHashMap("Destination", leg.getString("end_address")));
		//directionData.put(2, new WeakestHashMap("Average Speed", "18 kmph"));
		//directionData.put(3, new WeakestHashMap("Remaining Distance",  leg.getJSONObject("distance").getString("text")));
		//directionData.put(4, new WeakestHashMap("Estimated Time Arrival", leg.getJSONObject("duration").getString("text")));
		
		distanceInKmFormat  = leg.getJSONObject("distance").getString("text");
		durationInMinutesFormat = leg.getJSONObject("duration").getString("text");
		
		//System.out.println(" ---from server : "+distanceInKmFormat);
		//System.out.println(" ---from server : "+durationInMinutesFormat);
		
		mTempDirectionData.add(leg.getString("start_address"));
		mTempDirectionData.add(leg.getString("end_address"));
		mTempDirectionData.add("18 kmph");
		mTempDirectionData.add(leg.getJSONObject("distance").getString("text"));
		mTempDirectionData.add(leg.getJSONObject("duration").getString("text"));
		
		}catch (JSONException e) {
			Log.e(e.getMessage(), "Google JSON Parser - " );
		}
	}
	
	
	
	// Check the status code. and return true if we can go head or else false
	public boolean getJsonCheckStausCode(String jsonString) {
		JSONObject json;
		boolean statusBack = false;
		try {
			json = new JSONObject(jsonString);
		final String statusCode = json.getString("status");
		setStatusCode(statusCode);
		if (GlobalConfiguration.OK.equals(statusCode)) {
			statusBack = true;
		}else{
			statusBack = false;
			}
		}
		catch (JSONException e) {
			statusBack = false;
		}
		return statusBack;
	}

	public void jsonGeoPointsParsing(String jsonString){
		try{
			final JSONObject json = new JSONObject(jsonString);
			final JSONObject jsonRoute = json.getJSONArray("routes").getJSONObject(0);
			final JSONObject leg = jsonRoute.getJSONArray("legs").getJSONObject(0);
			final JSONArray steps = leg.getJSONArray("steps");
			final int numSteps = steps.length();
			for (int i = 0; i < numSteps; i++) {
				//segment.clearPoints();
				//Get the individual step
				final JSONObject step = steps.getJSONObject(i);
				//Retrieve & decode this segment's polyline and add it to the route & segment.
				 ArrayList<GeoPoint> temp =decodePolyLine(step.getJSONObject("polyline").getString("points"));
				 for (GeoPoint geoPoint : temp) {
					finalGeoPoints.add(geoPoint);
				}
			}
			
		}catch (JSONException e) {
			Log.e(e.getMessage(), "Google JSON Parser - " );
		}
	}
	
	public int getDuration(){ 
		// retuning the excatly the seconds required for the journey.
		return (setduration);
	}
	
	public int getdistance(){
		return setdistance;
	}
	
	public String getDurationInMinutes(){ 
		// retuning the excatly the seconds required for the journey.
		return durationInMinutesFormat;
	}
	
	public String getdistanceInKm(){
		return distanceInKmFormat;
	}
	
	
	
	private ArrayList<GeoPoint> decodePolyLine(final String poly) {
		int len = poly.length();
		int index = 0;
		ArrayList<GeoPoint> geoPoints = new ArrayList<GeoPoint>();
		int lat = 0;
		int lng = 0;

		while (index < len) {
		int b;
		int shift = 0;
		int result = 0;
		do {
			b = poly.charAt(index++) - 63;
			result |= (b & 0x1f) << shift;
			shift += 5;
		} while (b >= 0x20);
		int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
		lat += dlat;

		shift = 0;
		result = 0;
		do {
			b = poly.charAt(index++) - 63;
			result |= (b & 0x1f) << shift;
			shift += 5;
		} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;
			GeoPoint point = new GeoPoint((int) (((double) lat / 1E5) * 1E6),
		             (int) (((double) lng / 1E5) * 1E6));
			geoPoints.add(point);
		}
		return geoPoints;
		}
	
   /*  public Map<Integer, Map<String, String>> getDrivingData(){
	return directionData;
	}*/
	
	public ArrayList<GeoPoint> getgeoPoints(){
		return finalGeoPoints;
	}
	
	public ArrayList<String> getTempData(){
		return mTempDirectionData;
	}
}
