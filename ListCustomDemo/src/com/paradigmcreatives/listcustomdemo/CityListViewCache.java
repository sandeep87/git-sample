package com.paradigmcreatives.listcustomdemo;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

class CityListViewCache {
	private View baseView;
	private TextView textNameCity;
	private TextView textWikiCity;
	private ImageView imageCity;

	public CityListViewCache(View baseView) {

		this.baseView = baseView;
	}

	public View getViewBase() {
		return baseView;
	}

	public TextView getTextNameCity(int resource) {
		if (textNameCity == null) {
			textNameCity = (TextView) baseView.findViewById(R.id.cityname);
		}
		return textNameCity;
	}

	public TextView getTextWikiCity(int resource) {
		if (textWikiCity == null) {
			textWikiCity = (TextView) baseView.findViewById(R.id.citylinkwiki);
		}
		return textWikiCity;
	}

	public ImageView getImageView(int resource) {
		if (imageCity == null) {
			imageCity = (ImageView) baseView.findViewById(R.id.imagecity);
		}
		return imageCity;
	}
}
