package in.ccl.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.util.Log;

public class JSONPullParser {

	// Sets the initial size of the vector that stores data.
	public static final int VECTOR_INITIAL_SIZE = 500;

	// Storage for a single ContentValues for image data
	private static ContentValues item;

	// A vector that will contain all of the images
	private Vector <ContentValues> mImages;

	private Vector <ContentValues> mPages;

	private static final String TAG = "JSONPullParser";

	/**
	 * A getter that returns the banner photos data Vector
	 * 
	 * @return A Vector containing all of the banner image data retrieved by the parser
	 */
	public Vector <ContentValues> getparsedData () {
		return mImages;
	}

	public Vector <ContentValues> getPages () {
		return mPages;
	}

	public void parseBannerJson (InputStream inputStream, BroadcastNotifier mBroadcaster) {
		// Creates a new store for image URL data
		mImages = new Vector <ContentValues>(VECTOR_INITIAL_SIZE);
		String result = readStream(inputStream);
		if (result != null) {
			try {
				JSONArray jsonArray = new JSONArray(result);
				for (int i = 0; i < jsonArray.length(); i++) {
					item = new ContentValues();
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					if (!jsonObject.isNull("slide_id")) {
						if (jsonObject.has("slide_id")) {
							int slide_id = jsonObject.getInt("slide_id");
							item.put(DataProviderContract.ROW_ID, slide_id);
						}
					}
					if (!jsonObject.isNull("slide_url")) {
						if (jsonObject.has("slide_url")) {
							String slide_url = jsonObject.getString("slide_url");
							item.put(DataProviderContract.BANNER_IMAGE_URL_COLUMN, slide_url);
						}
					}
					if (!jsonObject.isNull("slide_title")) {
						if (jsonObject.has("slide_title")) {
							String slide_title = jsonObject.getString("slide_title");
							item.put(DataProviderContract.IMAGE_NAME_COLUMN, slide_title);
						}
					}
					if (!jsonObject.isNull("slide_album_id")) {
						if (jsonObject.has("slide_album_id")) {
							int slide_album_id = jsonObject.getInt("slide_album_id");
							item.put(DataProviderContract.BANNER_ALBUM_ID_COLUMN, slide_album_id);
						}
					}
					mImages.add(item);
				}
				// Testing for Banner items updates.
				/*
				 * item = new ContentValues(); item.put(DataProviderContract.ROW_ID, 13); item.put(DataProviderContract.BANNER_IMAGE_URL_COLUMN, "http://www.ccl.in/images/gallery/CCL%203%20Curtain%20Raiser%20Event/CCL%203%20Curtain%20Raiser%20Event%20%28203%29.jpg");
				 * item.put(DataProviderContract.IMAGE_NAME_COLUMN, "CCL 3 : Curtain Raiser Event update ONE"); item.put(DataProviderContract.BANNER_ALBUM_ID_COLUMN, 1234); mImages.add(item);
				 * 
				 * item = new ContentValues(); item.put(DataProviderContract.ROW_ID, 14); item.put(DataProviderContract.BANNER_IMAGE_URL_COLUMN, "http://www.ccl.in/images/gallery/CCL%203%20Curtain%20Raiser%20Event/CCL%203%20Curtain%20Raiser%20Event%20%28100%29.jpg");
				 * item.put(DataProviderContract.IMAGE_NAME_COLUMN, "CCL 3 : Curtain Raiser Event update TWO"); item.put(DataProviderContract.BANNER_ALBUM_ID_COLUMN, 1235); mImages.add(item);
				 * 
				 * item = new ContentValues(); item.put(DataProviderContract.ROW_ID, 15); item.put(DataProviderContract.BANNER_IMAGE_URL_COLUMN, "http://www.ccl.in/images/gallery/CCL%203%20Curtain%20Raiser%20Event/CCL%203%20Curtain%20Raiser%20Event%20%2860%29.jpg");
				 * item.put(DataProviderContract.IMAGE_NAME_COLUMN, "CCL 3 : Curtain Raiser Event update THREE"); item.put(DataProviderContract.BANNER_ALBUM_ID_COLUMN, 1236); mImages.add(item);
				 */}
			catch (JSONException e) {
				Log.i(TAG, "Banner items parsing exception");
			}
		}
	}

