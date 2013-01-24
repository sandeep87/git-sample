package in.ccl.database;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class BannerItems {

	private static final String TAG = "BannerItems";
	private static Cursor cursor;
	public static void insertItems (CCLDBHandler dbHandler, SQLiteDatabase sqlDB, ArrayList <in.ccl.model.Items> bannerList) {

		try {
			sqlDB = dbHandler.getWritableDatabase();
			String query = "insert into " + CCLDBHandler.CCL_BANNER_TABLE + " values (?,?,?,?)";
			try {
				// inserting banner items.
				SQLiteStatement insertDoodle = sqlDB.compileStatement(query);
				for (in.ccl.model.Items items : bannerList) {
					insertDoodle.bindLong(1, items.getId());
					insertDoodle.bindString(2, items.getTitle());
					insertDoodle.bindString(3, items.getPhotoOrVideoUrl());
					insertDoodle.bindLong(4, items.getAlbumId());
					insertDoodle.executeInsert();
				}
			}
			catch (SQLException sqle) {
				Log.e(TAG, "SQL error while inserting records.", sqle);
			}
			catch (Exception e) {
				Log.e(TAG, "Unknown error while inserting records.", e);
			}
			finally {
				try {
					sqlDB.close();
				}
				catch (SQLException sqle) {
					Log.e(TAG, "SQL error while closing DB connection.", sqle);
				}
				catch (Exception e) {
					Log.e(TAG, "Unknown error while closing DB connection.", e);
				}
			}
		}
		catch (SQLException sqle) {
			Log.e(TAG, "Error connecting to database.", sqle);
		}
	}
	
	public static ArrayList <in.ccl.model.Items> getBannerItems (CCLDBHandler dbHandler, SQLiteDatabase sqlDB) {
		try {
			sqlDB = dbHandler.getReadableDatabase();
		}
		catch (SQLException sqle) {
			Log.e(TAG, "Error connecting to database.", sqle);
		}

		if (sqlDB == null) {
			return null;
		}

		ArrayList <in.ccl.model.Items> itemsList = null;
		try {
			// item id one for banner items.
			String query = "select * from " + CCLDBHandler.CCL_BANNER_TABLE +" LIMIT 3;";
			cursor = sqlDB.rawQuery(query, null);
			itemsList = (cursor == null) ? null : (cursor.getCount() > 0 ? processDBCursorForItems(cursor) : null);
		}
		catch (SQLException sqle) {
			Log.e(TAG, "SQL error while retreiving records.", sqle);
		}
		catch (Exception e) {
			Log.e(TAG, "Unknown error while retreving records.", e);
		}
		finally {
			try {
				if (cursor != null) {
					cursor.close();
				}
				sqlDB.close();
			}
			catch (SQLException sqle) {
				Log.e(TAG, "SQL error while closing DB connection.", sqle);
			}
			catch (Exception e) {
				Log.e(TAG, "Unknown error while closing DB connection.", e);
			}
		}

		return itemsList;
	}

	private static ArrayList <in.ccl.model.Items> processDBCursorForItems (Cursor cursor) {
		ArrayList <in.ccl.model.Items> items = new ArrayList <in.ccl.model.Items>();
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					in.ccl.model.Items item = new in.ccl.model.Items();
					item.setId(cursor.getInt(0));
					item.setTitle(cursor.getString(1));
					item.setPhotoOrVideoUrl(cursor.getString(2));
					item.setAlbumId(cursor.getInt(3));
					items.add(item);
				}
				while (cursor.moveToNext());
			}
		}
		return items;
	}

	
}
