package in.ccl.database;

import java.util.Vector;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;

public class DisplayActivity extends FragmentActivity {

	// Intent for starting the IntentService that downloads the Picasa featured picture RSS feed
	private Intent mServiceIntent;

	private static final String CCL_BANNER_URL = "http://ec2-23-21-38-107.compute-1.amazonaws.com/restv2/sliders";

	private static final String CCL_PHOTO_ALBUM_URL = "http://ec2-23-21-38-107.compute-1.amazonaws.com/restv2/albums";

	private static final String CCL_VIDO_ALBUM_URL = "http://ec2-23-21-38-107.compute-1.amazonaws.com/restv2/videoalbums";

	private static final String RAW_PHOTO_URL = "http://ec2-23-21-38-107.compute-1.amazonaws.com/restv2/photos/1";

	private static final String RAW_VIDEO_URL = "http://ec2-23-21-38-107.compute-1.amazonaws.com/restv2/videos/2";

	@Override
	protected void onCreate (Bundle stateBundle) {
		super.onCreate(stateBundle);
	//	setContentView(R.layout.activity_display);

		// The filter's action is BROADCAST_ACTION
		IntentFilter statusIntentFilter = new IntentFilter(Constants.BROADCAST_ACTION);

		// Sets the filter's category to DEFAULT
		statusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

		// Instantiates a new DownloadStateReceiver
		DownloadStateReceiver mDownloadStateReceiver = new DownloadStateReceiver();

		// Registers the DownloadStateReceiver and its intent filters
		LocalBroadcastManager.getInstance(this).registerReceiver(mDownloadStateReceiver, statusIntentFilter);

		mServiceIntent = new Intent(this, CCLPullService.class).setData(Uri.parse(CCL_BANNER_URL));
		startService(mServiceIntent);

		mServiceIntent = new Intent(this, CCLPullService.class).setData(Uri.parse(CCL_PHOTO_ALBUM_URL));
		startService(mServiceIntent);
		mServiceIntent = new Intent(this, CCLPullService.class).setData(Uri.parse(CCL_VIDO_ALBUM_URL));
		startService(mServiceIntent);
		Cursor cursor = getContentResolver().query(DataProviderContract.CATEGORY_TABLE_CONTENTURI, null, null, null, null);
		if (cursor.getCount() <= 0) {
			Vector <ContentValues> mValues = new Vector <ContentValues>(JSONPullParser.VECTOR_INITIAL_SIZE);
			ContentValues values = new ContentValues();
			values.put(DataProviderContract.ROW_ID, 1);
			values.put(DataProviderContract.CATEGORY_TITLE, "photos");
			mValues.add(values);
			values = new ContentValues();
			values.put(DataProviderContract.ROW_ID, 2);
			values.put(DataProviderContract.CATEGORY_TITLE, "videos");
			mValues.add(values);
			// Stores the number of images
			int imageVectorSize = mValues.size();

			// Creates one ContentValues for each image
			ContentValues[] imageValuesArray = new ContentValues[imageVectorSize];

			imageValuesArray = mValues.toArray(imageValuesArray);

			getContentResolver().bulkInsert(DataProviderContract.CATEGORY_TABLE_CONTENTURI, imageValuesArray);
		}
		mServiceIntent = new Intent(this, CCLPullService.class);
		mServiceIntent.putExtra("KEY", "photos");
		mServiceIntent.setData(Uri.parse(RAW_PHOTO_URL));
		startService(mServiceIntent);

		mServiceIntent = new Intent(this, CCLPullService.class);
		mServiceIntent.putExtra("KEY", "videos");
		mServiceIntent.setData(Uri.parse(RAW_VIDEO_URL));
		startService(mServiceIntent);
		
		

	}

	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_display, menu);
		return true;
	}

	/**
	 * This class uses the BroadcastReceiver framework to detect and handle status messages from the service that downloads URLs.
	 */
	private class DownloadStateReceiver extends BroadcastReceiver {

		private static final String CLASS_TAG = "DisplayActivity";

		private DownloadStateReceiver () {
			// prevents instantiation by other packages.
		}

		/**
		 * 
		 * This method is called by the system when a broadcast Intent is matched by this class' intent filters
		 * 
		 * @param context An Android context
		 * @param intent The incoming broadcast Intent
		 */
		@Override
		public void onReceive (Context context, Intent intent) {

			/*
			 * Gets the status from the Intent's extended data, and chooses the appropriate action
			 */
			switch (intent.getIntExtra(Constants.EXTENDED_DATA_STATUS, Constants.STATE_ACTION_COMPLETE)) {

				// Logs "started" state
				case Constants.STATE_ACTION_STARTED:
					if (Constants.LOGD) {

						Log.d(CLASS_TAG, "State: STARTED");
					}
					break;
				// Logs "connecting to network" state
				case Constants.STATE_ACTION_CONNECTING:
					if (Constants.LOGD) {

						Log.d(CLASS_TAG, "State: CONNECTING");
					}
					break;
				// Logs "parsing the RSS feed" state
				case Constants.STATE_ACTION_PARSING:
					if (Constants.LOGD) {

						Log.d(CLASS_TAG, "State: PARSING");
					}
					break;
				// Logs "Writing the parsed data to the content provider" state
				case Constants.STATE_ACTION_WRITING:
					if (Constants.LOGD) {
						Log.d(CLASS_TAG, "State: WRITING");
					}
					break;
				// Starts displaying data when the RSS download is complete
				case Constants.STATE_ACTION_COMPLETE:
					// Logs the status
					if (Constants.LOGD) {
						Log.d(CLASS_TAG, "State: COMPLETE");
					}
					break;
				default:
					break;
			}
		}
	}

}
