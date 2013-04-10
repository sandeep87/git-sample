package com.paradigmcreatives.sqlitedemo;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SqliteDemoActivity extends Activity {
	/** Called when the activity is first created. */
	private SQLiteAdapter mySQliteAdapter;
	private TextView mTextView;
	private EditText mEditText;
	private Button insert_btn;
	private Button delete_btn;
	String cr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mTextView = (TextView) findViewById(R.id.content_list);
		insert_btn = (Button) findViewById(R.id.add_btn);

		mySQliteAdapter = new SQLiteAdapter(SqliteDemoActivity.this);

		insert_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mEditText = (EditText) findViewById(R.id.insertdata);
				cr = mEditText.getText().toString();
				mTextView.setText(cr);
				mySQliteAdapter.openToWrite();
				mySQliteAdapter.insert(cr);
				mySQliteAdapter.close();
				mEditText.setText("");
			}
		});

		delete_btn = (Button) findViewById(R.id.del_btn);
		delete_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mySQliteAdapter.openToRead();
				Cursor contentRead = mySQliteAdapter.queueAll();
				mySQliteAdapter.deleteAll();
				mySQliteAdapter.close();
			}
		});

		

	}
}