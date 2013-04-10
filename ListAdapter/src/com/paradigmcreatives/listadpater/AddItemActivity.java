package com.paradigmcreatives.listadpater;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class AddItemActivity extends Activity {

	private ListView mList;
	private Button mAdd;
	private EditText mEdit;
	private String mNewItem;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_item);

		mList = (ListView) findViewById(R.id.list);

		final ArrayList<String> mValues = new ArrayList<String>();
		mValues.add("Paradigm");
		mValues.add("Creatives");
		mValues.add("Infotech");
		mValues.add("ParadigmCreatives");

		final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(), R.layout.list_item, R.id.item, mValues);

		mAdd = (Button) findViewById(R.id.add);
		mAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mEdit = (EditText) findViewById(R.id.edit);
				mNewItem = mEdit.getText().toString();
				mValues.add(mNewItem);
				adapter.notifyDataSetChanged();
				mEdit.setText("");
			}
		});

		mList.setAdapter(adapter);

		mList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> a, View view, int position,
					long id) {
				Toast.makeText(getApplicationContext(),
						"Clicked on " + mValues.get(position),
						Toast.LENGTH_LONG).show();
			}
		});

		mList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> a, View view,
					final int position, long id) {

				final String[] mOptions = new String[] { "Delete", "Edit",
						"Option 1", "Option 2" };

				AlertDialog.Builder alert = new AlertDialog.Builder(
						AddItemActivity.this);
				alert.setTitle(mValues.get(position));
				alert.setItems(mOptions, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (mOptions[which] == "Delete") {
							mValues.remove(position);
							adapter.notifyDataSetChanged();
						} else {
							Toast.makeText(AddItemActivity.this,
									"Clicked on " + mOptions[which],
									Toast.LENGTH_LONG).show();
						}
					}
				});

				AlertDialog alertDialog = alert.create();
				alertDialog.show();

				return true;
			}
		});

	}

}
