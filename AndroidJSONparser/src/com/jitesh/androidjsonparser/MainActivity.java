package com.jitesh.androidjsonparser;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends ListActivity {
	private Context context;
	private static String url = "http://docs.blackberry.com/sampledata.json";

	private static final String TAG_VTYPE = "vehicleType";
	private static final String TAG_VCOLOR = "vehicleColor";
	private static final String TAG_FUEL = "fuel";
	private static final String TAG_TREAD = "treadType";
	private static final String TAG_OPERATOR = "approvedOperators";
	private static final String TAG_NAME = "name";
	private static final String TAG_POINTS = "experiencePoints";

	ArrayList<HashMap<String, String>> jsonlist = new ArrayList<HashMap<String, String>>();
	
	ListView lv ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new ProgressTask(MainActivity.this).execute();
	}

	private class ProgressTask extends AsyncTask<String, Void, Boolean> {
		private ProgressDialog dialog;

		private ListActivity activity;

		// private List<Message> messages;
		public ProgressTask(ListActivity activity) {
			this.activity = activity;
			context = activity;
			dialog = new ProgressDialog(context);
		}

		/** progress dialog to show user that the backup is processing. */

		/** application context. */
		private Context context;

		protected void onPreExecute() {
			this.dialog.setMessage("Progress start");
			this.dialog.show();
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
			ListAdapter adapter = new SimpleAdapter(context, jsonlist,
					R.layout.list_item, new String[] { TAG_VTYPE, TAG_VCOLOR,
							TAG_FUEL, TAG_TREAD }, new int[] {
							R.id.vehicleType, R.id.vehicleColor, R.id.fuel,
							R.id.treadType });

			setListAdapter(adapter);

			// selecting single ListView item
			 lv = getListView();
			
			

			

			
		}

		protected Boolean doInBackground(final String... args) {

			JSONParser jParser = new JSONParser();

			// getting JSON string from URL
			JSONArray json = jParser.getJSONFromUrl(url);

			for (int i = 0; i < json.length(); i++) {

				try {
					JSONObject c = json.getJSONObject(i);
					String vtype = c.getString(TAG_VTYPE);

					String vcolor = c.getString(TAG_VCOLOR);
					String vfuel = c.getString(TAG_FUEL);
					String vtread = c.getString(TAG_TREAD);

					HashMap<String, String> map = new HashMap<String, String>();

					// adding each child node to HashMap key => value
					map.put(TAG_VTYPE, vtype);
					map.put(TAG_VCOLOR, vcolor);
					map.put(TAG_FUEL, vfuel);
					map.put(TAG_TREAD, vtread);
					jsonlist.add(map);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			return null;

		}

	}

}
