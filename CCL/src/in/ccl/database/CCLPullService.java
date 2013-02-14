package in.ccl.database;

import in.ccl.model.Items;
import in.ccl.ui.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import org.apache.http.HttpStatus;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;

public class CCLPullService extends IntentService {

	// Used to write to the system log from this class.
	public static final String LOG_TAG = "RSSPullService";

	// Defines and instantiates an object for handling status updates.
	private BroadcastNotifier mBroadcaster = new BroadcastNotifier(this);

	/**
	 * An IntentService must always have a constructor that calls the super constructor. The string supplied to the super constructor is used to give a name to the IntentService's background thread.
	 */
	public CCLPullService () {
		super("CCLPullService");
	}

	@Override
	protected void onHandleIntent (Intent workIntent) {

		Vector <ContentValues> imageValues = null;
		int updatedRows = 0;
		// Gets a URL to read from the incoming Intent's "data" value
		String localUrlString = workIntent.getDataString();
		// System.out.println("Url " + localUrlString);
		String compareKey = null;
		if (workIntent.hasExtra("KEY")) {
			compareKey = workIntent.getStringExtra("KEY");
		}
		else {
			compareKey = Uri.parse(localUrlString).getLastPathSegment();
		}

		// Creates a projection to use in querying the modification date table in the provider.
		final String[] dateProjection = new String[] { DataProviderContract.ROW_ID, DataProviderContract.DATA_DATE_COLUMN };

		// A URL that's local to this method
		URL localURL;

		// A cursor that's local to this method.
		Cursor cursor = null;

		/*
		 * A block that tries to connect to the Picasa featured picture URL passed as the "data" value in the incoming Intent. The block throws exceptions (see the end of the block).
		 */
		try {

			// Convert the incoming data string to a URL.
			localURL = new URL(localUrlString);

			/*
			 * Tries to open a connection to the URL. If an IO error occurs, this throws an IOException
			 */
			URLConnection localURLConnection = localURL.openConnection();

			// If the connection is an HTTP connection, continue
			if ((localURLConnection instanceof HttpURLConnection)) {

				// Broadcasts an Intent indicating that processing has started.
				if (mBroadcaster != null) {
					mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_STARTED, null);
				}
				// Casts the connection to a HTTP connection
				HttpURLConnection localHttpURLConnection = (HttpURLConnection) localURLConnection;

				// Sets the user agent for this request.
				localHttpURLConnection.setRequestProperty("User-Agent", Constants.USER_AGENT);

				/*
				 * Queries the content provider to see if this URL was read previously, and when. The content provider throws an exception if the URI is invalid.
				 */
				cursor = getContentResolver().query(DataProviderContract.DATE_TABLE_CONTENTURI, dateProjection, null, null, null);

				// Flag to indicate that new metadata was retrieved
				boolean newMetadataRetrieved;

				/*
				 * Tests to see if the table contains a modification date for the URL
				 */
				if (null != cursor && cursor.moveToFirst()) {

					// Find the URL's last modified date in the content provider
					long storedModifiedDate = cursor.getLong(cursor.getColumnIndex(DataProviderContract.DATA_DATE_COLUMN));

					/*
					 * If the modified date isn't 0, sets another request property to ensure that data is only downloaded if it has changed since the last recorded modification date. Formats the date according to the RFC1123 format.
					 */
					if (0 != storedModifiedDate) {
						localHttpURLConnection.setRequestProperty("If-Modified-Since", org.apache.http.impl.cookie.DateUtils.formatDate(new Date(storedModifiedDate), org.apache.http.impl.cookie.DateUtils.PATTERN_RFC1123));
					}

					// Marks that new metadata does not need to be retrieved
					newMetadataRetrieved = false;

				}
				else {

					/*
					 * No modification date was found for the URL, so newmetadata has to be retrieved.
					 */
					newMetadataRetrieved = true;

				}

				// Reports that the service is about to connect to the RSS feed
				if (mBroadcaster != null) {
					mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_CONNECTING, null);
				}
				// Gets a response code from the RSS server
				int responseCode = localHttpURLConnection.getResponseCode();

				switch (responseCode) {

					// If the response is OK
					case HttpStatus.SC_OK:

						// Gets the last modified data for the URL
						long lastModifiedDate = localHttpURLConnection.getLastModified();

						// Reports that the service is parsing
						if (mBroadcaster != null) {
							mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_PARSING, null);
						}
						/*
						 * Instantiates a pull parser and uses it to parse XML from the RSS feed. The mBroadcaster argument send a broadcaster utility object to the parser.
						 */
						JSONPullParser localDataPullParser = new JSONPullParser();

						if (compareKey.equals("slidersv2") || compareKey.equals("update-banner")) {
							localDataPullParser.parseBannerJson(localURLConnection.getInputStream());
						}
						else if (compareKey.equals("albums") || compareKey.equals("update-photos")) {
							localDataPullParser.parsePhotoAlbumJson(localURLConnection.getInputStream());
						}
						else if (compareKey.equals("videoalbums") || compareKey.equals("update-videos")) {
							localDataPullParser.parseVideoAlbumJson(localURLConnection.getInputStream());
						}
						else if (compareKey.equals("photos") || compareKey.equals("photos_pages") || compareKey.equals("banner-photos") || compareKey.equals("photo_updates")) {
							localDataPullParser.parsePhotoJson(localURLConnection.getInputStream());
						}
						else if (compareKey.equals("videos") || compareKey.equals("videos_pages") || compareKey.equals("videos_updates")) {
							localDataPullParser.parseVideoJson(localURLConnection.getInputStream());
						}
						else if (compareKey.equals("downloads") || compareKey.equals("download_updates")) {
							localDataPullParser.parseDownloadJson(localURLConnection.getInputStream());
						}
						else if (compareKey.equals("teams")) {
							localDataPullParser.parseTeamsLogoJson(localURLConnection.getInputStream());
						}
						else if (compareKey.equals("team_members") || compareKey.equals("team_members_updates")) {
							localDataPullParser.parseTeamMembersJson(localURLConnection.getInputStream());
						}

						// Reports that the service is now writing data to the content provider.
						if (mBroadcaster != null) {
							mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_WRITING, null);
						}
						// Gets image data from the parser
						imageValues = localDataPullParser.getparsedData();
						// Stores the number of images
						int imageVectorSize = imageValues.size();

