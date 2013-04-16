package com.pardigmcreatives.jsonparsingdemo;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MainActivity extends ListActivity {

	private Context context;
	private static String url = "http://docs.blackberry.com/sampledata.json";
	private static final String TAG_VTYPE = "vehicleType";
	private static final String TAG_VCOLOR = "VehicleColor";
	private static final String TAG_FUEL = "fuel";
	private static final String TAG_TREAD = "treadtype";
	private static final String TAG_OPERATOR = "approved operators";
	private static final String TAG_NAME = "name";
	private static final String TAG_POINTS = "experiencepoints";

	ArrayList<HashMap<String, String>> jsonList = new ArrayList<HashMap<String, String>>();
	ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new ProgressTask(MainActivity.this).execute();

	}

	private class ProgressTask extends AsyncTask<String, Void, Boolean> {

		private ProgressDialog progressDialog;
		private ListActivity mListActivity;

		public ProgressTask(ListActivity mListActivity) {
			this.mListActivity = mListActivity;
			context = mListActivity;
			progressDialog = new ProgressDialog(context);
		}

		private Context context;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			this.progressDialog.setMessage("Progress Start");
			this.progressDialog.show();
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			ListAdapter adapter = new SimpleAdapter(context, jsonList,
					R.layout.list_item, new String[] { TAG_VTYPE, TAG_VCOLOR,
							TAG_TREAD, TAG_FUEL, TAG_NAME }, new int[] {
							R.id.vehicleType, R.id.vehicleColor, R.id.fuel,
							R.id.treadType });
			setListAdapter(adapter);
			mListView = getListView();
		}

		@Override
		protected Boolean doInBackground(final String... args) {
			JSONParser jParser = new JSONParser();

			// Getting json string from url
			JSONArray json = jParser.getJSONFromUrl(url);

			for (int i = 0; i < json.length(); i++) {
				try {
					JSONObject jobj = json.getJSONObject(i);
					String vType = jobj.getString(TAG_VTYPE);
					String vColor = jobj.getString(TAG_VCOLOR);
					String fuel = jobj.getString(TAG_FUEL);
					String tread = jobj.getString(TAG_TREAD);

					HashMap<String, String> map = new HashMap<String, String>();

					// Adding each child node to hashmap key value
					map.put(TAG_VTYPE, vType);
					map.put(TAG_VCOLOR, vColor);
					map.put(TAG_FUEL, fuel);
					map.put(TAG_TREAD, tread);
					jsonList.add(map);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}

	}
}
