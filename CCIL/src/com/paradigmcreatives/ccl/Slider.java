package com.paradigmcreatives.ccl;

import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class Slider {

	CCL midlet;
	Vector mVector;
	ServerData mserverData;
	SliderDownload mDownload;
	String slderurl = "http://ec2-23-21-38-107.compute-1.amazonaws.com/restv2/sliders";
	
	public Slider(CCL midlet) {
		
		this.midlet = midlet ;
		mserverData = new ServerData();
		String response = mserverData.data(slderurl);
		mVector = new Vector(9);
		try {
			JSONArray array = new JSONArray(response);
			for(int i = 0 ; i < 9 ; i++){
				SliderData mData = new SliderData();
				JSONObject object = array.getJSONObject(i);
				mData.setSlide_album_id(object.getString("slide_album_id"));
				mData.setSlide_id(object.getString("slide_id"));
				mData.setSlide_title(object.getString("slide_title"));
				mData.setSlide_url(object.getString("slide_url"));
				mVector.addElement(mData);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			
			  for( int i = 0 ; i < 9 ; i++){
					
					SliderData mData = (SliderData) mVector.elementAt(i);
					
					String thumb = mData.getSlide_url();	
					
					String findreplace = "celebrity_cricket_league";
					int indx = thumb.indexOf(findreplace);
					thumb = thumb.substring(0, indx) + "traningtest" 
							+ thumb.substring(indx+findreplace.length());	
					
					thumb = thumb.substring(0, thumb.length()-4) + ".png";
					
					System.out.println("Slider url"+thumb);

					mDownload = new SliderDownload(thumb,midlet);
					
					mDownload.start();
				
			  }
			
			} catch (ArrayIndexOutOfBoundsException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		
	}
}
