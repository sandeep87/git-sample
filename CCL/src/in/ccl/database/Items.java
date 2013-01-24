package in.ccl.database;

import in.ccl.util.Constants;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class Items {

	private static Cursor cursor;

	private static final String TAG = "Items";

	public static ArrayList <in.ccl.model.Items> getItems (CCLDBHandler dbHandler, SQLiteDatabase sqlDB,int album_id, int id) {
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
		int no_of_pages = 0;
		if(id ==  Constants.PHOTO_ITEMS ||id== Constants.VIDEO_ITEMS){
		String query = "select "+CCLDBHandler.NO_OF_PAGES+" from "+CCLDBHandler.CCL_ITEMS_PAGES_TABLE+" where "+CCLDBHandler.ALBUM_ID+" = "+album_id+" and "+CCLDBHandler.ID+" = "+id;
		cursor = sqlDB.rawQuery(query,null);
	  if(cursor.getCount()>0){
	  	no_of_pages =  cursor.getInt(0);
	  }
		}
		try {
			// item id one for banner items.
			String query = "select * from " + CCLDBHandler.CCL_INFO_TABLE + " where " + CCLDBHandler.ITEM_ID + " = " + id +" and "+CCLDBHandler.ALBUM_ID+" = "+album_id;

			cursor = sqlDB.rawQuery(query, null);
			itemsList = (cursor == null) ? null : (cursor.getCount() > 0 ? processDBCursorForItems(cursor,no_of_pages) : null);
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

	private static ArrayList <in.ccl.model.Items> processDBCursorForItems (Cursor cursor,int numberOfPages) {
		ArrayList <in.ccl.model.Items> items = new ArrayList <in.ccl.model.Items>();
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					in.ccl.model.Items item = new in.ccl.model.Items();
					item.setId(cursor.getInt(0));
					item.setNumberOfPages(numberOfPages);
					System.out.println("No of pages "+numberOfPages);
					item.setTitle(cursor.getString(1));
					item.setPhotoOrVideoUrl(cursor.getString(2));
					item.setThumbUrl(cursor.getString(3));
					
					items.add(item);
				}
				while (cursor.moveToNext());
			}
		}
		return items;
	}

	/**
	 * Should provide ArrayList <in.ccl.model.Items> to be insert. And itemId is for differentiating which item(banner item,photo item,video item etc.)
	 * 
	 * @param dbHandler
	 * @param sqlDB
	 * @param list
	 * @param itemId
	 */
	public static void insertItems (CCLDBHandler dbHandler, SQLiteDatabase sqlDB, ArrayList <in.ccl.model.Items> list, int itemId) {

		try {
			sqlDB = dbHandler.getWritableDatabase();
			String query = "insert into " + CCLDBHandler.CCL_INFO_TABLE + " values (?,?,?,?,?,?)";
			try {
				// inserting banner items.
				SQLiteStatement insertDoodle = sqlDB.compileStatement(query);
				for (in.ccl.model.Items items : list) {
					insertDoodle.bindLong(1, items.getId());
					insertDoodle.bindString(2, items.getTitle() == null ? "" : items.getTitle());
					insertDoodle.bindString(3, items.getPhotoOrVideoUrl() == null ? "" : items.getPhotoOrVideoUrl());
					insertDoodle.bindString(4, items.getThumbUrl() == null ? "" : items.getThumbUrl());
					insertDoodle.bindLong(5, items.getAlbumId());
					insertDoodle.bindLong(6, itemId);
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


}
