package in.ccl.ui;

import in.ccl.logging.Logger;
import in.ccl.logging.ParadigmExceptionHandler;
import in.ccl.util.AppPropertiesUtil;
import in.ccl.util.Constants;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Initial load of banner, photo-gallery and video data(home screen) while
 * splashing the screen. If yes, navigate to Home screen, otherwise shows
 * network dialog.
 * 
 * @author Rajesh Babu | Paradigm Creatives
 */

public class SplashScreenActivity extends Activity {
	private Animation animationRotateCenter;

	private Animation logoMoveTranslateAnimation;

	private Animation logoMoveScaleAnimation;

	private Animation txtTranselateAnimation;

	private Animation imageTranselateAnimation;

	private ImageView floatingLogoImage;

	private ImageView imgCaption;

	// private ImageView imgTopLogo;

	// private TextView txtSeason;

	/**
	 * Called when the activity is first created.
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Hiding title bar from the current window.
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Setting the default layout
		setContentView(R.layout.splash_screen);
		// for animation

		floatingLogoImage = (ImageView) findViewById(R.id.logo_image);
		// txtSeason = (TextView) findViewById(R.id.txt_season);
		// imgCaption = (ImageView) findViewById(R.id.caption_image);
		// imgTopLogo = (ImageView) findViewById(R.id.logo);

		animationRotateCenter = AnimationUtils.loadAnimation(this,
				R.anim.rotate_center);
		/*
		 * logoMoveTranslateAnimation = AnimationUtils.loadAnimation(this,
		 * R.anim.move_ccllogo_translate);
		 * 
		 * txtTranselateAnimation = AnimationUtils.loadAnimation(this,
		 * R.anim.move_season_top); imageTranselateAnimation =
		 * AnimationUtils.loadAnimation(this, R.anim.move_caption_down);
		 */

		animationRotateCenter.setAnimationListener(rotationAnimationListener);
		floatingLogoImage.startAnimation(animationRotateCenter);
		/*
		 * txtTranselateAnimation
		 * .setAnimationListener(txtTranslateAnimationListener);
		 * logoMoveTranslateAnimation
		 * .setAnimationListener(logoMoveTranslateAnimationListener);
		 */

		// Initialize the components

		/*
		 * else { // Initialization failed }
		 */
	}

	AnimationListener rotationAnimationListener = new AnimationListener() {
		public void onAnimationStart(Animation animation) {
		}

		public void onAnimationRepeat(Animation animation) {
		}

		public void onAnimationEnd(Animation animation) {
			initAppComponents(SplashScreenActivity.this);

			Intent nextActivityIntent = new Intent(SplashScreenActivity.this,
					HomeActivity.class);
			delayedNextActivity(nextActivityIntent,
					Constants.SPLASH_SCREEN_DURATION);

			// txtSeason.startAnimation(txtTranselateAnimation);
			// imgCaption.startAnimation(imageTranselateAnimation);
		}
	};
	AnimationListener txtTranslateAnimationListener = new AnimationListener() {

		public void onAnimationStart(Animation animation) {
		}

		public void onAnimationRepeat(Animation animation) {
		}

		public void onAnimationEnd(Animation animation) {
			// txtSeason.setVisibility(View.INVISIBLE);
			// imgCaption.setVisibility(View.INVISIBLE);
			// floatingLogoImage.startAnimation(logoMoveTranslateAnimation);
		}
	};
	AnimationListener logoMoveTranslateAnimationListener = new AnimationListener() {

		public void onAnimationStart(Animation animation) {

		}

		public void onAnimationRepeat(Animation animation) {
		}

		public void onAnimationEnd(Animation animation) {
			floatingLogoImage.setVisibility(View.GONE);
			// imgTopLogo.setVisibility(View.VISIBLE);

		}
	};

	/**
	 * Initializes all the background processes and the APIs which requires the
	 * application context Components getting initialized
	 * <ul>
	 * <li>Logger API</li>
	 * <li>Exception Handler</li>
	 * <li>Application Preferences</li>
	 * </ul>
	 * 
	 * @return <code>true</code> if initialization is successful else returns
	 *         <code>false</code>
	 */
	private boolean initAppComponents(Context ctx) {
		boolean initialized = true;

		Context context = ctx;

		// Initializing the logger, pending sync util and app properties
		initialized = AppPropertiesUtil.initAppDirectory(context)
				&& Logger.init(context) && AppPropertiesUtil.init(context);

		// Initializing the Exception Handler and setting it as the default for
		// all the uncaught exceptions
		String logDirectoryPath = AppPropertiesUtil.getAppDirectory(context)
				+ getResources().getString(R.string.log_folder);

		ParadigmExceptionHandler paradigmException = new ParadigmExceptionHandler(
				this, logDirectoryPath);
		Thread.setDefaultUncaughtExceptionHandler(paradigmException);
		// Logger.info("Initialization components "+initialized);
		return initialized;

	}

	/**
	 * Waits for the given time before starting the next activity
	 * 
	 * @param nextActivityIntent
	 *            Intent for next activity
	 * @param delayBy
	 *            Time is ms by which the start of next activity should be
	 *            delayed
	 */
	private void delayedNextActivity(final Intent nextActivityIntent,
			long delayBy) {
		// Keep the splash screen for few seconds and then move to the next
		// activity by killing the existing activity
		Handler splashScreenHandler = new Handler();
		splashScreenHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				finish();
				SplashScreenActivity.this.startActivity(nextActivityIntent);
				overridePendingTransition(0, 0);
			}

		}, Constants.SPLASH_SCREEN_DURATION);

	}

}// end of class

