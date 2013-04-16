package com.sandeep.listviewdemo;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

public class MainActivity extends ListActivity {

	ArrayList<String> listItems = new ArrayList<String>();
	ArrayAdapter<String> adapter;
	int clickCounter = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		adapter = new ArrayAdapter<String>(MainActivity.this,
				android.R.layout.simple_list_item_1, listItems);
		setListAdapter(adapter);
	}

	public void addItems(View v) {
		listItems.add("Clicked :" + clickCounter++);
		adapter.notifyDataSetChanged();
	}

}
