package com.example.customweather;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WeatherAdapter extends ArrayAdapter<Weather> {
	
	private Context context;
	private int layoutresourceid;
	private Weather data[] = new Weather[]{};

	public WeatherAdapter(Context context, int layoutresourceid,
			Weather[] data) {
		
		super(context, layoutresourceid, data);
		System.out.println("constructor");
		this.context = context;
		this.layoutresourceid = layoutresourceid;
		this.data = data;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View row = convertView;
		WeatherHolder holder ;
		System.out.println("get view");
		
		if (row == null) {
			
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutresourceid, parent, false);
			
			holder = new WeatherHolder();
			holder.imageicon = (ImageView) row.findViewById(R.id.imageicon);
			holder.textview = (TextView) row.findViewById(R.id.textview2);
			row.setTag(holder);
		}
		
		else {
			holder = (WeatherHolder) row.getTag();
		}
		
		Weather weather = data[position];
	
		 holder.textview.setText(weather.title);
	        holder.imageicon.setImageResource(weather.icon);
		
		return row;
	
	}
	
	static class WeatherHolder {

		public   TextView textview ;
		public  ImageView imageicon;
			
	}

}
