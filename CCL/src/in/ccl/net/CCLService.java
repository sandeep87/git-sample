package in.ccl.net;

import in.ccl.logging.Logger;
import in.ccl.model.Items;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

	public static void getPhotosFromAlbum (int photoAlbumId) {

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
			// designHomeVideoGallery(videoGalleryList);
		}
		catch (JSONException e) {
			Logger.info(TAG, e.toString());
		}
		System.out.println("Title "+videoGalleryList.size());
		return videoGalleryList;
	}

	public static void getVideosFromAlum (int videoAlbumId) {

	}

	public static void getRegionalNews () {

	}
}
