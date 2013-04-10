package com.paradigmcreatives.planetlistview;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PlanetListViewActivity extends Activity {
	/** Called when the activity is first created. */
	private ListView planetListView;
	private ArrayAdapter<String> listAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Find the list view resource
		planetListView = (ListView) findViewById(R.id.planet_list_view);
		// Create and populate a List of planet names.
		String[] planets = { "Mercury", "Venus", "Earth", "Mars", "Jupiter",
				"Saturn", "Uranus", "Neptune" };
		ArrayList<String> planetList = new ArrayList<String>();
		planetList.addAll(Arrays.asList(planets));

		// Create ArrayAdapter using the planet list.
		listAdapter = new ArrayAdapter<String>(this, R.layout.rowlist,
				planetList);

		// Add more planets. If you passed a String[] instead of a List<String>
		// into the ArrayAdapter constructor.
		listAdapter.add("Ceres");
		listAdapter.add("Pluto");
		listAdapter.add("Haumea");
		listAdapter.add("Makemake");
		listAdapter.add("Eris");

		// Set the ArrayAdapter as the ListView's adapter.
		planetListView.setAdapter(listAdapter);
		planetListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(),
						"You chose the option" + position, Toast.LENGTH_LONG)
						.show();

			}
		});
	}
}
