package in.ccl.database;

import in.ccl.model.Items;

import java.util.ArrayList;

import android.database.Cursor;

public class DownloadItemsCursor {

    public ArrayList<Items> getItems(Cursor cursor) {
	ArrayList<Items> list = new ArrayList<Items>();
	if (cursor != null && cursor.moveToFirst()) {
	    do {
		Items item = new Items();
		if (cursor.getInt(0) != -1)
		    item.setId(cursor.getInt(0));
		if (cursor.getString(1) != null)
		    item.setPhotoOrVideoUrl(cursor.getString(1));
		if (cursor.getString(2) != null)
		    item.setThumbUrl(cursor.getString(2));
		if (cursor.getInt(3) != -1)
		    item.setNumberOfPages(cursor.getInt(3));
		list.add(item);

	    } while (cursor.moveToNext());
	}
	return list;
    }

}
