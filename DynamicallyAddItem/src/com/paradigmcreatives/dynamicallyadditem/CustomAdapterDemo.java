package com.paradigmcreatives.dynamicallyadditem;

import java.util.ArrayList;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import android.widget.EditText;
import android.widget.TextView;

public class CustomAdapterDemo extends BaseAdapter {
	private ArrayList<String>listItems;
	private Context mContext;
	private EditText mEditText;
	//private Button mButton;
	ArrayAdapter<String> adapter;

	
	public CustomAdapterDemo(DynamicallyAddItemActivity listMobileActivity,
			ArrayList<String> list) {
		mContext = listMobileActivity;
		listItems = list;
	}

	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = (View) LayoutInflater.from(mContext).inflate(
					R.layout.custom, null);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.label);
		tv.setText(listItems.get(position));
		return convertView;
	}

	public void add(String string) {
		
		if (TextUtils.isEmpty(mEditText.getText().toString())) {

			adapter.add(mEditText.getText().toString());
			adapter.notifyDataSetChanged();
			
		}

	}

}