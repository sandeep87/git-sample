package in.ccl.database;

import in.ccl.model.Items;

import java.util.ArrayList;

import android.database.Cursor;

public class BannerCursor {

	public static ArrayList <Items> getItems (Cursor cursor) {
		ArrayList <Items> list = new ArrayList <Items>();
		if (cursor.moveToFirst()) {
			do {
				Items item = new Items();
				item.setId(cursor.getInt(0));
				item.setPhotoOrVideoUrl(cursor.getString(1));
				item.setTitle(cursor.getString(2));
				item.setAlbumId(cursor.getInt(3));
				list.add(item);
			}
			while (cursor.moveToNext());
		}
		return list;

	}

}
