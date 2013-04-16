package com.paradigmcreatives.sqlitedbwithlistview;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DatabaseHelper extends SQLiteOpenHelper {

/*private static Context context;*/

	public DatabaseHelper(Context context) {
		
		super(context, "cursor demo", null, 1);
		System.out.println("Hi from database helper");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS names ("
				+ BaseColumns._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, first VARCHAR, last VARCHAR)");
		db.execSQL("Insert into names (first, last) VALUES ('Sandeep', 'Ayipilla')");
		db.execSQL("Insert into names (first, last) VALUES ('Sowjanya', 'Ayipilla')");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
