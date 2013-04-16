package com.paradigmcreatives.listcustomdemo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends Activity {
	private ListView mListView;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		List<City> listCity = new ArrayList<City>();

		listCity.add(new City("India", "http://en.wikipedia.org/wiki/India",
				"india"));
		listCity.add(new City("Japan", "http://en.wikipedia.org/wiki/Japan",
				"japan"));
		listCity.add(new City("London", "http://en.wikipedia.org/wiki/London",
				"london"));
		listCity.add(new City("Rome", "http://en.wikipedia.org/wiki/Rome",
				"rome"));
		listCity.add(new City("Paris", "http://en.wikipedia.org/wiki/Paris",
				"paris"));
		listCity.add(new City("Australia",
				"http://en.wikipedia.org/wiki/Australia", "australia"));
		listCity.add(new City("U.S.A", "http://en.wikipedia.org/wiki/USA",
				"usa"));
		listCity.add(new City("Srilanka",
				"http://en.wikipedia.org/wiki/Srilanka", "srilanka"));
		listCity.add(new City("Germany",
				"http://en.wikipedia.org/wiki/Germany", "germany"));
		listCity.add(new City("SouthAfrica",
				"http://en.wikipedia.org/wiki/South_Africa", "southafrica"));
		listCity.add(new City("Singapore",
				"http://en.wikipedia.org/wiki/Singapore", "singapore"));

		mListView = (ListView) findViewById(R.id.city_list);
		mListView.setAdapter(new CityListAdapterWithCache(mContext,
				R.layout.city_row_item, listCity));
	}

}
