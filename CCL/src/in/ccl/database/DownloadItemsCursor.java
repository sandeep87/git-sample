package in.ccl.database;

import in.ccl.model.Items;

import java.util.ArrayList;

import android.database.Cursor;

public class DownloadItemsCursor {
	
	
	public static ArrayList <Items> getItems (Cursor cursor) {
		ArrayList <Items> list = new ArrayList <Items>();
		if (cursor != null && cursor.moveToFirst()) {
			do {
				Items item = new Items();
				item.setId(cursor.getInt(0));
				item.setPhotoOrVideoUrl(cursor.getString(1));
				item.setThumbUrl(cursor.getString(2));
				item.setNumberOfPages(cursor.getInt(3));
				list.add(item);

			}
			while (cursor.moveToNext());
		}
		if (cursor != null)
			cursor.close();
		return list;
	}

	
	
	
}
