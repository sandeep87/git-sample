package in.ccl.ui;

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

public class SplashScreenActivity extends Activity {

	private Animation animationRotateCenter;

	private ImageView floatingLogoImage;

	/**
	 * Called when the activity is first created.
	 */

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Setting the default layout
		setContentView(R.layout.splash_screen);

		// for animated splash screen.
		floatingLogoImage = (ImageView) findViewById(R.id.logo_image);
		animationRotateCenter = AnimationUtils.loadAnimation(this, R.anim.rotate_center);
		animationRotateCenter.setAnimationListener(rotationAnimationListener);
		floatingLogoImage.startAnimation(animationRotateCenter);
	}

	AnimationListener rotationAnimationListener = new AnimationListener() {

		public void onAnimationStart (Animation animation) {
			initAppComponents(SplashScreenActivity.this); // initializing logs and application utilities.
		}

		public void onAnimationRepeat (Animation animation) {
		}

		public void onAnimationEnd (Animation animation) {
			Intent nextActivityIntent = new Intent(SplashScreenActivity.this, HomeActivity.class);
			SplashScreenActivity.this.startActivityForResult(nextActivityIntent, in.ccl.util.Constants.SPLASH_SCREEN_RESULT);
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
}// end of class

