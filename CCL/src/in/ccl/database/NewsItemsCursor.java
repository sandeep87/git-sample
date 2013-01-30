package in.ccl.database;

import in.ccl.model.Items;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;


public class NewsItemsCursor {

	public static ArrayList <Items> getItems (Cursor cursor) {
		ArrayList <Items> list = new ArrayList <Items>();
		if (cursor != null && cursor.moveToFirst()) {
			do {
				Items item = new Items();
				item.setId(cursor.getInt(0));
				item.setPhotoOrVideoUrl(cursor.getString(2));
				item.setTitle(cursor.getString(1));
				item.setAlbumId(cursor.getInt(3));
				list.add(item);
			}
			while (cursor.moveToNext());
		}
		return list;
	}
	
	public static ArrayList <Items> getNews (Context ctx, int albumId) {
		String condition = DataProviderContract.NEWS_CATEGORY + " = " + albumId ;
		Cursor		cursor = ctx.getContentResolver().query(DataProviderContract.NEWS_TABLE_CONTENTURI, null, condition, null, null);
		ArrayList <Items> list = new ArrayList <Items>();
		if (cursor != null && cursor.moveToFirst()) {
			do {
				Items item = new Items();
			
				item.setId(cursor.getInt(0));
				item.setTitle(cursor.getString(1));
				item.setPhotoOrVideoUrl(cursor.getString(2));
				
				list.add(item);
			}
			while (cursor.moveToNext());
		}
		if (cursor != null)
			cursor.close();
		return list;

	}


}
