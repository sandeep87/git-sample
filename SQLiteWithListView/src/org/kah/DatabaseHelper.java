/** 
 * The code written in this file was intended to serve as an EXAMPLE. You are
 * free to modify and use code in this file. However, the this is also provided 
 * without ANY WARRANTY, EXPRESSED OR IMPLIED, INCLUDING ANY WARRANTIES OR 
 * CONDITIONS OF MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSES. Users of 
 * the code contained in this file assume the risks and costs of, including but
 * not limited to, any program errors, data loss or interruption of operations.
 *
 * This was written for the Android "Using the SQLite Database with ListView" tutorial at:
 * http://kahdev.wordpress.com/2010/09/27/android-using-the-sqlite-database-with-listview
 */
package org.kah;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Subclass of the {@link SQLiteOpenHelper} that sets up the database for the
 * demo.
 * 
 * @author Kah
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context) {
		super(context, "CursorDemo", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS names ("
				+ BaseColumns._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, first VARCHAR, last VARCHAR)");
		db.execSQL("INSERT INTO names (first, last) VALUES ('John', 'Doe')");
		db.execSQL("INSERT INTO names (first, last) VALUES ('James', 'Kirk')");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Steps to upgrade the database for the new version ...
	}
}