	private String readStream (InputStream inputStream) {
		String value = null;

		StringBuilder sb = null;

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(inputStream));
			String line = "";
			sb = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			value = sb.toString();
			return value;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			if (reader != null) {
				try {
					reader.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public void parsePhotoAlbumJson (InputStream inputStream, BroadcastNotifier mBroadcaster) {
		String result = readStream(inputStream);
		mImages = new Vector <ContentValues>(VECTOR_INITIAL_SIZE);

		if (result != null) {
			try {
				JSONArray jsonArray = new JSONArray(result);
				for (int i = 0; i < jsonArray.length(); i++) {
					item = new ContentValues();
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					if (!jsonObject.isNull("album_id")) {
						if (jsonObject.has("album_id")) {
							int photoId = jsonObject.getInt("album_id");
							item.put(DataProviderContract.ROW_ID, photoId);
						}
					}
					if (!jsonObject.isNull("album_title")) {
						if (jsonObject.has("album_title")) {
							String photoTitle = jsonObject.getString("album_title");
							item.put(DataProviderContract.IMAGE_NAME_COLUMN, photoTitle);

						}
					}
					if (!jsonObject.isNull("album_thumb")) {
						if (jsonObject.has("album_thumb")) {
							String photoThumbUrl = jsonObject.getString("album_thumb");
							item.put(DataProviderContract.PHOTO_ALBUM_IMAGE_URL_COLUMN, photoThumbUrl);
						}
					}
					mImages.add(item);
				}

				// TESTING FOR UPDATES OF PHOTO GALLERY
				/*
				 * item = new ContentValues(); item.put(DataProviderContract.ROW_ID, 111); item.put(DataProviderContract.IMAGE_NAME_COLUMN, "New CCL3 ONE"); item.put(DataProviderContract.PHOTO_ALBUM_IMAGE_URL_COLUMN,
				 * "http://www.ccl.in/images/gallery/CCL%203%20Curtain%20Raiser%20Event%20thumb/CCL%203%20Curtain%20Raiser%20Event%20%28217%29.jpg"); mImages.add(item); item = new ContentValues(); item.put(DataProviderContract.ROW_ID, 112); item.put(DataProviderContract.IMAGE_NAME_COLUMN, "New CCL3 Two");
				 * item.put(DataProviderContract.PHOTO_ALBUM_IMAGE_URL_COLUMN, "http://www.ccl.in/images/gallery/CCL%203%20Curtain%20Raiser%20Event%20thumb/CCL%203%20Curtain%20Raiser%20Event%20(215).jpg"); mImages.add(item); item = new ContentValues(); item.put(DataProviderContract.ROW_ID, 113);
				 * item.put(DataProviderContract.IMAGE_NAME_COLUMN, "New CCL3 Three"); item.put(DataProviderContract.PHOTO_ALBUM_IMAGE_URL_COLUMN, "http://www.ccl.in/images/gallery/CCL%203%20Curtain%20Raiser%20Event%20thumb/CCL%203%20Curtain%20Raiser%20Event%20(213).jpg"); mImages.add(item);
				 */

			}
			catch (JSONException e) {
				Log.i(TAG, "Photo album json parsing exception");
			}
		}
	}

	public void parseVideoAlbumJson (InputStream inputStream, BroadcastNotifier mBroadcaster) {
		String result = readStream(inputStream);
		mImages = new Vector <ContentValues>(VECTOR_INITIAL_SIZE);

		if (result != null) {
			try {
				JSONArray jsonArray = new JSONArray(result);
				for (int i = 0; i < jsonArray.length(); i++) {
					item = new ContentValues();
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					if (!jsonObject.isNull("valbum_id")) {
						if (jsonObject.has("valbum_id")) {
							int valbumId = jsonObject.getInt("valbum_id");
							item.put(DataProviderContract.ROW_ID, valbumId);
						}
					}
					if (!jsonObject.isNull("valbum_title")) {
						if (jsonObject.has("valbum_title")) {
							String valbumTitle = jsonObject.getString("valbum_title");
							item.put(DataProviderContract.IMAGE_NAME_COLUMN, valbumTitle);

						}
					}
					if (!jsonObject.isNull("valbum_thumb")) {
						if (jsonObject.has("valbum_thumb")) {
							String valbumThumbUrl = jsonObject.getString("valbum_thumb");
							item.put(DataProviderContract.VIDEO_ALBUM_IMAGE_URL_COLUMN, valbumThumbUrl);

						}
					}
					mImages.add(item);
				}
				// testing for updates
				/*
				 * item = new ContentValues(); item.put(DataProviderContract.ROW_ID, 111); item.put(DataProviderContract.IMAGE_NAME_COLUMN, "New CCL3 Video One"); item.put(DataProviderContract.VIDEO_ALBUM_IMAGE_URL_COLUMN,
				 * "http://www.ccl.in/images/gallery/CCL%203%20Curtain%20Raiser%20Event%20thumb/CCL%203%20Curtain%20Raiser%20Event%20(213).jpg"); mImages.add(item); item = new ContentValues(); item.put(DataProviderContract.ROW_ID, 112); item.put(DataProviderContract.IMAGE_NAME_COLUMN, "New CCL3 Video Two");
				 * item.put(DataProviderContract.VIDEO_ALBUM_IMAGE_URL_COLUMN, "http://www.ccl.in/images/gallery/CCL%203%20Curtain%20Raiser%20Event%20thumb/CCL%203%20Curtain%20Raiser%20Event%20(213).jpg"); mImages.add(item); item = new ContentValues(); item.put(DataProviderContract.ROW_ID, 113);
				 * item.put(DataProviderContract.IMAGE_NAME_COLUMN, "New CCL3 Video THree"); item.put(DataProviderContract.VIDEO_ALBUM_IMAGE_URL_COLUMN, "http://www.ccl.in/images/gallery/CCL%203%20Curtain%20Raiser%20Event%20thumb/CCL%203%20Curtain%20Raiser%20Event%20(213).jpg"); mImages.add(item);
				 */}
			catch (JSONException e) {
				e.printStackTrace();
				Log.i(TAG, "Video album parsing exception.");
			}
		}
	}

	public void parsePhotoJson (InputStream inputStream, BroadcastNotifier mBroadcaster) {
		String result = readStream(inputStream);
		System.out.println("Result " + result);
		mImages = new Vector <ContentValues>(VECTOR_INITIAL_SIZE);
		mPages = new Vector <ContentValues>(VECTOR_INITIAL_SIZE);
		ContentValues pages = new ContentValues();
		if (result != null) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (jsonObject.has("album_id")) {
					pages.put(DataProviderContract.ALBUM_ID_COLUMN, jsonObject.getInt("album_id"));
					pages.put(DataProviderContract.CATEGORY_ID, 1);

				}
				if (jsonObject.has("total_pages")) {
					pages.put(DataProviderContract.TOTAL_PAGES, jsonObject.getInt("total_pages"));

				}
				if (jsonObject.has("result")) {
					JSONArray jsonArray = jsonObject.getJSONArray("result");

					for (int i = 0; i < jsonArray.length(); i++) {
						item = new ContentValues();
						item.put(DataProviderContract.ALBUM_ID_COLUMN, pages.getAsInteger(DataProviderContract.ALBUM_ID_COLUMN));
						item.put(DataProviderContract.CATEGORY_ID, pages.getAsInteger(DataProviderContract.CATEGORY_ID));

						JSONObject resultJsonObject = jsonArray.getJSONObject(i);
						if (resultJsonObject.has("photo_id")) {
							int photoId = resultJsonObject.getInt("photo_id");
							item.put(DataProviderContract.ROW_ID, photoId);
						}
						if (resultJsonObject.has("photo_url")) {
							String photoUrl = resultJsonObject.getString("photo_url");
							item.put(DataProviderContract.URL, photoUrl);
						}
						if (resultJsonObject.has("photo_thumb")) {
							String photoThumb = resultJsonObject.getString("photo_thumb");
							item.put(DataProviderContract.THUMB_IMAGE_URL, photoThumb);
						}
						mImages.add(item);
					}
					mPages.add(pages);
				}
			}
			catch (JSONException e) {
				Log.i(TAG, e.toString());
			}
		}
	}

