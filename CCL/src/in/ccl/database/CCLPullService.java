package in.ccl.database;

import in.ccl.model.Items;

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
		// Gets a URL to read from the incoming Intent's "data" value
		String localUrlString = workIntent.getDataString();
		System.out.println("Url " + localUrlString);
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
				mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_STARTED, null);

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
				mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_CONNECTING, null);

				// Gets a response code from the RSS server
				int responseCode = localHttpURLConnection.getResponseCode();

				switch (responseCode) {

					// If the response is OK
					case HttpStatus.SC_OK:

						// Gets the last modified data for the URL
						long lastModifiedDate = localHttpURLConnection.getLastModified();

						// Reports that the service is parsing
						mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_PARSING, null);

						/*
						 * Instantiates a pull parser and uses it to parse XML from the RSS feed. The mBroadcaster argument send a broadcaster utility object to the parser.
						 */
						JSONPullParser localDataPullParser = new JSONPullParser();

						if (compareKey.equals("slidersv2")) {
							localDataPullParser.parseBannerJson(localURLConnection.getInputStream(), mBroadcaster);
						}
						else if (compareKey.equals("albums")) {
							localDataPullParser.parsePhotoAlbumJson(localURLConnection.getInputStream(), mBroadcaster);
						}
						else if (compareKey.equals("videoalbums")) {
							localDataPullParser.parseVideoAlbumJson(localURLConnection.getInputStream(), mBroadcaster);
						}
						else if (compareKey.equals("photos")) {
							localDataPullParser.parsePhotoJson(localURLConnection.getInputStream(), mBroadcaster);
						}
						else if (compareKey.equals("videos")) {
							localDataPullParser.parseVideoJson(localURLConnection.getInputStream(), mBroadcaster);
						}

						// Reports that the service is now writing data to the content provider.
						mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_WRITING, null);

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
						if (compareKey.equals("slidersv2")) {

							getContentResolver().bulkInsert(DataProviderContract.PICTUREURL_TABLE_CONTENTURI, imageValuesArray);
						}
						else if (compareKey.equals("albums")) {
							getContentResolver().bulkInsert(DataProviderContract.PHOTO_ALBUM_TABLE_CONTENTURI, imageValuesArray);
						}
						else if (compareKey.equals("videoalbums")) {
							getContentResolver().bulkInsert(DataProviderContract.VIDEO_ALBUM_TABLE_CONTENTURI, imageValuesArray);
						}
						else if (compareKey.equals("photos")) {
							try {
								int result = getContentResolver().bulkInsert(DataProviderContract.RAW_TABLE_CONTENTURI, imageValuesArray);
								if (result == -1) {
									return;
								}
							}
							catch (SQLiteConstraintException e) {
								// TODO: handle exception
							}
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
						else if (compareKey.equals("videos")) {
							getContentResolver().bulkInsert(DataProviderContract.RAW_TABLE_CONTENTURI, imageValuesArray);
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
				mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_COMPLETE, null);
				if (compareKey.equals("sliders")) {
					mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_BANNER_COMPLETE, null);
				}
				else if (compareKey.equals("albums")) {
					mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_PHOTO_ALBUM_COMPLETE, null);
				}
				else if (compareKey.equals("videoalbums")) {
					mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_VIDEO_ALBUM_COMPLETE, null);
				}
				else if (compareKey.equals("photos")) {
					mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_PHOTO_COMPLETE, getArrayOfItems(imageValues));
				}
				else if (compareKey.equals("videos")) {
					mBroadcaster.broadcastIntentWithState(Constants.STATE_ACTION_VIDEO_COMPLETE, getArrayOfItems(imageValues));
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
