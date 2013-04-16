package com.paradigmcreatives.customadapterdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ListMobileActivity extends Activity {
	private String[] mobiles = new String[] { "Android", "Ios", "windows",
			"BlackBerry" };
	private ListView mListView;
	private CustomAdapterDemo mAdapterDemo;

	// private EditText editText;
	// private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listactivity);

		mListView = (ListView) findViewById(R.id.list);
		mAdapterDemo = new CustomAdapterDemo(ListMobileActivity.this, mobiles);
		mListView.setAdapter(mAdapterDemo);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {

				Toast.makeText(getApplicationContext(), "position" + pos,
						Toast.LENGTH_SHORT).show();
			}
		});

	}

}
