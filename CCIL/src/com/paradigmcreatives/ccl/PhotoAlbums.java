package com.paradigmcreatives.ccl;

import java.util.Vector;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class PhotoAlbums 

{
	private Download mDownload;
	ServerData mServerData;
	HomeScreenCanvas homeScreenCanvas;
	CCL Midlet;
	Vector vector;
	PhotoAlbum mPhotoDownload;
	String photoAlbum = "http://ec2-54-234-80-86.compute-1.amazonaws.com/restv2/albums";

	public PhotoAlbums(CCL Midlet) {
		
		this.Midlet = Midlet;
		mServerData = new ServerData();
		String response = mServerData.data(photoAlbum);
		try {
			vector = new Vector(5,2);
			JSONArray mArray = new JSONArray(response);
			for(int i = 0 ; i < mArray.length() ; i++) {
	
				PhotoAlbumsData mData = new PhotoAlbumsData();
				JSONObject object = mArray.getJSONObject(i);
				mData.setAlbum_id(object.getString("album_id"));
				mData.setAlbum_title(object.getString("album_title"));
				mData.setAlbum_thumb(object.getString("album_thumb"));
				vector.addElement(mData);	
			}
		} catch (JSONException e1) {
			
			e1.printStackTrace();
		}catch(ArrayIndexOutOfBoundsException e) {
			
			System.out.println("Array index bound exception");
		}
		
		mPhotoDownload = new PhotoAlbum(Midlet,vector);
		
		/*try {
			
		  for( int i = 0 ; i <5 ; i++){
				
				PhotoAlbumsData mData = (PhotoAlbumsData) vector.elementAt(i);
				
				String thumb = mData.getAlbum_thumb();	
				
				String findreplace = "celebrity_cricket_league";
				int indx = thumb.indexOf(findreplace);
				thumb = thumb.substring(0, indx) + "traningtest" 
						+ thumb.substring(indx+findreplace.length());	
				
				thumb = thumb.substring(0, thumb.length()-4) + ".png";		

				mDownload = new Download(thumb,mData.getAlbum_id(),mData.getAlbum_title(),Midlet);
				
				mDownload.start();	
			
		  }
		
		} catch (ArrayIndexOutOfBoundsException e) {
			
			e.printStackTrace();
		}*/
	}	

	public PhotoAlbums(CCL ccl, boolean b) {
		
		this.Midlet = ccl;
		
		mServerData = new ServerData();
		
		String response = mServerData.data(photoAlbum);
		try {
			vector = new Vector(5,2);
			JSONArray mArray = new JSONArray(response);
			for(int i = 0 ; i < 9 ; i++) {
	
				PhotoAlbumsData mData = new PhotoAlbumsData();
				JSONObject object = mArray.getJSONObject(i);
				mData.setAlbum_id(object.getString("album_id"));
				mData.setAlbum_title(object.getString("album_title"));
				mData.setAlbum_thumb(object.getString("album_thumb"));
				vector.addElement(mData);	
			}
		} catch (JSONException e1) {
			
			e1.printStackTrace();
		}catch(ArrayIndexOutOfBoundsException e) {
			
			System.out.println("Array index bound exception");
		}
		
		try {
			
		  for( int i = 0 ; i < 9 ; i++){
				
				PhotoAlbumsData mData = (PhotoAlbumsData) vector.elementAt(i);
				
				String thumb = mData.getAlbum_thumb();	
				
				String findreplace = "celebrity_cricket_league";
				int indx = thumb.indexOf(findreplace);
				thumb = thumb.substring(0, indx) + "traningtest" 
						+ thumb.substring(indx+findreplace.length());	
				
				thumb = thumb.substring(0, thumb.length()-4) + ".png";
				
				System.out.println("old URL "+"  : "+thumb);	


				mDownload = new Download(thumb,false,ccl);
				
				mDownload.start();	
			
		  }
		
		} catch (ArrayIndexOutOfBoundsException e) {
			
			e.printStackTrace();
		}
	
	}

	
}
