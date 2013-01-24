package in.ccl.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CCLDBHandler extends SQLiteOpenHelper {

	private static final String TAG = "CCLDBHandler";

	private static final String DATABASE_NAME = "cclDB";

	private static final int DATABASE_VERSION = 1;

	public static final String CCL_INFO_TABLE = "ccl_info";

	public static final String CCL_BANNER_TABLE = "ccl_banner";

	public static final String CCL_ITEMS_TABLE = "ccl_items";

	public static final String CCL_ITEMS_PAGES_TABLE = "pages_table";

	public static final String ID = "_id";

	public static final String ITEM_ID = "I_Id";

	public static final String TITLE = "title";

	public static final String IMAGE_OR_VIDEO_URL = "image_or_video_url";

	public static final String THUMB_IMAGE_URL = "thumb_image_url";

	public static final String ALBUM_ID = "album_id";

	public static final String NO_OF_PAGES = "total_pages";

	public static final String BANNER_IMAGE_URL = "banner_image_url";

	private static final String CREATE_CCL_ITEMS_TABLE = "CREATE TABLE IF NOT EXISTS " + CCL_ITEMS_TABLE + "(" + ID + " INTEGER PRIMARY KEY," + TITLE + " TEXT);";

	private static final String CREATE_CCL_INFO_TABLE = "CREATE TABLE IF NOT EXISTS " + CCL_INFO_TABLE + " (" + ID + " INTEGER," + TITLE + " TEXT," + IMAGE_OR_VIDEO_URL + " TEXT," + THUMB_IMAGE_URL + " TEXT," + ALBUM_ID + " int," + ITEM_ID + " INTEGER, FOREIGN KEY(" + ITEM_ID + ") REFERENCES " + CCL_ITEMS_TABLE + "(" + ID + "));";

	private static final String CREATE_BANNER_TABLE = "CREATE TABLE IF NOT EXISTS " + CCL_BANNER_TABLE + " (" + ID + " INTEGER," + TITLE + " TEXT," + BANNER_IMAGE_URL + " TEXT," + ALBUM_ID + " INTEGER);";

	private static final String CREATE_ITEMS_PAGE_TABLE = "CREATE TABLE IF NOT EXISTS " + CCL_ITEMS_PAGES_TABLE + " (" + ID + " INTEGER," + ALBUM_ID + " INTEGER PRIMARY KEY," + NO_OF_PAGES + " INTEGER);";

	public CCLDBHandler (Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate (SQLiteDatabase db) {
		try {
			db.execSQL(CREATE_CCL_ITEMS_TABLE);
			db.execSQL(CREATE_CCL_INFO_TABLE);
			db.execSQL(CREATE_BANNER_TABLE);
			db.execSQL(CREATE_ITEMS_PAGE_TABLE);
			Log.i(TAG, "Database created.");
		}
		catch (SQLException e) {
			Log.e(TAG, "SQL Exception has cocurred while creating the tables" + e);
		}
	}// end of onCreate()

	@Override
	public void onUpgrade (SQLiteDatabase arg0, int arg1, int arg2) {

	}

}
