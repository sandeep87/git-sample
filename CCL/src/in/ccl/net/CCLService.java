package in.ccl.net;

import in.ccl.logging.Logger;
import in.ccl.model.Items;
import in.ccl.ui.R;
import in.ccl.util.Constants;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class CCLService {

	private static final String TAG = "CCLSevice";

	public static ArrayList <Items> getPhotoAlbums (String photoJson) {
		ArrayList <Items> photoGalleryList = new ArrayList <Items>();
		try {
			JSONArray jsonArray = new JSONArray(photoJson);
			for (int i = 0; i < jsonArray.length(); i++) {
				Items photoGalleryItem = new Items();
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (!jsonObject.isNull("album_id")) {
					if (jsonObject.has("album_id")) {
						int photoId = jsonObject.getInt("album_id");
						photoGalleryItem.setId(photoId);
					}
				}
				if (!jsonObject.isNull("album_title")) {
					if (jsonObject.has("album_title")) {
						String photoTitle = jsonObject.getString("album_title");
						photoGalleryItem.setTitle(photoTitle);

					}
				}
				if (!jsonObject.isNull("album_thumb")) {
					if (jsonObject.has("album_thumb")) {
						String photoThumbUrl = jsonObject.getString("album_thumb");
						photoGalleryItem.setUrl(photoThumbUrl);
					}
				}
				photoGalleryList.add(photoGalleryItem);
			}
		}
		catch (JSONException e) {
			Logger.info(TAG, e.toString());
		}
		return photoGalleryList;
	}

	public static ArrayList <Items> getBannerItems (String bannerJson) {
		ArrayList <Items> bannerList = new ArrayList <Items>();
		try {
			JSONArray jsonArray = new JSONArray(bannerJson);
			for (int i = 0; i < jsonArray.length(); i++) {
				Items bannerItem = new Items();
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (!jsonObject.isNull("slide_id")) {
					if (jsonObject.has("slide_id")) {
						int slide_id = jsonObject.getInt("slide_id");
						bannerItem.setId(slide_id);
					}
				}
				if (!jsonObject.isNull("slide_url")) {
					if (jsonObject.has("slide_url")) {
						String slide_url = jsonObject.getString("slide_url");
						bannerItem.setUrl(slide_url);
					}
				}
				if (!jsonObject.isNull("slide_title")) {
					if (jsonObject.has("slide_title")) {
						String slide_title = jsonObject.getString("slide_title");
						bannerItem.setTitle(slide_title);
					}
				}
				bannerList.add(bannerItem);
			}
		}
		catch (JSONException e) {
			Logger.info(TAG, e.toString());
		}
		return bannerList;
	}

	public static ArrayList <Items> getVideoAlbums (String videoJson) {
		ArrayList <Items> videoGalleryList = new ArrayList <Items>();
		try {
			JSONArray jsonArray = new JSONArray(videoJson);
			for (int i = 0; i < jsonArray.length(); i++) {
				Items videoGalleryItem = new Items();
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (!jsonObject.isNull("valbum_id")) {
					if (jsonObject.has("valbum_id")) {
						int valbumId = jsonObject.getInt("valbum_id");
						videoGalleryItem.setId(valbumId);

					}
				}
				if (!jsonObject.isNull("valbum_title")) {
					if (jsonObject.has("valbum_title")) {
						String valbumTitle = jsonObject.getString("valbum_title");
						videoGalleryItem.setTitle(valbumTitle);

					}
				}
				if (!jsonObject.isNull("valbum_thumb")) {
					if (jsonObject.has("valbum_thumb")) {
						String valbumThumbUrl = jsonObject.getString("valbum_thumb");
						videoGalleryItem.setUrl(valbumThumbUrl);

					}
				}
				videoGalleryList.add(videoGalleryItem);
			}
		}
		catch (JSONException e) {
			Logger.info(TAG, e.toString());
		}
		return videoGalleryList;
	}

	public static ArrayList <Items> getVideosFromAlbum (Context ctx, int videoAlbumId) {
		new Download(ctx, Constants.VIDEO_PARSER, new CCLDownloadListener() {

			@Override
			public ArrayList <Items> downloadCompleted (ArrayList <Items> resultList) {
				return resultList;
			}
		}).start(ctx.getResources().getString(R.string.video_album_url));
		return null;
	}

	public static ArrayList <Items> getPhotosFromAlbum (Context ctx, int photoAlbumId) {
		new Download(ctx, Constants.PHOTO_PARSER, new CCLDownloadListener() {

			@Override
			public ArrayList <Items> downloadCompleted (ArrayList <Items> resultList) {
				return resultList;
			}
		}).start(ctx.getResources().getString(R.string.photo_album_url + photoAlbumId));
		return null;

	}

	public static void getRegionalNews () {

	}
}
