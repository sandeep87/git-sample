package in.ccl.livescore.service;

import in.ccl.database.CCLPullService;
import in.ccl.ui.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.SystemClock;

/**
 * CheckUpdateService checks every 24 hours if updates have been made to the CCL Home screen of the current data.
 * */
public class CheckUpdateService extends Service {

	private static boolean DEBUG = false;

	// Check interval: every 24 hours
	private static long UPDATES_CHECK_INTERVAL = 24 * 60 * 60 * 1000;

	private CheckForUpdatesTask mTask;

	@Override
	public int onStartCommand (Intent intent, int flags, int startId) {
		(mTask = new CheckForUpdatesTask()).execute();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy () {
		super.onDestroy();
		if (mTask != null && mTask.getStatus() == UserTask.Status.RUNNING) {
			mTask.cancel(true);
		}
	}

	public IBinder onBind (Intent intent) {
		return null;
	}

	public static void schedule (Context context) {
		final Intent intent = new Intent(context, CheckUpdateService.class);
		final PendingIntent pending = PendingIntent.getService(context, 0, intent, 0);

		Calendar c = new GregorianCalendar();
		c.add(Calendar.DAY_OF_YEAR, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);

		final AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarm.cancel(pending);
		if (DEBUG) {
			alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 30 * 1000, pending);
		}
		else {
			alarm.setRepeating(AlarmManager.RTC, c.getTimeInMillis(), UPDATES_CHECK_INTERVAL, pending);
		}
	}

	private class CheckForUpdatesTask extends UserTask <Void, Object, Void> {

		@Override
		public void onPreExecute () {
		}

		public Void doInBackground (Void... params) {
      System.out.println("Checking background updates for home screen.");
			// Intent for starting the IntentService that downloads data.
			Intent mServiceIntent;
			// starting services for getting home screen data.
			mServiceIntent = new Intent(CheckUpdateService.this, CCLPullService.class).setData(Uri.parse(getResources().getString(R.string.banner_url)));
			mServiceIntent.putExtra("KEY", "update-banner");
			startService(mServiceIntent);

			mServiceIntent = new Intent(CheckUpdateService.this, CCLPullService.class).setData(Uri.parse(getResources().getString(R.string.photo_album_url)));
			mServiceIntent.putExtra("KEY", "update-photos");
			startService(mServiceIntent);

			mServiceIntent = new Intent(CheckUpdateService.this, CCLPullService.class).setData(Uri.parse(getResources().getString(R.string.video_album_url)));
			mServiceIntent.putExtra("KEY", "update-videos");
			startService(mServiceIntent);
			return null;
		}

		@Override
		public void onPostExecute (Void aVoid) {
			stopSelf();
		}
	}
}
