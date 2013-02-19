package com.paradigmcreatives.ccl;

import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;



public class VideoAlbum {

	private Vdownload mDownload;
	ServerData mServerData;
	static int i = 0;
	CCL Midlet;
	Vector vector;
	String videoAlbum = "http://ec2-23-21-38-107.compute-1.amazonaws.com/restv2/videoalbums";
	
	public VideoAlbum(CCL Midlet) {
		// TODO Auto-generated constructor stub
		this.Midlet = Midlet;
		mServerData = new ServerData();
		String response = mServerData.data(videoAlbum);
		System.out.println(response);
		
		try {
			vector = new Vector(3, 2);
			JSONArray mArray = new JSONArray(response);
			for(int i = 1 ; i < mArray.length() ; i++){
				VideoAlbumsData  mVdata = new VideoAlbumsData();
				
				JSONObject object = mArray.getJSONObject(i);
				mVdata.setValbum_id(object.getString("valbum_id"));
				mVdata.setValbum_thumb(object.getString("valbum_thumb"));
				mVdata.setValbum_title(object.getString("valbum_title"));
				
				vector.addElement(mVdata);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			   for(int i = 0 ; i < vector.size() ; i++){
					
					VideoAlbumsData mData = (VideoAlbumsData) vector.elementAt(i);
					String thumb = mData.getValbum_thumb();
					System.out.println(mData.getValbum_id()+" : "+thumb);
					mDownload = new Vdownload(thumb,Midlet);
					mDownload.start();		
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		
	}
	
	
	
	

}