						// Creates one ContentValues for each image
						ContentValues[] imageValuesArray = new ContentValues[imageVectorSize];

						imageValuesArray = imageValues.toArray(imageValuesArray);

						/*
						 * Stores the image data in the content provider. The content provider throws an exception if the URI is invalid.
						 */
						if (compareKey.equals("slidersv2") || compareKey.equals("update-banner")) {
							updatedRows = 0;
							updatedRows = getContentResolver().bulkInsert(DataProviderContract.BANNERURL_TABLE_CONTENTURI, imageValuesArray);
						}
						else if (compareKey.equals("albums") || compareKey.equals("update-photos")) {
							updatedRows = 0;
							updatedRows = getContentResolver().bulkInsert(DataProviderContract.PHOTO_ALBUM_TABLE_CONTENTURI, imageValuesArray);
						}
						else if (compareKey.equals("videoalbums") || compareKey.equals("update-videos")) {
							updatedRows = 0;
							updatedRows = getContentResolver().bulkInsert(DataProviderContract.VIDEO_ALBUM_TABLE_CONTENTURI, imageValuesArray);
						}

						else if (compareKey.equals("photos") || compareKey.equals("photos_pages") || compareKey.equals("banner-photos") || compareKey.equals("photo_updates")) {
							updatedRows = 0;
							updatedRows = getContentResolver().bulkInsert(DataProviderContract.RAW_TABLE_CONTENTURI, imageValuesArray);

							// Gets image data from the parser
							Vector <ContentValues> pageValues = localDataPullParser.getPages();
							if (pageValues != null) {
								// Stores the number of images
								int pageVectorSize = pageValues.size();

								// Creates one ContentValues for each image
								ContentValues[] pageValuesArray = new ContentValues[pageVectorSize];

								pageValuesArray = pageValues.toArray(pageValuesArray);
								try {
									getContentResolver().bulkInsert(DataProviderContract.PAGES_TABLE_CONTENTURI, pageValuesArray);
								}
								catch (SQLiteConstraintException e) {
								}
							}
						}
						else if (compareKey.equals("videos") || compareKey.equals("videos_pages") || compareKey.equals("videos_updates")) {
							updatedRows = 0;
							updatedRows = getContentResolver().bulkInsert(DataProviderContract.RAW_TABLE_CONTENTURI, imageValuesArray);
							// Gets image data from the parser
							Vector <ContentValues> pageValues = localDataPullParser.getPages();

							if (pageValues != null) {
								// Stores the number of images
								int pageVectorSize = pageValues.size();

								// Creates one ContentValues for each image
								ContentValues[] pageValuesArray = new ContentValues[pageVectorSize];

								pageValuesArray = pageValues.toArray(pageValuesArray);

								getContentResolver().bulkInsert(DataProviderContract.PAGES_TABLE_CONTENTURI, pageValuesArray);
							}

						}
						else if (compareKey.equals("downloads") || compareKey.equals("download_updates")) {
							getContentResolver().bulkInsert(DataProviderContract.DOWNLOAD_IMAGE_TABLE_CONTENTURI, imageValuesArray);
							Vector <ContentValues> pageValues = localDataPullParser.getPages();

							if (pageValues != null) {
								// Stores the number of images
								int pageVectorSize = pageValues.size();
								// Creates one ContentValues for each image
								ContentValues[] pageValuesArray = new ContentValues[pageVectorSize];
								pageValuesArray = pageValues.toArray(pageValuesArray);
								getContentResolver().bulkInsert(DataProviderContract.PAGES_TABLE_CONTENTURI, pageValuesArray);
							}

						}
						else if (compareKey.equals("teams")) {
							getContentResolver().bulkInsert(DataProviderContract.TEAMS_LOGO_TABLE_CONTENTURI, imageValuesArray);
							Cursor teamCursor = getContentResolver().query(DataProviderContract.TEAMS_LOGO_TABLE_CONTENTURI, null, null, null, null);
							if (teamCursor != null && teamCursor.getCount() > 0) {
								if (teamCursor.moveToFirst()) {
									do {
										Intent mServiceIntent = new Intent(CCLPullService.this, CCLPullService.class).setData(Uri.parse(getResources().getString(R.string.team_members_url) + teamCursor.getInt(0)));
										mServiceIntent.putExtra("KEY", "team_members_updates");
										startService(mServiceIntent);
									}
									while (teamCursor.moveToNext());
								}
							}
							if (teamCursor != null) {
								teamCursor.close();
							}
						}

