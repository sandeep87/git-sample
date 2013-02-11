package in.ccl.livescore.service;

import in.ccl.database.BroadcastNotifier;
import in.ccl.database.Constants;
import in.ccl.logging.Logger;
import in.ccl.model.MatchSchedule;
import in.ccl.score.LiveScore;
import in.ccl.score.MatchesResponse;
import in.ccl.score.ScoreBoard;
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
import java.util.Date;
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
					if (compareKey.equals("match_schedule")) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", new Locale("in"));
						SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd", new Locale("in"));
						MatchSchedule matchSchedule = LiveScoreParser.parseCurrentMatchSchedule(localHttpURLConnection.getInputStream());
						if (matchSchedule != null && matchSchedule.getStartTime() != null && matchSchedule.getEndDate() != null && matchSchedule.getStatus() != null) {
							dateSdf.setTimeZone(TimeZone.getTimeZone("UTC"));
							java.util.Date date = new java.util.Date();
							try {
								Date todayDate = dateSdf.parse(sdf.format(date));;
								Date scheduleDate = dateSdf.parse(dateSdf.format(matchSchedule.getStartTime()));
								if (scheduleDate.compareTo(todayDate) == 0 && date.getTime() >= matchSchedule.getStartTime().getTime() && date.getTime() <= matchSchedule.getEndDate().getTime() && matchSchedule.getStatus().equalsIgnoreCase("started")) {
									System.out.println("Calling for current score");
									Intent mServiceIntent = new Intent(this, LiveScoreService.class).setData(Uri.parse(getResources().getString(R.string.currentscore_url)));
									PendingIntent pendingIntent = PendingIntent.getService(this, 0, mServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
									long trigger = getScheduleTimeInMills(matchSchedule.getStartTime());
									alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
									alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
								}
								else {
									workIntent.putExtra("KEY", "match_schedule_update");
									System.out.println("Schedule for next start date " + getScheduleTimeInMills(matchSchedule.getStartTime()));

									PendingIntent pendingIntent = PendingIntent.getService(this, 0, workIntent, PendingIntent.FLAG_UPDATE_CURRENT);
									alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
									long trigger = getScheduleTimeInMills(matchSchedule.getStartTime());
									alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);

								}
							}

							catch (ParseException e) {
								e.printStackTrace();
							}
						}
					}
					else if (compareKey.equals("match_schedule_update")) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", new Locale("in"));
						SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd", new Locale("in"));
						dateSdf.setTimeZone(TimeZone.getTimeZone("UTC"));
						java.util.Date date = new java.util.Date();
						MatchSchedule matchSchedule = LiveScoreParser.parseCurrentMatchSchedule(localHttpURLConnection.getInputStream());
						if (matchSchedule != null && matchSchedule.getStartTime() != null && matchSchedule.getEndDate() != null && matchSchedule.getStatus() != null) {
							try {
								Date todayDate = dateSdf.parse(sdf.format(date));;
								Date scheduleDate = dateSdf.parse(dateSdf.format(matchSchedule.getStartTime()));
								if (scheduleDate.compareTo(todayDate) == 0 && date.getTime() >= matchSchedule.getStartTime().getTime() && date.getTime() <= matchSchedule.getEndDate().getTime() && matchSchedule.getStatus().equalsIgnoreCase("started")) {
									Intent mServiceIntent = new Intent(this, LiveScoreService.class).setData(Uri.parse(getResources().getString(R.string.currentscore_url)));
									PendingIntent pendingIntent = PendingIntent.getService(this, 0, mServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
									long trigger = getScheduleTimeInMills(matchSchedule.getStartTime());
									alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
									alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
								}
								else {
									if (date.getTime() <= matchSchedule.getEndDate().getTime()) {
										System.out.println("RAJESH Setting for next 30 mins");
										PendingIntent pendingIntent = PendingIntent.getService(this, 0, workIntent, PendingIntent.FLAG_UPDATE_CURRENT);
										alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
										long trigger = System.currentTimeMillis() + 30 * 60000;
										alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
										TopActivity.setCurrentScoreTimerStarted(false);
									}
									else {
									}
								}
							}
							catch (ParseException e) {
								e.printStackTrace();
							}
						}
					}
					else if (compareKey.equals("livematches")) {
						ArrayList <MatchesResponse> matches = LiveScoreParser.parseMatches(localHttpURLConnection.getInputStream());
						mBroadcaster.broadcastIntentWithMatches(Constants.STATE_MATCHES_TASK_COMPLETED, matches);
					}
					else if (compareKey.equals("livescore")) {
						LiveScore liveScore = LiveScoreParser.parseLiveScore(localHttpURLConnection.getInputStream());
						mBroadcaster.broadcastIntentWithLiveScore(Constants.STATE_LIVE_SCORE_TASK_COMPLETED, liveScore);
						Intent updateLIVEScoreIntent = new Intent(this, LiveScoreService.class).setData(Uri.parse(localUrlString));
						updateLIVEScoreIntent.putExtra("KEY", "update-livescore");
						startService(updateLIVEScoreIntent);
					}
					else if (compareKey.equals("update-livescore")) {
						LiveScore liveScore = LiveScoreParser.parseLiveScore(localHttpURLConnection.getInputStream());
						mBroadcaster.broadcastIntentWithLiveScore(Constants.STATE_LIVE_SCORE_UPDATE_TASK_COMPLETED, liveScore);
						PendingIntent pendingIntent = PendingIntent.getService(this, 0, workIntent, PendingIntent.FLAG_UPDATE_CURRENT);
						long trigger = System.currentTimeMillis() + 30000;
						alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
						if (liveScore == null) {
							alarmManager.cancel(pendingIntent);
						}
						else {
							alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
						}
					}
					else if (compareKey.equals("fullscore")) {
						ScoreBoard scoreBoard = LiveScoreParser.parseScoreBoard(localHttpURLConnection.getInputStream());
						mBroadcaster.broadcastIntentWithScoreBoard(Constants.STATE_LIVE_SCOREBOARD_TASK_COMPLETED, scoreBoard);
					}
					else if (compareKey.equals("score_board_update")) {
						System.out.println("score_board_update");
						ScoreBoard scoreBoard = LiveScoreParser.parseScoreBoard(localHttpURLConnection.getInputStream());
						mBroadcaster.broadcastIntentWithScoreBoard(Constants.STATE_LIVE_SCOREBOARD_UPDATE_TASK_COMPLETED, scoreBoard);
						workIntent.putExtra("KEY", "score_board_update");
						PendingIntent pendingIntent = PendingIntent.getService(this, 0, workIntent, PendingIntent.FLAG_UPDATE_CURRENT);
						long trigger = System.currentTimeMillis() + 30000;
						alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
						if (scoreBoard == null) {
							System.out.println("Setting alaram cancled");

							alarmManager.cancel(pendingIntent);
						}
						else {
							System.out.println("Setting alaram ");

							alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
						}

					}
					else if (compareKey.equals("currentscore")) {
						TopActivity.setCurrentScoreTimerStarted(true);
						String currentMessage = LiveScoreParser.parseCurrentScore(localHttpURLConnection.getInputStream());
						TopActivity.setCurrentScore(currentMessage);
						mBroadcaster.broadcastIntentWithCurrentScore(Constants.STATE_CURRENT_SCORE_TASK_COMPLETED, currentMessage);
						PendingIntent pendingIntent = PendingIntent.getService(this, 0, workIntent, PendingIntent.FLAG_UPDATE_CURRENT);
						long trigger = System.currentTimeMillis() + 30000;
						alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
						if (currentMessage == null) {
							alarmManager.cancel(pendingIntent);
							Intent mServiceIntent = new Intent(this, LiveScoreService.class).setData(Uri.parse(getResources().getString(R.string.match_schedule_url)));
							pendingIntent = PendingIntent.getService(this, 0, mServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
							alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
							trigger = System.currentTimeMillis() + 5 * 30000;
							alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
							TopActivity.setCurrentScoreTimerStarted(false);
						}
						else {
							alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, pendingIntent);
						}
					}
				}
				else {
					if (compareKey.equals("currentscore")) {
						mBroadcaster.broadcastIntentWithCurrentScore(Constants.STATE_CURRENT_SCORE_TASK_COMPLETED, null);
					}
					else if (compareKey.equals("fullscore")) {
						TopActivity.setTopHeaderSelected(false);
					}
					else if (compareKey.equals("score_board")) {
						System.out.println("score_board error");
						mBroadcaster.broadcastIntentWithScoreBoard(Constants.STATE_LIVE_SCOREBOARD_TASK_COMPLETED, null);
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

	private long getScheduleTimeInMills (Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", new Locale("in"));
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		java.util.Date dates;
		try {
			dates = sdf.parse(sdf.format(date));
			return dates.getTime();
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
