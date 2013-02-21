package com.paradigmcreatives.ccl;

import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class VideoAlbum {

	private Vdownload mDownload;
	ServerData mServerData;
	VideoAlbums mVideoAlbums;
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
			for (int i = 1; i < mArray.length(); i++) {
				VideoAlbumsData mVdata = new VideoAlbumsData();

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
		
		mVideoAlbums = new VideoAlbums(Midlet,vector);
		
		/*try {
			for (int i = 0; i < 5; i++) {

				VideoAlbumsData mData = (VideoAlbumsData) vector.elementAt(i);
				String thumb = mData.getValbum_thumb();
				String replace = "celebrity_cricket_league";
				int index = thumb.indexOf(replace);
				thumb = thumb.substring(0, index) + "traningtest"
						+ thumb.substring(index + replace.length());
				thumb = thumb.substring(0, thumb.length()-4) + ".png";
				
				System.out.println("Video url"+thumb);
				mDownload = new Vdownload(thumb, Midlet);
				mDownload.start();
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
*/
	}

	public VideoAlbum(CCL midlet, boolean b) {

		Midlet = midlet;
		mServerData = new ServerData();
		String response = mServerData.data(videoAlbum);
		System.out.println(response);

		try {
			vector = new Vector(3, 2);
			JSONArray mArray = new JSONArray(response);
			for (int i = 1; i < 9; i++) {
				VideoAlbumsData mVdata = new VideoAlbumsData();

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
			for (int i = 0; i < vector.size(); i++) {

				VideoAlbumsData mData = (VideoAlbumsData) vector.elementAt(i);
				String thumb = mData.getValbum_thumb();
				String replace = "celebrity_cricket_league";
				int index = thumb.indexOf(replace);
				thumb = thumb.substring(0, index) + "traningtest"
						+ thumb.substring(index + replace.length());
				thumb = thumb.substring(0, thumb.length()-4) + ".png";
				
				System.out.println("Video url"+thumb);
				
				mDownload = new Vdownload(thumb, Midlet, false);
				mDownload.start();
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
