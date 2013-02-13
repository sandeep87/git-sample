package in.ccl.database;

import java.util.Locale;

public class Constants {

	// The download is starting
	public static final int STATE_ACTION_STARTED = 0;

	// The background thread is connecting to the RSS feed
	public static final int STATE_ACTION_CONNECTING = 1;

	// The background thread is parsing the RSS feed
	public static final int STATE_ACTION_PARSING = 2;

	// The background thread is writing data to the content provider
	public static final int STATE_ACTION_WRITING = 3;

	// The background thread is done
	public static final int STATE_ACTION_COMPLETE = 4;

	// The banner thread is done
	public static final int STATE_ACTION_BANNER_COMPLETE = 5;

	// The photo album thread is done
	public static final int STATE_ACTION_PHOTO_ALBUM_COMPLETE = 6;

	// The video album thread is done
	public static final int STATE_ACTION_VIDEO_ALBUM_COMPLETE = 7;

	// The video album thread is done
	public static final int STATE_ACTION_PHOTO_COMPLETE = 8;

	// The video album thread is done
	public static final int STATE_ACTION_VIDEO_COMPLETE = 9;

	public static final int STATE_ACTION_BANNER_UPDATES_COMPLETE = 10;

	public static final int STATE_ACTION_PHOTO_ALBUM_UPDATES_COMPLETE = 11;

	public static final int STATE_ACTION_VIDEO_ALBUM_UPDATES_COMPLETE = 12;

	public static final int STATE_ACTION_PHOTO_PAGES_DOWNLOAD_COMPLETE = 13;

	public static final int STATE_ACTION_VIDEO_PAGES_DOWNLOAD_COMPLETE = 14;

	public static final int STATE_ACTION_BANNER_PAGES_DOWNLOAD_COMPLETE = 15;

	public static final int STATE_ACTION_VIDEO_UPDATES_COMPLETE = 16;

	public static final int STATE_ACTION_PHOTO_UPDATES_COMPLETE = 17;

	public static final int STATE_ACTION_DOWNLOAD_IMAGE_COMPLETE = 19;
//The team logos thread is done
	public static final int STATE_ACTION_TEAM_LOGO_COMPLETE = 21;
//The team members thread is done
	public static final int STATE_ACTION_TEAM_MEMBERS_COMPLETE = 22;
	/*
	 * A user-agent string that's sent to the HTTP site. It includes information about the device and the build that the device is running.
	 */
	public static final String USER_AGENT = "Mozilla/5.0 (Linux; U; Android " + android.os.Build.VERSION.RELEASE + ";" + Locale.getDefault().toString() + "; " + android.os.Build.DEVICE + "/" + android.os.Build.ID + ")";

	// Defines a custom Intent action
	public static final String BROADCAST_ACTION = "in.ccl.BROADCAST";

	// Defines the key for the status "extra" in an Intent
	public static final String EXTENDED_DATA_STATUS = "in.ccl.STATUS";

	// Defines the key for the log "extra" in an Intent
	public static final String EXTENDED_STATUS_LOG = "in.ccl.LOG";

	public static final boolean LOGD = true;

	public static final int STATE_ACTION_UPDATE_DOWNLOAD_IMAGE_COMPLETE = 24;

	public static final int STATE_CURRENT_SCORE_TASK_COMPLETED = 25;

	public static final int STATE_MATCHES_TASK_COMPLETED = 26;

	public static final int STATE_LIVE_SCORE_TASK_COMPLETED = 27;

	public static final int STATE_LIVE_SCORE_UPDATE_TASK_COMPLETED = 28;
	
	public static final int STATE_LIVE_SCOREBOARD_UPDATE_TASK_COMPLETED = 29;
	public static final int STATE_LIVE_SCOREBOARD_TASK_COMPLETED = 30;


	public static final int STATE_LIVE_SCORE_ACTIVITY_TASK_COMPLETED = 31;
}
