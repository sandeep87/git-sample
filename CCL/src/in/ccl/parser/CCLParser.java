package in.ccl.parser;

import in.ccl.logging.Logger;
import in.ccl.model.Items;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CCLParser {

	private static final String TAG = "CCLParser";

	public static ArrayList <Items> photoParser (String result) {
		ArrayList <Items> photoList = new ArrayList <Items>();
		int totalPages = 0;
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (jsonObject.has("total_pages")) {
				totalPages = jsonObject.getInt("total_pages");
			}
			if (jsonObject.has("current_page")) {
				//int currentPage = jsonObject.getInt("current_page");
			}
			if (jsonObject.has("result")) {
				JSONArray jsonArray = jsonObject.getJSONArray("result");

				for (int i = 0; i < jsonArray.length(); i++) {
					Items item = new Items();
					item.setNumberOfPages(totalPages);
					JSONObject resultJsonObject = jsonArray.getJSONObject(i);
					if (resultJsonObject.has("photo_id")) {
						int photoId = resultJsonObject.getInt("photo_id");
						item.setId(photoId);
					}
					if (resultJsonObject.has("photo_url")) {
						String photoUrl = resultJsonObject.getString("photo_url");
						item.setUrl(photoUrl);
					}
					if (resultJsonObject.has("photo_thumb")) {
						String photoThumb = resultJsonObject.getString("photo_thumb");
						item.setThumbUrl(photoThumb);
					}
					photoList.add(item);
				}
			}
		}
		catch (JSONException e) {
			Logger.info(TAG, e.toString());
		}
		return photoList;
	}

	public static ArrayList <Items> photoAlbumParser (String result) {
		ArrayList <Items> photoGalleryList = new ArrayList <Items>();
		try {
			JSONArray jsonArray = new JSONArray(result);
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

	public static ArrayList <Items> videoAlbumParser (String videoJson) {
		int totalPages = 0;
		ArrayList <Items> videosList = new ArrayList <Items>();
		try {
			JSONObject jsonObject = new JSONObject(videoJson);
			if (jsonObject.has("total_pages")) {
				totalPages = jsonObject.getInt("total_pages");
			}
			if (jsonObject.has("current_page")) {
				//int currentPage = jsonObject.getInt("current_page");
			}
			if (jsonObject.has("result")) {
				JSONArray jsonArray = jsonObject.getJSONArray("result");

				for (int i = 0; i < jsonArray.length(); i++) {
					Items item = new Items();
					item.setNumberOfPages(totalPages);
					JSONObject resultJsonObject = jsonArray.getJSONObject(i);
					if (resultJsonObject.has("video_id")) {
						int videoId = resultJsonObject.getInt("video_id");
						item.setId(videoId);
					}
					if (resultJsonObject.has("video_title")) {
						String videoTitle = resultJsonObject.getString("video_title");
						item.setTitle(videoTitle);
					}
					if (resultJsonObject.has("video_thumb")) {
						String videoThumb = resultJsonObject.getString("video_thumb");
						item.setThumbUrl(videoThumb);
					}
					if (resultJsonObject.has("video_url")) {
						String videoUrl = resultJsonObject.getString("video_url");
						item.setVideoUrl(videoUrl);
					}
					videosList.add(item);
				}
			}
		}
		catch (JSONException e) {
			Logger.info(TAG, e.toString());
		}
		return videosList;

	}

}