	public void parseVideoJson (InputStream inputStream, BroadcastNotifier mBroadcaster) {
		int albumId = 0;
		String result = readStream(inputStream);
		mImages = new Vector <ContentValues>(VECTOR_INITIAL_SIZE);
		mPages = new Vector <ContentValues>(VECTOR_INITIAL_SIZE);
		ContentValues pages = new ContentValues();
		if (result != null) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				if (jsonObject.has("album_id")) {
					pages.put(DataProviderContract.ALBUM_ID_COLUMN, jsonObject.getInt("album_id"));
					albumId = jsonObject.getInt("album_id");
					pages.put(DataProviderContract.CATEGORY_ID, 2);

				}
				if (jsonObject.has("total_pages")) {
					pages.put(DataProviderContract.TOTAL_PAGES, jsonObject.getInt("total_pages"));

				}
				if (jsonObject.has("result")) {
					JSONArray jsonArray = jsonObject.getJSONArray("result");

					for (int i = 0; i < jsonArray.length(); i++) {
						item = new ContentValues();
						item.put(DataProviderContract.ALBUM_ID_COLUMN, pages.getAsInteger(DataProviderContract.ALBUM_ID_COLUMN));
						item.put(DataProviderContract.CATEGORY_ID, pages.getAsInteger(DataProviderContract.CATEGORY_ID));

						JSONObject resultJsonObject = jsonArray.getJSONObject(i);
						if (resultJsonObject.has("video_id")) {
							int photoId = resultJsonObject.getInt("video_id");
							item.put(DataProviderContract.ROW_ID, photoId);
						}
						if (resultJsonObject.has("video_url")) {
							String photoUrl = resultJsonObject.getString("video_url");
							item.put(DataProviderContract.URL, photoUrl);
						}
						if (resultJsonObject.has("video_thumb")) {
							String photoThumb = resultJsonObject.getString("video_thumb");
							item.put(DataProviderContract.THUMB_IMAGE_URL, photoThumb);
						}
						mImages.add(item);
					}
					mPages.add(pages);
				}
			}
			catch (JSONException e) {
				Log.i(TAG, e.toString());
			}
		}
	}

	public void parseNewsJson (InputStream inputStream, BroadcastNotifier mBroadcaster, int categoryId) {

		String result = readStream(inputStream);
		mImages = new Vector <ContentValues>(VECTOR_INITIAL_SIZE);
		JSONArray jsonArray;
		try {
			jsonArray = new JSONArray(result);
			for (int i = 0; i < jsonArray.length(); i++) {
				item = new ContentValues();
				// regionalNewsItems = new Items();
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				if (jsonObject.has("news_id")) {
					int id = jsonObject.getInt("news_id");
					item.put(DataProviderContract.NEWS_ID, id);
				}
				if (jsonObject.has("news_title")) {
					String title = jsonObject.getString("news_title");
					item.put(DataProviderContract.NEWS_TITLE, title);
				}
				if (jsonObject.has("news_url")) {
					String url = jsonObject.getString("news_url");
					item.put(DataProviderContract.NEWS_URL, url);
				}
				item.put(DataProviderContract.NEWS_CATEGORY, categoryId);
				mImages.add(item);
			}
			// testing for updates
			/*
			 * item = new ContentValues(); item.put(DataProviderContract.NEWS_ID, 111111); item.put(DataProviderContract.NEWS_TITLE, "venky................."); item.put(DataProviderContract.NEWS_URL, "dynamic updates"); item.put(DataProviderContract.NEWS_CATEGORY, 1); mImages.add(item);
			 * 
			 * item = new ContentValues(); item.put(DataProviderContract.NEWS_ID, 22222); item.put(DataProviderContract.NEWS_TITLE, "phani................."); item.put(DataProviderContract.NEWS_URL, "dynamic updates"); item.put(DataProviderContract.NEWS_CATEGORY, 2); mImages.add(item);
			 */
		}
		catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public void parseDownloadJson (InputStream inputStream, BroadcastNotifier mBroadcaster) {
		String result = readStream(inputStream);
		mImages = new Vector <ContentValues>(VECTOR_INITIAL_SIZE);
		// mPages = new Vector <ContentValues>(VECTOR_INITIAL_SIZE);

		ContentValues pages = new ContentValues();
		if (result != null) {
			int noofpages = 0;
			try {
				JSONObject object = new JSONObject(result);

				if (object.has("album_id")) {
					pages.put(DataProviderContract.ALBUM_ID_COLUMN, 0);
					pages.put(DataProviderContract.CATEGORY_ID, 3);

				}
				if (object.has("total_pages")) {
					pages.put(DataProviderContract.TOTAL_PAGES, object.getInt("total_pages"));
					noofpages = object.getInt("total_pages");

				}

				JSONArray jsonArray = new JSONArray(object.getString("result"));

				for (int i = 0; i < jsonArray.length(); i++) {

					item = new ContentValues();
					// item.put(DataProviderContract.CATEGORY_ID, pages.getAsInteger(DataProviderContract.CATEGORY_ID));

					JSONObject jsonObject = jsonArray.getJSONObject(i);

					item.put(DataProviderContract.DOWNLOAD_IMAGE_NO_OF_PAGES, noofpages);

					if (jsonObject.has("photo_id")) {
						int id = jsonObject.getInt("photo_id");
						item.put(DataProviderContract.DOWNLOAD_IMAGE_ID, id);
					}
					if (jsonObject.has("photo_url")) {
						String photo_url = jsonObject.getString("photo_url");

						item.put(DataProviderContract.DOWNLOAD_IMAGE_URL, photo_url);
					}
					if (jsonObject.has("photo_thumb")) {
						String photo_thumb = jsonObject.getString("photo_thumb");

						item.put(DataProviderContract.DOWNLOAD_IMAGE_THUMB_URL, photo_thumb);
					}

					mImages.add(item);
				}
			}
			catch (JSONException e) {
				e.printStackTrace();

			}

		}

	}

	public void parseTeamsLogoJson (InputStream inputStream, BroadcastNotifier mBroadcaster) {

		String result = readStream(inputStream);
		System.out.println("kranthi teams logo Result " + result);

		mImages = new Vector <ContentValues>(VECTOR_INITIAL_SIZE);

		if (result != null) {
			try {
				JSONArray jsonArray = new JSONArray(result);
				for (int i = 0; i < jsonArray.length(); i++) {
					item = new ContentValues();
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					if (!jsonObject.isNull("team_id")) {
						if (jsonObject.has("team_id")) {
							int teamId = jsonObject.getInt("team_id");
							item.put(DataProviderContract.TEAM_ID_COLUMN, teamId);
						}
					}
					if (!jsonObject.isNull("team_name")) {
						if (jsonObject.has("team_name")) {
							String teamName = jsonObject.getString("team_name");
							item.put(DataProviderContract.TEAM_NAME_COLUMN, teamName.trim());

						}
					}
					if (!jsonObject.isNull("team_logo")) {
						if (jsonObject.has("team_logo")) {
							String teamLogoThumbUrl = jsonObject.getString("team_logo");
							item.put(DataProviderContract.TEAM_LOGO_IMAGE_URL_COLUMN, teamLogoThumbUrl);
						}
					}
					if (!jsonObject.isNull("team_banner")) {
						if (jsonObject.has("team_banner")) {
							String teamBannerUrl = jsonObject.getString("team_banner");
							item.put(DataProviderContract.TEAM_BANNER_IMAGE_URL_COLUMN, teamBannerUrl);
						}
					}
					mImages.add(item);
				}
			}
			catch (JSONException e) {
				Log.i(TAG, "Photo album json parsing exception");
			}
		}
	}
	public void parseTeamMembersJson (InputStream inputStream, BroadcastNotifier mBroadcaster) {

		String result = readStream(inputStream);
		System.out.println("kranthi teams members Result " + result);

		mImages = new Vector <ContentValues>(VECTOR_INITIAL_SIZE);

		if (result != null) {
			try {
				JSONArray jsonArray = new JSONArray(result);
				for (int i = 0; i < jsonArray.length(); i++) {
					item = new ContentValues();
					JSONObject jsonObject = jsonArray.getJSONObject(i);

					if (!jsonObject.isNull("person_id")) {
						if (jsonObject.has("person_id")) {
							int teamPersonId = jsonObject.getInt("person_id");
							item.put(DataProviderContract.TEAM_PERSON_ID_COLUMN, teamPersonId);
						}
					}
					if (!jsonObject.isNull("person_name")) {
						if (jsonObject.has("person_name")) {
							String teamPersonName = jsonObject.getString("person_name");
							item.put(DataProviderContract.TEAM_PERSON_NAME_COLUMN, teamPersonName.trim());

						}
					}
					if (!jsonObject.isNull("s3_person_image")) {
						if (jsonObject.has("s3_person_image")) {
							String teamPersonThumbUrl = jsonObject.getString("s3_person_image");
							item.put(DataProviderContract.TEAM_MEMBER_IMAGE_URL_COLUMN, teamPersonThumbUrl);
						}
					}
					if (!jsonObject.isNull("team_name")) {
						if (jsonObject.has("team_name")) {
							String teamName = jsonObject.getString("team_name");
							item.put(DataProviderContract.TEAM_NAME_MEMBER_COLUMN, teamName.trim());
						}
					}
					if (!jsonObject.isNull("person_roles")) {
						if (jsonObject.has("person_roles")) {
							String teamPersonalRole = jsonObject.getString("person_roles");
							String[] roleArray;
							if (teamPersonalRole.contains(",")) {
								roleArray = teamPersonalRole.split(",");
								teamPersonalRole = String.format("%s (%s)", roleArray[0].trim(), roleArray[1].trim());

							}
							System.out.println("kranthi role parser:" + " " + teamPersonalRole);
							item.put(DataProviderContract.TEAM_PERSON_ROLE_COLUMN, teamPersonalRole);
						}
					}
					mImages.add(item);
				}
			}
			catch (JSONException e) {
				Log.i(TAG, "Photo album json parsing exception");
			}
		}
	}
}
