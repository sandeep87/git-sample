package com.paradigmcreatives.sqlitedbwithlistview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class SqliteDbWithListViewActivity extends ListActivity {

	private static final int DIALOG_ID = 100;
	private SQLiteDatabase database;
	private CursorAdapter datasource;
	private View entryView;
	private EditText feditText;
	private EditText leditText;
	private static final String fields[] = { "first", "last", BaseColumns._ID };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		DatabaseHelper helper = new DatabaseHelper(this);
		database = helper.getWritableDatabase();
		System.out.println("database:"+database);
		Cursor data = database.query("names", fields, null, null, null, null,
				null);
		datasource = new SimpleCursorAdapter(SqliteDbWithListViewActivity.this,
				R.layout.row, data, fields, new int[] { R.id.first, R.id.last });
		ListView view = getListView();
		view.setHeaderDividersEnabled(true);
		view.addHeaderView(getLayoutInflater().inflate(R.layout.row, null));
		setListAdapter(datasource);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, DIALOG_ID, 1, R.string.addItem);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == DIALOG_ID) {
			showDialog(DIALOG_ID);
		}
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		entryView = getLayoutInflater().inflate(R.layout.main, null);
		builder.setView(entryView);
		feditText = (EditText) findViewById(R.id.firstname);
		leditText = (EditText) findViewById(R.id.lastname);
		builder.setTitle(R.string.addDialogTitle);
		builder.setPositiveButton(R.string.addItem, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				ContentValues values = new ContentValues();
				values.put("first", feditText.getText().toString());
				values.put("last", leditText.getText().toString());
				database.insert("names", null, values);
				datasource.getCursor().requery();

			}
		});
		builder.setNegativeButton(R.string.cancelItem, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		return builder.create();
	}
}