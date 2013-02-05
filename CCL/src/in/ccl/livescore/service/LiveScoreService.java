package in.ccl.livescore.service;

import in.ccl.database.BroadcastNotifier;
import in.ccl.database.Constants;
import in.ccl.logging.Logger;
import in.ccl.ui.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.http.HttpStatus;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class LiveScoreService extends IntentService {

	private static final String TAG = "LiveScoreService";

	private AlarmManager alarmManager;

	private BroadcastNotifier mBroadcaster = new BroadcastNotifier(this);

	public LiveScoreService () {
		super("LiveScoreService");
	}

	@Override
	protected void onHandleIntent (Intent workIntent) {
		// Gets a URL to read from the incoming Intent's "data" value
		String localUrlString = workIntent.getDataString();
		String compareKey = null;
		if (workIntent.hasExtra("KEY")) {
			compareKey = workIntent.getStringExtra("KEY");
		}
		else {
			compareKey = Uri.parse(localUrlString).getLastPathSegment();
		}
		// A URL that's local to this method
		URL localURL;
		try {
			// Convert the incoming data string to a URL.
			localURL = new URL(localUrlString);
			/*
			 * Tries to open a connection to the URL. If an IO error occurs, this throws an IOException
			 */
			URLConnection localURLConnection = localURL.openConnection();

			// If the connection is an HTTP connection, continue
			if ((localURLConnection instanceof HttpURLConnection)) {

				// Casts the connection to a HTTP connection
				HttpURLConnection localHttpURLConnection = (HttpURLConnection) localURLConnection;

				// Sets the user agent for this request.
				localHttpURLConnection.setRequestProperty("User-Agent", Constants.USER_AGENT);

				// Gets a response code from the RSS server
				int responseCode = localHttpURLConnection.getResponseCode();
				// Handles possible exceptions
				if (responseCode == HttpStatus.SC_OK) {
					if (compareKey.equals("livematches")) {
						String timeStamp = LiveScoreParser.parseCurrentMatchSchedule(localHttpURLConnection.getInputStream());
						Intent mServiceIntent = new Intent(this, LiveScoreService.class).setData(Uri.parse(getResources().getString(R.string.dummy_currentscore_url)));
						PendingIntent pendingIntent = PendingIntent.getService(this, 0, mServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
						long trigger = getScheduleTimeInMills(timeStamp);
						alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
						alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
					}
					else {
						String currentMessage = LiveScoreParser.parseCurrentScore(localHttpURLConnection.getInputStream());
						mBroadcaster.broadcastIntentWithCurrentScore(Constants.STATE_CURRENT_SCORE_TASK_COMPLETED, currentMessage);
						PendingIntent pendingIntent = PendingIntent.getService(this, 0, workIntent, PendingIntent.FLAG_UPDATE_CURRENT);
						long trigger = System.currentTimeMillis() + 3000;
						alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
						if (currentMessage == null) {
							alarmManager.cancel(pendingIntent);
						}
						else {
							alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
						}
					}
				}
			}
		}
		catch (MalformedURLException localMalformedURLException) {
			Logger.info(TAG, "MalformedURLException for current score/livematches");
		}
		catch (IOException localIOException) {
			Logger.info(TAG, "MalformedURLException for current score/livematches");
		}
	}

	private long getScheduleTimeInMills (String timeStamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", new Locale("in"));
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		java.util.Date dates;
		try {
			dates = sdf.parse(timeStamp);
			return dates.getTime();
		}
		catch (ParseException e) {
		}
		return 0;
	}

}
