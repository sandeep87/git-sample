package com.example.customweather;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends Activity {
	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Weather weather_data[] = new Weather[] {
				new Weather(R.drawable.ic_launcher, "Cloudy"),
				new Weather(R.drawable.ic_launcher, "Showers"),
				new Weather(R.drawable.ic_launcher, "Snow"),
				new Weather(R.drawable.ic_launcher, "Storm"),
				new Weather(R.drawable.ic_launcher, "Sunny") };

		WeatherAdapter adapter = new WeatherAdapter(this,
				R.layout.listview_item_row, weather_data);

		mListView = (ListView) findViewById(R.id.listview1);
		View header = (View)getLayoutInflater().inflate(R.layout.listview_header_row,
				null);
		mListView.addHeaderView(header);
		mListView.setAdapter(adapter);
	}

	
}
