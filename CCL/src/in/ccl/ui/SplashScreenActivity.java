package in.ccl.ui;

import in.ccl.helper.ServerResponse;
import in.ccl.helper.Util;
import in.ccl.logging.Logger;
import in.ccl.logging.ParadigmExceptionHandler;
import in.ccl.model.Items;
import in.ccl.net.CCLService;
import in.ccl.net.DownLoadAsynTask;
import in.ccl.util.AppPropertiesUtil;
import in.ccl.util.Constants;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Initial load of banner, photo-gallery and video data(home screen) while splashing the screen. If yes, navigate to Home screen, otherwise shows network dialog.
 * 
 * @author Rajesh Babu | Paradigm Creatives
 */

public class SplashScreenActivity extends Activity implements ServerResponse {

	private Animation animationRotateCenter;

	private ImageView floatingLogoImage;

	private boolean isInitialDataLoaded;

	private ArrayList <Items> bannerList = new ArrayList <Items>();

	private ArrayList <Items> photoGalleryList = new ArrayList <Items>();

	private ArrayList <Items> videoGalleryList = new ArrayList <Items>();

	private DownLoadAsynTask asyncTask;

	public enum RequestType {
		NO_REQUEST, BANNER_REQUEST, PHOTO_ALBUMREQUEST, VIDEO_ALBUMREQUEST;
	}

	RequestType mRequestType = RequestType.NO_REQUEST;

	/**
	 * Called when the activity is first created.
	 */

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Setting the default layout
		setContentView(R.layout.splash_screen);

		// getting reference of ccl logo for animated splash screen
		floatingLogoImage = (ImageView) findViewById(R.id.logo_image);
		// creating rotate animation object by loading rotate_center anim properties.
		animationRotateCenter = AnimationUtils.loadAnimation(this, R.anim.rotate_center);
		// adding rotation animation lister to animation object to know the start and end of the animations.
		animationRotateCenter.setAnimationListener(rotationAnimationListener);
		// starting the animation for ccl logo on splash screen.
		floatingLogoImage.startAnimation(animationRotateCenter);
		// initializing logs and application utilities.
		initAppComponents(SplashScreenActivity.this);

		if (Util.getInstance().isOnline(SplashScreenActivity.this)) {
			asyncTask = new DownLoadAsynTask(this, this, true);
			mRequestType = RequestType.BANNER_REQUEST;
			asyncTask.execute(getResources().getString(R.string.banner_url));
		}
		else {
			animationRotateCenter = null;
			Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
		}
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

				Intent homeActivityIntent = new Intent(SplashScreenActivity.this, HomeActivity.class);
				homeActivityIntent.putParcelableArrayListExtra(Constants.EXTRA_BANNER_KEY, bannerList);
				homeActivityIntent.putParcelableArrayListExtra(Constants.EXTRA_PHOTO_KEY, photoGalleryList);
				homeActivityIntent.putParcelableArrayListExtra(Constants.EXTRA_VIDEO_KEY, videoGalleryList);
				SplashScreenActivity.this.startActivityForResult(homeActivityIntent, in.ccl.util.Constants.SPLASH_SCREEN_RESULT);
			}
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

	@Override
	public void setData (String result) {
		if (result != null) {
			switch (mRequestType) {
				case NO_REQUEST:
					mRequestType = RequestType.NO_REQUEST;
					break;
				case BANNER_REQUEST:
					// parsing server banner items response.
					bannerList = CCLService.getBannerItems(result);
					// for downloading photo album data.
					asyncTask = new DownLoadAsynTask(this, this, true);
					mRequestType = RequestType.PHOTO_ALBUMREQUEST;
					asyncTask.execute(getResources().getString(R.string.photo_album_url));

					break;
				case PHOTO_ALBUMREQUEST:
					// parsing server photo album responose.
					photoGalleryList = CCLService.getPhotoAlbums(result);
					// for downloading video albums.
					asyncTask = new DownLoadAsynTask(this, this, true);
					mRequestType = RequestType.VIDEO_ALBUMREQUEST;
					asyncTask.execute(getResources().getString(R.string.video_album_url));
					break;
				case VIDEO_ALBUMREQUEST:
					// parsing video album response.
					videoGalleryList = CCLService.getVideoAlbums(result);
					// to finish the animation, it will be executed in onAnimation end method.
					if (videoGalleryList != null) {
						isInitialDataLoaded = true;
					}
					break;
				default:
					break;
			}
		}
		else {
			// No network connection.
			if (!Util.getInstance().isOnline(SplashScreenActivity.this)) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
			}
		}

	}
}// end of class

