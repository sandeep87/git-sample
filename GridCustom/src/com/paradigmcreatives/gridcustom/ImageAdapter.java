package com.paradigmcreatives.gridcustom;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {
	private Context context;
	private final String[] mobileValues;

	public ImageAdapter(Context context, String[] mobileValues) {
		this.context = context;
		this.mobileValues = mobileValues;
	}
		
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mobileValues.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View gridView;
		if(convertView == null) {
			gridView = new View(context);
			gridView = inflater.inflate(R.layout.mobile, null);
			
			TextView textView = (TextView) gridView.findViewById(R.id.grid_item_label);
			textView.setText(mobileValues[position]);
			ImageView imageView = (ImageView) gridView.findViewById(R.id.grid_item_image);
			String mobile = mobileValues[position];
			
			if(mobile.equals("Windows")) {
				imageView.setImageResource(R.drawable.windows);
			}
			else if (mobile.equals("iOS")) {
				imageView.setImageResource(R.drawable.ios);
			}
			else if (mobile.equals("Blackberry")) {
				imageView.setImageResource(R.drawable.blackberry);
			}
			else if (mobile.equals("Android")) {
				imageView.setImageResource(R.drawable.ic_launcher);
			}
			else {
				imageView.setImageResource(R.drawable.ic_launcher);
			}
		}
		else {
			gridView = (View)convertView;
		}
		return gridView;
	}

	

}
