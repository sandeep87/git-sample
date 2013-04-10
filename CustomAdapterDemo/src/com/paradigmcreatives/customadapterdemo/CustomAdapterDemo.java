package com.paradigmcreatives.customadapterdemo;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CustomAdapterDemo extends BaseAdapter {
	private String[] listItems;
	private Context mContext;
	private EditText mEditText;
	private Button mButton;
	
    public CustomAdapterDemo(ListMobileActivity listMobileActivity,
			String[] mobiles) {
    	mContext = listMobileActivity;
		listItems = mobiles;
	}
	
	@Override
	public int getCount() {
		return listItems.length;
	}

	@Override
	public String[] getItem(int position) {
		return listItems;
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
		tv.setText(listItems[position]);
		return convertView;
	}

//	public void add(String string) {
//		
//		if (TextUtils.isEmpty(mEditText.getText().toString())) {
//
//			adapter.add(mEditText.getText().toString());
//			adapter.notifyDataSetChanged();
//		}
//
//	}

}