package com.paradigmcreatives.bachao.util;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.paradigmcreatives.bachao.R;

/**
 * Utility class for maintaining the application preferences
 * 
 * @author robin
 * 
 */
public class AppPropertiesUtil {

	/**
	 * Initializes the application preferences
	 * 
	 * @param context Context of the application
	 * @return <code>true</code> if initialization is successfull, else returns <code>false</code>
	 */
	public static boolean init (Context context) {
		if (context == null) {
			return false;
		}

		boolean initialized = true;

		// Get a handle to shared preference. It always returns a value
		SharedPreferences appPref = context.getSharedPreferences(Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = appPref.edit();

		// Check for the existence of the required keys, put them if they don't
		// exist

		// Commit the changes to shared preference (if any)
		initialized = prefEditor.commit();
		if (Constants.DEBUG) {
			Log.v("TAG", "Initialization of app preferences - " + initialized);
		}

		return initialized;
	}

	/**
	 * Created the application directory on the SD card if it does not exists already
	 * 
	 * @param context Application's context
	 * @return <code>true</code> if directory created successfully or it already exists, otherwsie <code>false</code>
	 */
	public static boolean initAppDirectory (Context context) {
		if (context == null) {
			return false;
		}

		String sdCardPath = Util.getSdCardPath();
		if (sdCardPath != null && DeviceInfoUtil.mediaWritable()) {
			File appDir = new File(Util.getSdCardPath() + context.getResources().getString(R.string.app_name));

			return appDir.exists() || appDir.mkdirs();
		}
		else {
			return false;
		}
	}

	/**
	 * Returns the application directory
	 * 
	 * @param context
	 * @return Application directory, <code>null</code> if not available
	 */
	public static String getAppDirectory (Context context) {
		if (context == null) {
			return null;
		}

		String sdCardPath = Util.getSdCardPath();
		if (sdCardPath != null) {
			return sdCardPath + context.getResources().getString(R.string.app_name) + File.separator;
		}
		else {
			return null;
		}

	}

}
