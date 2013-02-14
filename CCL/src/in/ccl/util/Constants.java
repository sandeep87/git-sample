package in.ccl.util;

import java.util.Locale;

public class Constants {

	public static final int SPLASH_SCREEN_RESULT = 1;

	public static int TEAM_RESULT = 111;

	public static final int PHOTO_PARSER = 100;

	public static final int VIDEO_PARSER = 101;

	public static final int PHOTO_ALBUM_PARSER = 102;

	public static final boolean DEBUG = true;

	public static final String KEY_APP_PREFERENCE_NAME = "in.ccl";

	public static final String EXTRA_ALBUM_ID = "photo_album_id";

	public static final String EXTRA_ALBUM_TITLE = "photo_album_title";

	public static final String EXTRA_BANNER_KEY = "extra_banner_key";

	public static final String EXTRA_PHOTO_KEY = "extra_photo_key";

	public static final String EXTRA_FROM_KEY = "extra_from_key";

	public static final String EXTRA_VIDEO_KEY = "extra_video_key";

	public static final String EXTRA_PHOTOS_LIST = "extra_photo_list";

	public static final String EXTRA_VIDEOS_LIST = "extra_videos_list";

	public static final String EXTRA_ALBUM_ITEMS = "extra_album_items";

	public static final String EXTRA_PHOTO_POSITION_ID = "extra_photo_position_id";

	public static final String EXTRA_VIDEO_ITEMS = "extra_video_items";

	public static final String EXTRA_VIDEO_POSITION_ID = "extra_video_position_id";

	public static final String EXTRA_TEAMLOGO_KEY = "extra_teamlogo_key";

	public static final String EXTRA_TEAMROLE_KEY = "extra_teamroles_key";

	public static final String EXTRA_TEAMMEMBER_KEY = "extra_teammember_key";

	public static final String EXTRA_NEWS_KEY = "extra_news_key";

	public static final String EXTRA_NEWS_DOWNLOAD_IMAGE_KEY = "extra_news_download_image_key";

	public static final String EXTRA_KEY = "extra_key";

	public static final String EXTRA_TEAMNAME = "extra_teamname";

	public static final String EXTRA_LOGO_POSITION = "extra_logo_position";

	public static final String EXTRA_TEAM_LOGO_KEY = "extra_team_logo_key";

	public static final String EXTRA_TEAM_MEMBER_KEY = "extra_team_members_key";

	public static final int PHOTO_GALLERY = 1;

	public static final int VIDEO_GALLERY = 2;

	public static final int PHOTO_ITEMS = 3;

	public static final int VIDEO_ITEMS = 4;

	// Set to true to turn on debug logging
	public static final boolean LOGD = true;

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

	// The background thread is doing logging
	public static final int STATE_LOG = -1;

	public static final CharSequence BLANK = " ";

	public static final String THUMBNAIL_FRAGMENT_TAG = "in.ccl.THUMBNAIL_FRAGMENT_TAG";

	// Custom actions

	public static final String ACTION_VIEW_IMAGE = "in.ccl.ACTION_VIEW_IMAGE";

	public static final String ACTION_ZOOM_IMAGE = "com.example.android.threadsample.ACTION_ZOOM_IMAGE";

	// Fragment tags
	public static final String PHOTO_FRAGMENT_TAG = "com.example.android.threadsample.PHOTO_FRAGMENT_TAG";

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

	public static final String MY_AD_UNIT_ID = "a15108b4f6bb5fe";

	public static final String EXTRA_DOWNLOAD_KEY = "extra_downlods_key";
	
	public static final String FIRST_INNINGS_KEY = "first_innings_key";
	public static final String SECOND_INNINGS_KEY = "second_innings_key";
	public static final String SECOND_INNINGS_STATUS = "second_innings_status";
  
	public static final int LOGIN_SUCCESS = 50;
	public static final int FACEBOOK_POST_SUCCESS = 51;
	public static final int FACEBOOK_POST_UNSUCCESS = 52;
}
