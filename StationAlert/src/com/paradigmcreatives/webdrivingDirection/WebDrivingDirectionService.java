package com.paradigmcreatives.webdrivingDirection;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.util.Log;

public class WebDrivingDirectionService {
	
	
	public static final int MODE_ANY = 0;
	public static final int MODE_CAR = 1;
	public static final int MODE_WALKING = 2;
	
	public String webServerData;
	
	public String getDurationInMinutesInString() {
		return durationInMinutesInString;
	}


	public void setDurationInMinutesInString(String durationInMinutesInString) {
		this.durationInMinutesInString = durationInMinutesInString;
	}


	public String getDistanceInKm() {
		return distanceInKm;
	}


	public void setDistanceInKm(String distanceInKm) {
		this.distanceInKm = distanceInKm;
	}


	public int getDurationInMinutes() {
		return durationInMinutes;
	}


	public void setDurationInMinutes(int durationInMinutes) {
		this.durationInMinutes = durationInMinutes;
	}


	public String durationInMinutesInString;
	public String distanceInKm;
	public int durationInMinutes;

	public String buildURLBYLatAndLng(double mSrtLat, double mSrtLng, double mEndLat, double mEndLng) {
		
		StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.google.com/maps?f=d&hl=en");
        urlString.append("&saddr=");//from
        urlString.append(Double.toString(mSrtLat));
        urlString.append(",");
        urlString.append(Double.toString(mSrtLng));
        urlString.append("&daddr=");//to
        urlString.append(Double.toString(mEndLat));
        urlString.append(",");
        urlString.append(Double.toString(mEndLng));
        urlString.append("&ie=UTF8&0&om=0&output=kml");
		return urlString.toString();
	}
	
	
	public static String inputStreamToString (InputStream in) throws IOException {
	    StringBuffer out = new StringBuffer();
	    byte[] b = new byte[4096];
	    for (int n; (n = in.read(b)) != -1;) {
	        out.append(new String(b, 0, n));
	    }
	    return out.toString();
	}
	
/*	public static NavigationDataSet calculateRoute(String startCoords, String targetCoords, int mode) {
	    String urlPedestrianMode = "http://maps.google.com/maps?" + "saddr=" + startCoords + "&daddr="
	            + targetCoords + "&sll=" + startCoords + "&dirflg=w&hl=en&ie=UTF8&z=14&output=kml";

	    Log.d("Testing Google Map Ping", "urlPedestrianMode: "+urlPedestrianMode);

	    String urlCarMode = "http://maps.google.com/maps?" + "saddr=" + startCoords + "&daddr="
	            + targetCoords + "&sll=" + startCoords + "&hl=en&ie=UTF8&z=14&output=kml";

	    Log.d("Testing Google Map Ping", "urlCarMode: "+urlCarMode);

	    NavigationDataSet navSet = null;
	    // for mode_any: try pedestrian route calculation first, if it fails, fall back to car route
	    if (mode==MODE_ANY||mode==MODE_WALKING) navSet = MapService.getNavigationDataSet(urlPedestrianMode);
	    if (mode==MODE_ANY&&navSet==null||mode==MODE_CAR) navSet = MapService.getNavigationDataSet(urlCarMode);
	    return navSet;
	}*/
	
	
	/**
	 * Retrieve navigation data set from either remote URL or String
	 * @param url
	 * @return navigation set
	 */
	public  NavigationDataSet getNavigationDataSet(String url) {

	    // urlString = "http://192.168.1.100:80/test.kml";
		
	   // Log.d("Testing Google Map Ping","urlString -->> " + url);
	    
	    NavigationDataSet navigationDataSet = null;
	    try
	        {           
	        final URL aUrl = new URL(url);
	        
	        //System.out.println("-- URL : "+aUrl);
	        
	        final URLConnection conn = aUrl.openConnection();
	        conn.setReadTimeout(15 * 1000);  // timeout for reading the google maps data: 15 secs
	        conn.connect();

	        /* Get a SAXParser from the SAXPArserFactory. */
	        SAXParserFactory spf = SAXParserFactory.newInstance(); 
	        SAXParser sp = spf.newSAXParser(); 

	        /* Get the XMLReader of the SAXParser we created. */
	        XMLReader xr = sp.getXMLReader();

	        /* Create a new ContentHandler and apply it to the XML-Reader*/ 
	        NavigationSaxHandler navSax2Handler = new NavigationSaxHandler(); 
	        xr.setContentHandler(navSax2Handler); 

	        /* Parse the xml-data from our URL. */ 

	       // System.out.println("-- url : "+conn); 
	        System.out.println(" host Name :"+aUrl.getHost()); 
	        xr.parse(new InputSource(aUrl.openStream()));

	        /* Our NavigationSaxHandler now provides the parsed data to us. */ 
	        navigationDataSet = navSax2Handler.getParsedData(); 
	        
	        
	        getContentFromWebString(navigationDataSet.getCurrentPlacemark().description);
	      
	        
	        
	        /* Set the result to be displayed in our GUI. */ 
	       // System.out.println(" hello");
	       // System.out.println("------"+navigationDataSet.toString());
	        
/*	        System.out.println("###########################");
	        System.out.println(" Placemark Data ");
	        System.out.println(navigationDataSet.toString());
	        System.out.println("###########################");
	       
	        System.out.println("                         ");
	        
	        System.out.println("###########################");
	        System.out.println(" currentPlacemark ");
	        System.out.println(" currentPlacemark title"+navigationDataSet.getCurrentPlacemark().title);
	        System.out.println(" currentPlacemark description"+navigationDataSet.getCurrentPlacemark().description);
	        System.out.println(" currentPlacemark address"+navigationDataSet.getCurrentPlacemark().address);
	        System.out.println("currentPlacemark coordinates"+navigationDataSet.getCurrentPlacemark().coordinates);
	        System.out.println("###########################");
	        
	        System.out.println("                         ");
	        
	        System.out.println("###########################");
	        System.out.println(" routePlacemark ");
	        System.out.println(" routePlacemark title"+navigationDataSet.getRoutePlacemark().title);
	        System.out.println(" routePlacemark description"+navigationDataSet.getRoutePlacemark().description);
	        System.out.println(" routePlacemark address"+navigationDataSet.getRoutePlacemark().address);
	        System.out.println(" routePlacemark coordinates"+navigationDataSet.getRoutePlacemark().coordinates);
	        System.out.println("###########################");*/
	        
	        
	        //Log.d("Testing Google Map Ping","navigationDataSet: "+navigationDataSet.toString());

	    } catch (Exception e) {
	        // Log.e(myapp.APP, "error with kml xml", e);
	        navigationDataSet = null;
	        e.printStackTrace();
	      }   

	    return navigationDataSet;
	}
	
	
	// Break the string and get the distance and duration from it.
	private void getContentFromWebString(String description) {
		
		System.out.println(" ORIGINAL DATA FROM WEBSERVER : "+description);
		
		int duration = 0;
		String distanceInKm = description.substring(description.indexOf(" ")+1, description.indexOf("(")-1);
		String durationInMinutes = description.substring(description.indexOf("(")+1, description.indexOf(")"));
		durationInMinutes = durationInMinutes.replaceFirst("about ", "");
		
		distanceInKm = distanceInKm.replaceFirst("&#160;", " ");
		
		setDistanceInKm(distanceInKm);
		setDurationInMinutesInString(durationInMinutes);
		
		if (durationInMinutes.contains("day")) {
			String d  = durationInMinutes.substring(0, durationInMinutes.indexOf("d")-1);
			duration = (Integer.parseInt(d) * 24 )* 60;
			
				if (durationInMinutes.contains("hour")) {
					String h  = durationInMinutes.substring(durationInMinutes.indexOf("y")+1, durationInMinutes.indexOf("h")-1);
					h = h.trim();
					duration = duration + Integer.parseInt(h) * 60;
					
					if (durationInMinutes.contains("mins")) {
						String m  = durationInMinutes.substring(durationInMinutes.indexOf("r")+1, durationInMinutes.indexOf("m")-1);
						m = m.trim();
						duration = duration + Integer.parseInt(m);
					}
					System.out.println(" d h m : "+duration);
				}
			
		}else if (durationInMinutes.contains("hour")) {
			String h  = durationInMinutes.substring(durationInMinutes.indexOf("y")+1, durationInMinutes.indexOf("h")-1);
			h = h.trim();
			duration = duration + Integer.parseInt(h) * 60;
			
			if (durationInMinutes.contains("mins")) {
				String m  = durationInMinutes.substring(durationInMinutes.indexOf("r")+1, durationInMinutes.indexOf("m")-1);
				m = m.trim();
				duration = duration + Integer.parseInt(m);
				System.out.println(" h and m : "+ duration);
			}
			
		}else{
			String m  = durationInMinutes.substring(durationInMinutes.indexOf("r")+1, durationInMinutes.indexOf("m")-1);
			m = m.trim();
			duration = duration + Integer.parseInt(m);
			System.out.println(" only min : "+duration);
		}
		
		setDurationInMinutes(duration);
		
	}


	
}
