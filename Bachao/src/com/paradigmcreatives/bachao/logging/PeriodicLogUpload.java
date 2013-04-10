package com.paradigmcreatives.bachao.logging;

import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.paradigmcreatives.bachao.R;

/**
 * This class takes care of doing periodic log uploads. This achieved by setting an alarm to go off after every n-seconds and uploading calling the log upload functionality at that event. By default the alarm would be set to go off once at 12:00 AM everyday. If it fails to get the current time then
 * the first log is uploaded immediately at the time of initialization and rest of them at the same time everyday
 * 
 * In essence this class is a receiver for the alarms and initialization for the same
 * 
 * @author robin
 * 
 */
public class PeriodicLogUpload extends BroadcastReceiver {

	private static final int REQUEST_CODE = 4101985;

	private static final String TAG = "PeriodicLogUpload";

	private static final long MILLS_IN_ONE_DAY = 24 * 60 * 60 * 1000;

	public static boolean init (Context context) {
		// Cannot proceed if context is null
		if (context == null) {
			return false;
		}

		Intent intent = new Intent(context, PeriodicLogUpload.class);
		intent.putExtra("alarm_message", "time to upload logs");

		PendingIntent sender = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		// Get the AlarmManager service
		long firstUploadTime = milliSecondsUntilMidnight();
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC_WAKEUP, firstUploadTime, MILLS_IN_ONE_DAY, sender);

		return true;
	}

	@Override
	public void onReceive (Context context, Intent intent) {
		try {
			Bundle bundle = intent.getExtras();
			String message = bundle.getString("alarm_message");

			if (message != null && message.equals("time to upload logs") && context != null) {
				String logDirectoryPath = DeviceInfoUtil.getAppDirectory(context) + context.getResources().getString(R.string.log_folder);
				LogUploader logUploader = new LogUploader(context, logDirectoryPath);
				logUploader.uploadLogsToS3();
			}
			else {
				Log.w(TAG, "Alarm set off but didn't ask to upload logs");
			}

		}
		catch (Exception e) {
			Logger.warn(TAG, "Could not upload the periodic logs - " + e);

		}

	}

	/**
	 * Finds and returns the milliseconds for the midnight of the date when its being called (Epoch time-stamp). Can also return 0 if it cannot determine today's date
	 * 
	 * @return milliseconds to midnight tonight
	 */
	private static long milliSecondsUntilMidnight () {
		long milliSeconds = 0;

		Date now = new Date();
		milliSeconds = MILLS_IN_ONE_DAY - now.getTime() % MILLS_IN_ONE_DAY;
		milliSeconds += now.getTime();

		return milliSeconds;
	}

}
