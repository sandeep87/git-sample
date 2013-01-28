package in.ccl.database;

import in.ccl.model.Items;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;

public class PhotoAlbumCurosr {

	public static ArrayList <Items> getItems (Cursor cursor) {
		ArrayList <Items> list = new ArrayList <Items>();
		if (cursor != null && cursor.moveToFirst()) {
			do {
				Items item = new Items();
				item.setId(cursor.getInt(0));
				item.setPhotoOrVideoUrl(cursor.getString(1));
				item.setTitle(cursor.getString(2));
				list.add(item);
			}
			while (cursor.moveToNext());
		}
		return list;

	}

	public static ArrayList <Items> getPhotos (Context ctx, int albumId) {
		System.out.println("Alubum Id "+albumId);
		String condition = DataProviderContract.ALBUM_ID_COLUMN + " = " + albumId + " and " + DataProviderContract.CATEGORY_ID + " = 1";
		Cursor cursor = ctx.getContentResolver().query(DataProviderContract.PAGES_TABLE_CONTENTURI, new String[] { DataProviderContract.TOTAL_PAGES }, condition, null, null);
		int totalPages = 0;
		if (cursor != null && cursor.getCount() > 0) {
			if (cursor.moveToFirst()) {
				totalPages = cursor.getInt(0);
			}
		}
		cursor = ctx.getContentResolver().query(DataProviderContract.RAW_TABLE_CONTENTURI, null, condition, null, null);
		ArrayList <Items> list = new ArrayList <Items>();
		if (cursor != null && cursor.moveToFirst()) {
			do {
				Items item = new Items();
				item.setNumberOfPages(totalPages);
				item.setId(cursor.getInt(0));
				item.setPhotoOrVideoUrl(cursor.getString(1));
				item.setThumbUrl(cursor.getString(2));
				list.add(item);
			}
			while (cursor.moveToNext());
		}
		if (cursor != null)
			cursor.close();
		System.out.println("List size "+list.size());
		return list;

	}

}