						else {
							if (compareKey.equals("team_members") || compareKey.equals("team_members_updates")) {
								updatedRows = 0;
								try {

									updatedRows = getContentResolver().bulkInsert(DataProviderContract.TEAM_MEMBERS_TABLE_CONTENTURI, imageValuesArray);
									System.out.println("updatedRows " + updatedRows);
								}
								catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						// Creates another ContentValues for storing date information
						ContentValues dateValues = new ContentValues();

						// Adds the URL's last modified date to the ContentValues
						dateValues.put(DataProviderContract.DATA_DATE_COLUMN, lastModifiedDate);

						if (newMetadataRetrieved) {

							// No previous metadata existed, so insert the data
							getContentResolver().insert(DataProviderContract.DATE_TABLE_CONTENTURI, dateValues);
						}
						else {
							// Previous metadata existed, so update it.
							getContentResolver().update(DataProviderContract.DATE_TABLE_CONTENTURI, dateValues, DataProviderContract.ROW_ID + "=" + cursor.getString(cursor.getColumnIndex(DataProviderContract.ROW_ID)), null);
						}
						break;
					default:
						break;
				}

				// Reports that the feed retrieval is complete.
				if (updatedRows > 0 && mBroadcaster != null) {
					if (compareKey.equals("slidersv2")) {
						mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_BANNER_COMPLETE, null);
					}
					else if (compareKey.equals("albums")) {
						mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_PHOTO_ALBUM_COMPLETE, null);
					}
					else if (compareKey.equals("videoalbums")) {
						mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_VIDEO_ALBUM_COMPLETE, null);
					}
					else if (compareKey.equals("photos")) {
						mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_PHOTO_COMPLETE, null);
					}
					else if (compareKey.equals("videos")) {
						mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_VIDEO_COMPLETE, null);
					}
					else if (compareKey.equals("update-banner")) {
						// database updated, should notify home activity to upate banner items.
						mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_BANNER_UPDATES_COMPLETE, null);
					}
					else if (compareKey.equals("update-photos")) {
						// database updated, should notify home activity to update photos items.
						mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_PHOTO_ALBUM_UPDATES_COMPLETE, null);

					}
					else if (compareKey.equals("update-videos")) {
						mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_VIDEO_ALBUM_UPDATES_COMPLETE, null);
					}

					else if (compareKey.equals("photos_pages")) {
						mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_PHOTO_PAGES_DOWNLOAD_COMPLETE, getArrayOfItems(imageValues));
					}
					else if (compareKey.equals("videos_pages")) {
						mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_VIDEO_PAGES_DOWNLOAD_COMPLETE, getArrayOfItems(imageValues));
					}
					else if (compareKey.equals("banner-photos")) {
						mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_BANNER_PAGES_DOWNLOAD_COMPLETE, null);
					}
					else if (compareKey.equals("videos_updates")) {
						mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_VIDEO_UPDATES_COMPLETE, null);
					}
					else if (compareKey.equals("photo_updates")) {
						mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_PHOTO_UPDATES_COMPLETE, null);
					}
					else if (compareKey.equals("downloads")) {
						mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_DOWNLOAD_IMAGE_COMPLETE, null);
					}

					else if (compareKey.equals("teams")) {
						mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_TEAM_LOGO_COMPLETE, null);
					}
					else if (compareKey.equals("team_members")) {
						System.out.println("kranthi completeteam_members");
						mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_TEAM_MEMBERS_COMPLETE, null);

					}
					else if (compareKey.equals("download_updates")) {
						mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_UPDATE_DOWNLOAD_IMAGE_COMPLETE, null);
					}
				}
			}

			// Handles possible exceptions
		}
		catch (MalformedURLException localMalformedURLException) {

			localMalformedURLException.printStackTrace();

		}
		catch (IOException localIOException) {

			localIOException.printStackTrace();

		}
		finally {

			// If an exception occurred, close the cursor to prevent memory leaks.
			if (null != cursor) {
				cursor.close();
			}
		}
	}

	private ArrayList <Items> getArrayOfItems (Vector <ContentValues> imageValues) {

		ArrayList <in.ccl.model.Items> list = new ArrayList <in.ccl.model.Items>();
		if (imageValues != null) {
			int imageVectorSize = imageValues.size();
			// Creates one ContentValues for each image
			ContentValues[] imageValuesArray = new ContentValues[imageVectorSize];
			imageValuesArray = imageValues.toArray(imageValuesArray);

			for (int i = 0; i < imageValuesArray.length; i++) {
				ContentValues val = imageValuesArray[i];
				Items item = new Items();
				if (val.containsKey(DataProviderContract.ALBUM_ID_COLUMN)) {
					item.setAlbumId(val.getAsInteger(DataProviderContract.ALBUM_ID_COLUMN));
				}
				if (val.containsKey(DataProviderContract.TOTAL_PAGES)) {
					item.setNumberOfPages(val.getAsInteger(DataProviderContract.TOTAL_PAGES));
				}
				if (val.containsKey(DataProviderContract.ROW_ID)) {
					item.setId(val.getAsInteger(DataProviderContract.ROW_ID));
				}
				if (val.containsKey(DataProviderContract.URL)) {
					item.setPhotoOrVideoUrl(val.getAsString(DataProviderContract.URL));
				}
				if (val.containsKey(DataProviderContract.THUMB_IMAGE_URL)) {
					item.setThumbUrl(val.getAsString(DataProviderContract.THUMB_IMAGE_URL));
				}
				list.add(item);
			}
		}
		return list;
	}

}
