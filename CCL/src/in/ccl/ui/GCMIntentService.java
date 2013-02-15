package in.ccl.ui;

import java.util.List;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";
	Context mContext;

	public GCMIntentService() {
		super("763383436164");
		mContext = this;
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		SharedPreferences mSharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		Editor mEditor = mSharedPreferences.edit();
		mEditor.putString("Gcm_RegisterId", registrationId);
		mEditor.commit();
		NotificationActivity.sHandler.sendEmptyMessage(0);

	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "Received message" + intent.toString());
		if (isAppInRunningState(context)) {
			notifyPayload(context, intent.toString(), true);
		} else {
			notifyPayload(context, intent.toString(), true);
		}
	}

	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");

	}

	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);

	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		Log.i(TAG, "Received recoverable error: " + errorId);
		return false;

	}

	private boolean isAppInRunningState(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
		if ((taskInfo != null) && taskInfo.size() > 0) {
			String className = null;
			try {
				className = taskInfo.get(0).topActivity.getClassName();
			} catch (IndexOutOfBoundsException e) {
				return false;
			}
			if (className.contains("in.ccl.ui")) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/**
	 * This method shows the status bar notification
	 */
	private void notifyPayload(Context context, String notificationData,
			boolean isFrom) {
		try {
			NotificationManager mNotificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			CharSequence tickerText = notificationData;
			long when = System.currentTimeMillis();
			Notification notification = new Notification(R.drawable.ccl_logo,
					tickerText, when);
			int notificationLayoutId = -1;
			RemoteViews contentView = new RemoteViews(context.getPackageName(),
					notificationLayoutId);
			notification.contentView = contentView;
			PendingIntent contentIntent = null;
			if (isFrom) {
				Intent notificationIntent = new Intent(context,
						SplashScreenActivity.class);
				notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				contentIntent = PendingIntent.getActivity(context, 0,
						notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			}
			notification.contentIntent = contentIntent;
			notification.defaults |= Notification.DEFAULT_SOUND;
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			mNotificationManager.notify(1, notification);
		} catch (IllegalArgumentException iae) {
		} catch (Exception e) {
		}
	}

	/**
	 * This method cancels the status bar notification
	 */
	public synchronized void cancelNotification(Context context) {
		try {
			if (context != null) {
				NotificationManager mNotificationManager = (NotificationManager) context
						.getSystemService(Context.NOTIFICATION_SERVICE);
				mNotificationManager.cancel(1);
			}
		} catch (Exception e) {

		}
	}

}
