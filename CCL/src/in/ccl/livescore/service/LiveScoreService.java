package in.ccl.livescore.service;

import in.ccl.database.BroadcastNotifier;
import in.ccl.database.Constants;
import in.ccl.logging.Logger;
import in.ccl.score.LiveScore;
import in.ccl.score.MatchesResponse;
import in.ccl.ui.R;
import in.ccl.ui.TopActivity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
		System.out.println("COmpare key " + compareKey);
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
					if (compareKey.equals("match_schedule.html")) {
						String timeStamp = LiveScoreParser.parseCurrentMatchSchedule(localHttpURLConnection.getInputStream());
						System.out.println("tIME STAMP "+timeStamp );
						Intent mServiceIntent = new Intent(this, LiveScoreService.class).setData(Uri.parse(getResources().getString(R.string.dummy_currentscore_url)));
						PendingIntent pendingIntent = PendingIntent.getService(this, 0, mServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
						long trigger = getScheduleTimeInMills(timeStamp);
						alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
						alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
					}
					else if (compareKey.equals("onematch.html")) {						
						ArrayList <MatchesResponse> matches = LiveScoreParser.parseMatches(localHttpURLConnection.getInputStream());
						System.out.println("Matches array " + matches.size());
						mBroadcaster.broadcastIntentWithMatches(Constants.STATE_MATCHES_TASK_COMPLETED, matches);
					}
					else if (compareKey.equals("livescoreone.html") || compareKey.equals("livescoretwo.html")) {
						LiveScore liveScore = LiveScoreParser.parseLiveScore(localHttpURLConnection.getInputStream());
						mBroadcaster.broadcastIntentWithLiveScore(Constants.STATE_LIVE_SCORE_TASK_COMPLETED, liveScore);
						Intent updateLIVEScoreIntent = new Intent(this, LiveScoreService.class).setData(Uri.parse(localUrlString));
						updateLIVEScoreIntent.putExtra("KEY", "update-livescore");
						startService(updateLIVEScoreIntent);
					}else if(compareKey.equals("update-livescore")){		
						LiveScore liveScore = LiveScoreParser.parseLiveScore(localHttpURLConnection.getInputStream());
						mBroadcaster.broadcastIntentWithLiveScore(Constants.STATE_LIVE_SCORE_UPDATE_TASK_COMPLETED, liveScore);
						PendingIntent pendingIntent = PendingIntent.getService(this, 0, workIntent, PendingIntent.FLAG_UPDATE_CURRENT);
						long trigger = System.currentTimeMillis() + 60000;
						alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
						if (liveScore == null) {
							alarmManager.cancel(pendingIntent);
						}
						else {
							alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
						}
					}
					else {
						System.out.println("LIve current score callling....");
						TopActivity.setCurrentScoreTimerStarted(true);
						String currentMessage = LiveScoreParser.parseCurrentScore(localHttpURLConnection.getInputStream());
						TopActivity.setCurrentScore(currentMessage);
						mBroadcaster.broadcastIntentWithCurrentScore(Constants.STATE_CURRENT_SCORE_TASK_COMPLETED, currentMessage);
						PendingIntent pendingIntent = PendingIntent.getService(this, 0, workIntent, PendingIntent.FLAG_UPDATE_CURRENT);
						long trigger = System.currentTimeMillis() + 60000;
						alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
						if (currentMessage == null) {
							alarmManager.cancel(pendingIntent);
						}
						else {
							alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
						}
					}
				}else{
					if(compareKey.equals("currentscore.html")){
					mBroadcaster.broadcastIntentWithCurrentScore(Constants.STATE_CURRENT_SCORE_TASK_COMPLETED, null);
					}
				}
			}
		}
		catch (MalformedURLException localMalformedURLException) {
			Logger.info(TAG, "MalformedURLException for current score/livematches " + localMalformedURLException.getMessage());
		}
		catch (IOException localIOException) {
			Logger.info(TAG, "MalformedURLException for current score/livematches" + localIOException.getMessage());
		}
	}

	private long getScheduleTimeInMills (String timeStamp) {
		//System.out.println("Timestamp "+timeStamp);
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
