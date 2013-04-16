package com.paradigmcreatives.sqlitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteAdapter {

	public static final String MYDATABASE_NAME = "MY_DATABASE";
	public static final String MYDATABASE_TABLE = "MY_TABLE";
	public static final int MYDATABASE_VERSION = 1;
	public static final String KEY_ID = "_id";
	public static final String KEY_CONTENT = "Content";

	public static final String SCRIPT_CREATE_DATABASE = "create table "
			+ MYDATABASE_TABLE + "(" + KEY_ID
			+ " integer primary key autoincrement," + KEY_CONTENT
			+ " Text not null);";
	private SQLiteHelper sqlitehelper;
	private SQLiteDatabase sqlitedatabase;
	private Context mContext;

	public SQLiteAdapter(Context context) {
		mContext = context;
	}

	public SQLiteAdapter openToWrite() {

		sqlitehelper = new SQLiteHelper(mContext, MYDATABASE_NAME, null,
				MYDATABASE_VERSION);
		System.out.println("sqlitehelper"+""+sqlitehelper);
		sqlitedatabase = sqlitehelper.getWritableDatabase();
		return this;

	}

	public long insert(String content) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_CONTENT, content);
		return sqlitedatabase.insert(MYDATABASE_TABLE, null, contentValues);

	}

	public int deleteAll() {
		
		return sqlitedatabase.delete(MYDATABASE_TABLE, null, null);

	}

	public void close() {
		sqlitehelper.close();

	}

	public SQLiteAdapter openToRead() {

		sqlitehelper = new SQLiteHelper(mContext, MYDATABASE_NAME, null,
				MYDATABASE_VERSION);
		sqlitedatabase = sqlitehelper.getReadableDatabase();
		return this;

	}

	public Cursor queueAll() {
		String[] columns = new String[] { KEY_ID, KEY_CONTENT };
		Cursor cursor = sqlitedatabase.query(MYDATABASE_TABLE, columns, null,
				null, null, null, null);

		return cursor;
	}

	public class SQLiteHelper extends SQLiteOpenHelper {

		public SQLiteHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SCRIPT_CREATE_DATABASE);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
		}

	}

}
