package in.ccl.ui;

import in.ccl.helper.ServerResponse;
import in.ccl.logging.Logger;
import in.ccl.logging.ParadigmExceptionHandler;
import in.ccl.util.AppPropertiesUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

/**
 * Initial load of banner, photo-gallery and video data(home screen) while splashing the screen. If yes, navigate to Home screen, otherwise shows network dialog.
 * 
 * @author Rajesh Babu | Paradigm Creatives
 */

public class SplashScreenActivity extends Activity implements ServerResponse{

	private Animation animationRotateCenter;

	private ImageView floatingLogoImage;

	private boolean isInitialDataLoaded;

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
	}

	AnimationListener rotationAnimationListener = new AnimationListener() {

		public void onAnimationStart (Animation animation) {
			// initializing logs and application utilities.
			initAppComponents(getApplicationContext());
		}

		public void onAnimationRepeat (Animation animation) {
			floatingLogoImage.startAnimation(animation);
		}

		public void onAnimationEnd (Animation animation) {
			// Here it should get data from server for home screen.
			// isInitialDataLoaded is true when all required home screen data is downloaded from the server.
			// in this case it should navigate to home screen.
			if (!isInitialDataLoaded) {
				Intent nextActivityIntent = new Intent(SplashScreenActivity.this, HomeActivity.class);
				SplashScreenActivity.this.startActivityForResult(nextActivityIntent, in.ccl.util.Constants.SPLASH_SCREEN_RESULT);
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
		
	}
}// end of class

