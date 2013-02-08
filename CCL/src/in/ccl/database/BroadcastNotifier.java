package in.ccl.database;

import in.ccl.model.Items;
import in.ccl.score.Innings;
import in.ccl.score.LiveScore;
import in.ccl.score.MatchesResponse;
import in.ccl.score.ScoreBoard;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class BroadcastNotifier {

	private LocalBroadcastManager mBroadcaster;

	public BroadcastNotifier (Context context) {
		// Gets an instance of the support library local broadcastmanager
		mBroadcaster = LocalBroadcastManager.getInstance(context);
	}

	/**
	 * 
	 * Uses LocalBroadcastManager to send an {@link Intent} containing {@code status}. The {@link Intent} has the action {@code BROADCAST_ACTION} and the category {@code DEFAULT}.
	 * 
	 * @param status {@link Integer} denoting a work request status
	 * @param list
	 */
	public void broadcastIntentWithState (int status, ArrayList <Items> list) {

		Intent localIntent = new Intent();

		// The Intent contains the custom broadcast action for this app
		localIntent.setAction(Constants.BROADCAST_ACTION);

		// Puts the status into the Intent
		localIntent.putExtra(Constants.EXTENDED_DATA_STATUS, status);
		localIntent.addCategory(Intent.CATEGORY_DEFAULT);
		if (list != null) {
			localIntent.putParcelableArrayListExtra("list", list);
		}

		// Broadcasts the Intent
		mBroadcaster.sendBroadcast(localIntent);

	}

	public void broadcastIntentWithCurrentScore (int status, String message) {

		Intent localIntent = new Intent();

		// The Intent contains the custom broadcast action for this app
		localIntent.setAction(Constants.BROADCAST_ACTION);

		// Puts the status into the Intent
		localIntent.putExtra(Constants.EXTENDED_DATA_STATUS, status);
		localIntent.addCategory(Intent.CATEGORY_DEFAULT);
		localIntent.putExtra("current_score", message);
		// Broadcasts the Intent
		mBroadcaster.sendBroadcast(localIntent);

	}

	/**
	 * Uses LocalBroadcastManager to send an {@link String} containing a logcat message. {@link Intent} has the action {@code BROADCAST_ACTION} and the category {@code DEFAULT}.
	 * 
	 * @param logData a {@link String} to insert into the log.
	 */
	public void notifyProgress (String logData) {

		Intent localIntent = new Intent();

		// The Intent contains the custom broadcast action for this app
		localIntent.setAction(Constants.BROADCAST_ACTION);

		localIntent.putExtra(Constants.EXTENDED_DATA_STATUS, -1);

		// Puts log data into the Intent
		localIntent.putExtra(Constants.EXTENDED_STATUS_LOG, logData);
		localIntent.addCategory(Intent.CATEGORY_DEFAULT);

		// Broadcasts the Intent
		mBroadcaster.sendBroadcast(localIntent);

	}

	public void broadcastIntentWithMatches (int status, ArrayList <MatchesResponse> matchesResponse) {

		Intent localIntent = new Intent();

		// The Intent contains the custom broadcast action for this app
		localIntent.setAction(Constants.BROADCAST_ACTION);

		// Puts the status into the Intent
		localIntent.putExtra(Constants.EXTENDED_DATA_STATUS, status);

		localIntent.addCategory(Intent.CATEGORY_DEFAULT);
		if (matchesResponse != null) {
			System.out.println("maches size in broadcast "+matchesResponse.size());
			localIntent.putParcelableArrayListExtra("matches_list", matchesResponse);
		}

		// Broadcasts the Intent
		mBroadcaster.sendBroadcast(localIntent);

	}

	public void broadcastIntentWithLiveScore (int status, LiveScore liveScore) {

		Intent localIntent = new Intent();

		// The Intent contains the custom broadcast action for this app
		localIntent.setAction(Constants.BROADCAST_ACTION);

		// Puts the status into the Intent
		localIntent.putExtra(Constants.EXTENDED_DATA_STATUS, status);

		localIntent.addCategory(Intent.CATEGORY_DEFAULT);
		if (liveScore != null) {
			localIntent.putExtra("livescore", liveScore);
		}
		// Broadcasts the Intent
		mBroadcaster.sendBroadcast(localIntent);

	}

	public void broadcastIntentWithScoreBoard (int status, ScoreBoard scoreBoard) {

		Intent localIntent = new Intent();

		// The Intent contains the custom broadcast action for this app
		localIntent.setAction(Constants.BROADCAST_ACTION);

		// Puts the status into the Intent
		localIntent.putExtra(Constants.EXTENDED_DATA_STATUS, status);

		localIntent.addCategory(Intent.CATEGORY_DEFAULT);
		if (scoreBoard != null) {
			localIntent.putExtra("scoreboard", scoreBoard);
		}
		// Broadcasts the Intent
		mBroadcaster.sendBroadcast(localIntent);

	}

}
