package com.paradigmcreatives.listcustomdemo;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CityListAdapter extends ArrayAdapter<City> {
	private int resource;
	private LayoutInflater inflator;
	private Context context;

	public CityListAdapter(Context mContext, int resourceId, List<City> objects) {
		super(mContext, resourceId, objects);
		resource = resourceId;
		inflator = LayoutInflater.from(mContext);
		context = mContext;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		System.out.println("I am in getView");

		convertView = (RelativeLayout) inflator.inflate(resource, null);
		City city = getItem(position);

		TextView txtName = (TextView) convertView.findViewById(R.id.cityname);
		txtName.setText(city.getName());

		TextView txtUrl = (TextView) convertView
				.findViewById(R.id.citylinkwiki);
		txtUrl.setText(city.getUrlwiki());

		ImageView imageCity = (ImageView) convertView
				.findViewById(R.id.imagecity);
		String uri = "drawable/" + city.getImage();
		int imageResource = context.getResources().getIdentifier(uri, null,
				context.getPackageName());
		System.out.println("Image Resource :" + imageResource);
		System.out.println("URI :" + uri);

		Drawable image = context.getResources().getDrawable(imageResource);
		System.out.println("Image :" + image);
		if (image != null) {
			imageCity.setImageDrawable(image);
		}

		return convertView;
	}

}
