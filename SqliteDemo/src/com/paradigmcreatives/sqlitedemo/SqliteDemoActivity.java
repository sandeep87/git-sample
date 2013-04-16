package com.paradigmcreatives.sqlitedemo;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SqliteDemoActivity extends Activity {
	/** Called when the activity is first created. */
	private SQLiteAdapter mySQliteAdapter;
	private TextView mTextView;
	private EditText mEditText;
	private Button insert_btn;
	private Button delete_btn;
	private ListView lv;
    private ArrayList<String> itemArrey;
	private ArrayAdapter<String> adapter;
	private String cr;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mTextView = (TextView) findViewById(R.id.content_list);
		insert_btn = (Button) findViewById(R.id.add_btn);
		lv = (ListView) findViewById(R.id.list);
		itemArrey = new ArrayList<String>();
        itemArrey.clear();
		adapter = new ArrayAdapter<String>(SqliteDemoActivity.this, android.R.layout.simple_list_item_1,itemArrey);
		lv.setAdapter(adapter);

		mySQliteAdapter = new SQLiteAdapter(SqliteDemoActivity.this);
		

		insert_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mEditText = (EditText) findViewById(R.id.insertdata);
				cr = mEditText.getText().toString();
				
				addItemList();
				mTextView.setText(cr);
				mySQliteAdapter.openToWrite();
				mySQliteAdapter.insert(cr);
				mTextView.setText(cr);
				adapter.notifyDataSetChanged();
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
				adapter.notifyDataSetChanged();
				mySQliteAdapter.close();
			}
		});

	}

	protected void addItemList() {
		if(cr != null) {
			itemArrey.add(mEditText.getText().toString());
			mEditText.setText("");
		}
		
	}
}