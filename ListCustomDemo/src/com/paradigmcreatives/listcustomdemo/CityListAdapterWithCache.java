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

public class CityListAdapterWithCache extends ArrayAdapter<City> {
	private int resource;
	private LayoutInflater inflater;
	private Context context;

	public CityListAdapterWithCache(Context mContext, int resourceId,
			List<City> objects) {
		super(mContext, resourceId, objects);
		resource = resourceId;
		inflater = LayoutInflater.from(mContext);
		context = mContext;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		City city = getItem(position);
		CityListViewCache viewCache;

		if (convertView == null) {
			convertView = (RelativeLayout) inflater.inflate(resource, null);
			viewCache = new CityListViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			convertView = (RelativeLayout) convertView;
			viewCache = (CityListViewCache) convertView.getTag();
		}

		TextView txtName = viewCache.getTextNameCity(resource);
		txtName.setText(city.getName());

		TextView txtWiki = viewCache.getTextWikiCity(resource);
		txtWiki.setText(city.getUrlwiki());

		ImageView imageCity = viewCache.getImageView(resource);
		String uri = "drawable/" + city.getImage();
		int imageResource = context.getResources().getIdentifier(uri, null,
				context.getPackageName());
		Drawable image = context.getResources().getDrawable(imageResource);
		imageCity.setImageDrawable(image);
		return convertView;
	}

}
