package in.ccl.ui;

import in.ccl.database.BannerCursor;
import in.ccl.database.CCLPullService;
import in.ccl.database.Constants;
import in.ccl.database.DataProviderContract;
import in.ccl.database.DisplayActivity;
import in.ccl.database.JSONPullParser;
import in.ccl.database.PhotoAlbumCurosr;
import in.ccl.database.VideoAlbumCursor;
import in.ccl.helper.Util;
import in.ccl.logging.Logger;
import in.ccl.logging.ParadigmExceptionHandler;
import in.ccl.model.Items;
import in.ccl.util.AppPropertiesUtil;

import java.util.ArrayList;
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
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Initial load of banner, photo-gallery and video data(home screen) while splashing the screen. If yes, navigate to Home screen, otherwise shows network dialog.
 * 
 * @author Rajesh Babu | Paradigm Creatives
 */

public class SplashScreenActivity extends FragmentActivity {

	private Animation animationRotateCenter;

	private ImageView floatingLogoImage;

	private TextView txtSplashLoading;

	private boolean isInitialDataLoaded;

	public enum RequestType {
		NO_REQUEST, BANNER_REQUEST, PHOTO_ALBUMREQUEST, VIDEO_ALBUMREQUEST;
	}

	RequestType mRequestType = RequestType.NO_REQUEST;

	/**
	 * Called when the activity is first created.
	 */
	// Intent for starting the IntentService that downloads the Picasa featured picture RSS feed
	private Intent mServiceIntent;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Setting the default layout
		setContentView(R.layout.splash_screen);
		// getting reference of ccl logo for animated splash screen
		floatingLogoImage = (ImageView) findViewById(R.id.logo_image);
		// getting reference of loading text in splash screen
		txtSplashLoading = (TextView) findViewById(R.id.txt_splash_loading);
		// creating rotate animation object by loading rotate_center anim properties.
		animationRotateCenter = AnimationUtils.loadAnimation(this, R.anim.rotate_center);
		// adding rotation animation lister to animation object to know the start and end of the animations.
		animationRotateCenter.setAnimationListener(rotationAnimationListener);
		// starting the animation for ccl logo on splash screen.
		floatingLogoImage.startAnimation(animationRotateCenter);
		// initializing logs and application utilities.
		initAppComponents(SplashScreenActivity.this);

		Util.setTextFont(SplashScreenActivity.this, txtSplashLoading);

		// The filter's action is BROADCAST_ACTION
		IntentFilter statusIntentFilter = new IntentFilter(Constants.BROADCAST_ACTION);

		// Sets the filter's category to DEFAULT
		statusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

		// Instantiates a new DownloadStateReceiver
		DownloadStateReceiver mDownloadStateReceiver = new DownloadStateReceiver();

		// Registers the DownloadStateReceiver and its intent filters
		LocalBroadcastManager.getInstance(this).registerReceiver(mDownloadStateReceiver, statusIntentFilter);
		Cursor cursor = getContentResolver().query(DataProviderContract.PICTUREURL_TABLE_CONTENTURI, null, null, null, null);
		if (cursor.getCount() > 0) {
			ArrayList <Items> bannerItems = BannerCursor.getItems(cursor);
			cursor = getContentResolver().query(DataProviderContract.PHOTO_ALBUM_TABLE_CONTENTURI, null, null, null, null);
			ArrayList <Items> photoAlbumItems = PhotoAlbumCurosr.getItems(cursor);
			cursor = getContentResolver().query(DataProviderContract.VIDEO_ALBUM_TABLE_CONTENTURI, null, null, null, null);
			ArrayList <Items> videoAlbumItems = VideoAlbumCursor.getItems(cursor);
			callHomeIntent(bannerItems, photoAlbumItems, videoAlbumItems);
		}
		else {
			if (cursor != null) {
				cursor.close();
			}
			if (Util.getInstance().isOnline(SplashScreenActivity.this)) {
				mServiceIntent = new Intent(this, CCLPullService.class).setData(Uri.parse(getResources().getString(R.string.banner_url)));
				startService(mServiceIntent);

				mServiceIntent = new Intent(this, CCLPullService.class).setData(Uri.parse(getResources().getString(R.string.photo_album_url)));
				startService(mServiceIntent);

				mServiceIntent = new Intent(this, CCLPullService.class).setData(Uri.parse(getResources().getString(R.string.video_album_url)));
				startService(mServiceIntent);

				cursor = getContentResolver().query(DataProviderContract.CATEGORY_TABLE_CONTENTURI, null, null, null, null);
				if (cursor.getCount() <= 0) {
					// Initilization of Category table.
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

			}
			else {
				animationRotateCenter = null;
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
			}
		}
	}

