package in.ccl.net;

import in.ccl.database.CCLDAO;
import in.ccl.logging.Logger;
import in.ccl.model.Items;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

public class CCLService {

	private static final String TAG = "CCLSevice";

	public static ArrayList <Items> getPhotoAlbums (String photoJson) {
		final ArrayList <Items> photoGalleryList = new ArrayList <Items>();
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
						photoGalleryItem.setPhotoOrVideoUrl(photoThumbUrl);
					}
				}
				photoGalleryList.add(photoGalleryItem);
			}
		}
		catch (JSONException e) {
			Logger.info(TAG, e.toString());
		}

		new AsyncTask <Void, Void, Void>() {

			@Override
			protected Void doInBackground (Void... params) {
				CCLDAO.insertPhotoGalleryItems(photoGalleryList);
				return null;
			}
		}.execute();

		return photoGalleryList;
	}

	public static ArrayList <Items> getBannerItems (String bannerJson) {
		final ArrayList <Items> bannerList = new ArrayList <Items>();
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
						bannerItem.setPhotoOrVideoUrl(slide_url);
					}
				}
				if (!jsonObject.isNull("slide_title")) {
					if (jsonObject.has("slide_title")) {
						String slide_title = jsonObject.getString("slide_title");
						bannerItem.setTitle(slide_title);
					}
				}
				if (!jsonObject.isNull("slide_album_id")) {
					if (jsonObject.has("slide_album_id")) {
						int slide_album_id = jsonObject.getInt("slide_album_id");
						bannerItem.setAlbumId(slide_album_id);
					}
				}
				bannerList.add(bannerItem);
			}
		}
		catch (JSONException e) {
			Logger.info(TAG, e.toString());
		}

		// inserting download data into database.
		new AsyncTask <Void, Void, Void>() {

			@Override
			protected Void doInBackground (Void... params) {
				CCLDAO.insertBannerItems(bannerList);
				return null;
			}
		}.execute();

		return bannerList;
	}

	public static ArrayList <Items> getVideoAlbums (String videoJson) {
		final ArrayList <Items> videoGalleryList = new ArrayList <Items>();
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
						videoGalleryItem.setPhotoOrVideoUrl(valbumThumbUrl);

					}
				}
				videoGalleryList.add(videoGalleryItem);
			}
		}
		catch (JSONException e) {
			Logger.info(TAG, e.toString());
		}
		new AsyncTask <Void, Void, Void>() {

			@Override
			protected Void doInBackground (Void... params) {
				CCLDAO.insertVideoGalleryItems(videoGalleryList);
				return null;
			}
		}.execute();
		return videoGalleryList;
	}

	public static void getRegionalNews () {

	}
}
