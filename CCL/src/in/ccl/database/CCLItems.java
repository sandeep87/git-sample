package in.ccl.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class CCLItems {

	private static final String TAG = "CCLItems";

	private static Cursor cursor;

	public static void init (CCLDBHandler dbHandler, SQLiteDatabase sqlDB) {
		try {
			sqlDB = dbHandler.getWritableDatabase();
			boolean flag = !isInit(dbHandler, sqlDB);
			if (flag) {
				String query = "insert into " + CCLDBHandler.CCL_ITEMS_TABLE + " values (?,?)";
				try {
					// inserting default items when table is created.
					sqlDB = dbHandler.getWritableDatabase();
					SQLiteStatement insertItems = sqlDB.compileStatement(query);
					insertItems.bindLong(1, 1);
					insertItems.bindString(2, "photo_album");
					insertItems.executeInsert();

					insertItems = sqlDB.compileStatement(query);
					insertItems.bindLong(1, 2);
					insertItems.bindString(2, "video_album");
					insertItems.executeInsert();

					insertItems = sqlDB.compileStatement(query);
					insertItems.bindLong(1, 3);
					insertItems.bindString(2, "photo_item");
					insertItems.executeInsert();

					insertItems = sqlDB.compileStatement(query);
					insertItems.bindLong(1, 4);
					insertItems.bindString(2, "video_item");
					insertItems.executeInsert();

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
		}
		catch (SQLException sqle) {
			Log.e(TAG, "Error connecting to database.", sqle);
		}
	}

	public static void insertPages (CCLDBHandler dbHandler, SQLiteDatabase sqlDB, int totalPages, int key,int albumId) {
		ContentValues values = new ContentValues();
		values.put(CCLDBHandler.NO_OF_PAGES, totalPages);
		try {
			sqlDB = dbHandler.getReadableDatabase();
		}
		catch (SQLException sqle) {
			Log.e(TAG, "Error connecting to database.", sqle);
		}

		if (sqlDB != null) {
			String query = "insert into " + CCLDBHandler.CCL_ITEMS_PAGES_TABLE + " values (?,?,?)";
			try {
				// inserting default items when table is created.
				sqlDB = dbHandler.getWritableDatabase();
				SQLiteStatement insertItems = sqlDB.compileStatement(query);
				insertItems.bindLong(1, key);
				insertItems.bindLong(2, albumId);
				insertItems.bindLong(3, totalPages);
				insertItems.executeInsert();
			}catch (SQLException sqle) {
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
	}

	private static boolean isInit (CCLDBHandler dbHandler, SQLiteDatabase sqlDB) {
		boolean flag = false;
		try {
			sqlDB = dbHandler.getReadableDatabase();
		}
		catch (SQLException sqle) {
			Log.e(TAG, "Error connecting to database.", sqle);
		}

		if (sqlDB == null) {
			return false;
		}

		try {
			String query = "select  * from " + CCLDBHandler.CCL_ITEMS_TABLE;
			cursor = sqlDB.rawQuery(query, null);
			System.out.println("Rajesh cursor count " + cursor.getCount());
			if (cursor.getCount() > 0) {
				flag = true;
			}
			else {
				flag = false;
			}
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

		return flag;
	}

}