	private void callHomeIntent (ArrayList <Items> bannerItems, ArrayList <Items> photoAlbumItems, ArrayList <Items> videoAlbumItems) {
		Intent homeActivityIntent = new Intent(SplashScreenActivity.this, HomeActivity.class);
		homeActivityIntent.putParcelableArrayListExtra(in.ccl.util.Constants.EXTRA_BANNER_KEY, bannerItems);
		homeActivityIntent.putParcelableArrayListExtra(in.ccl.util.Constants.EXTRA_PHOTO_KEY, photoAlbumItems);
		homeActivityIntent.putParcelableArrayListExtra(in.ccl.util.Constants.EXTRA_VIDEO_KEY, videoAlbumItems);
		SplashScreenActivity.this.startActivityForResult(homeActivityIntent, in.ccl.util.Constants.SPLASH_SCREEN_RESULT);
	}

	AnimationListener rotationAnimationListener = new AnimationListener() {

		public void onAnimationStart (Animation animation) {
		}

		public void onAnimationRepeat (Animation animation) {
			// download initial data from server.

			floatingLogoImage.startAnimation(animation);
		}

		public void onAnimationEnd (Animation animation) {
			// Here it should get data from server for home screen.
			// isInitialDataLoaded is true when all required home screen data is downloaded from the server.
			// in this case it should navigate to home screen.
			if (isInitialDataLoaded) {
				Intent homeActivityIntent = new Intent(SplashScreenActivity.this, DisplayActivity.class);
				startActivity(homeActivityIntent);

				/*
				 * Intent homeActivityIntent = new Intent(SplashScreenActivity.this, HomeActivity.class); homeActivityIntent.putParcelableArrayListExtra(Constants.EXTRA_BANNER_KEY, bannerList); homeActivityIntent.putParcelableArrayListExtra(Constants.EXTRA_PHOTO_KEY, photoGalleryList);
				 * homeActivityIntent.putParcelableArrayListExtra(Constants.EXTRA_VIDEO_KEY, videoGalleryList); SplashScreenActivity.this.startActivityForResult(homeActivityIntent, in.ccl.util.Constants.SPLASH_SCREEN_RESULT);
				 */}
			else {
				// if initial data is not received from the server to show on home screen.
				// here it repeat the animation, until it gets the data.
				if (animationRotateCenter != null) {
					onAnimationRepeat(animationRotateCenter);
				}
			}
		}
	};

	/**
	 * Initializes all the background processes and the APIs which requires the application context Components getting initialized
	 * <ul>
	 * <li>Logger API</li>
	 * <li>Exception Handler</li>
	 * <li>Application Preferences</li>
	 * </ul>
	 * 
	 * @return <code>true</code> if initialization is successful else returns <code>false</code>
	 */
	private boolean initAppComponents (Context ctx) {
		boolean initialized = true;

		Context context = ctx;

		// Initializing the logger and app properties
		initialized = AppPropertiesUtil.initAppDirectory(context) && Logger.init(context) && AppPropertiesUtil.init(context);

		// Initializing the Exception Handler and setting it as the default for
		// all the uncaught exceptions
		String logDirectoryPath = AppPropertiesUtil.getAppDirectory(context) + getResources().getString(R.string.log_folder);

		ParadigmExceptionHandler paradigmException = new ParadigmExceptionHandler(this, logDirectoryPath);
		Thread.setDefaultUncaughtExceptionHandler(paradigmException);
		Logger.info("Initialization components " + initialized);
		return initialized;
	}

	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		// if user press back from Home activity splash screen should not be appear.
		// so here it finishes the splash screen activity.
		finish();
	}

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

			// Gets the status from the Intent's extended data, and chooses the appropriate action

			switch (intent.getIntExtra(Constants.EXTENDED_DATA_STATUS, Constants.STATE_ACTION_COMPLETE)) {
				case Constants.STATE_ACTION_VIDEO_ALBUM_COMPLETE:
					Cursor cursor = getContentResolver().query(DataProviderContract.PICTUREURL_TABLE_CONTENTURI, null, null, null, null);
					ArrayList <Items> bannerItems = BannerCursor.getItems(cursor);
					cursor = getContentResolver().query(DataProviderContract.PHOTO_ALBUM_TABLE_CONTENTURI, null, null, null, null);
					ArrayList <Items> photoAlbumItems = PhotoAlbumCurosr.getItems(cursor);
					cursor = getContentResolver().query(DataProviderContract.VIDEO_ALBUM_TABLE_CONTENTURI, null, null, null, null);
					ArrayList <Items> videoAlbumItems = VideoAlbumCursor.getItems(cursor);
					cursor.close();
					callHomeIntent(bannerItems, photoAlbumItems, videoAlbumItems);
					break;
				default:
					break;
			}
		}
	}
}// end of class

