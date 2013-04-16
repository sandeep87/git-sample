package com.paradigmcreatives.gridviewsample1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class GridViewSample1Activity extends Activity {
	private GridView gridView;
	static final String[] numbers = new String[] { "Apple", "BlackBerry",
			"Celkon", "Dish", "Eminem", "Ferral", "Gang", "Hardy", "Iphone",
			"Jockey" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_view);

		gridView = (GridView) findViewById(R.id.gridview);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				GridViewSample1Activity.this,
				android.R.layout.simple_list_item_1, numbers);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Toast.makeText(GridViewSample1Activity.this,
						((TextView) v).getText(), Toast.LENGTH_LONG).show();

			}
		});
	}
}