package com.paradigmcreatives.jsonparsing;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends ListActivity {

	private static String url = "http://api.androidhive.info/contacts/";

	private static final String TAG_CONTACTS = "contacts";
	private static final String TAG_ID = "id";
	private static final String TAG_NAME = "name";
	private static final String TAG_EMAIL = "email";
	private static final String TAG_ADDRESS = "address";
	private static final String TAG_GENDER = "gender";
	private static final String TAG_PHONE = "phone";
	private static final String TAG_PHONE_MOBILE = "mobile";
	private static final String TAG_PHONE_HOME = "home";
	private static final String TAG_PHONE_OFFICE = "office";

	JSONArray contacts = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ArrayList<HashMap<String, String>> contactList = new ArrayList<HashMap<String, String>>();

		JSONParser jParser = new JSONParser();

		JSONObject json = jParser.getJSONFromUrl(url);

		try {
			contacts = json.getJSONArray(TAG_CONTACTS);

			for (int i = 0; i < contacts.length(); i++) {
				JSONObject c = contacts.getJSONObject(i);

				String id = c.getString(TAG_ID);
				String name = c.getString(TAG_NAME);
				String email = c.getString(TAG_EMAIL);
				String address = c.getString(TAG_ADDRESS);
				String gender = c.getString(TAG_GENDER);

				JSONObject phone = c.getJSONObject(TAG_PHONE);

				String mobile = phone.getString(TAG_PHONE_MOBILE);
				String home = phone.getString(TAG_PHONE_HOME);
				String office = phone.getString(TAG_PHONE_OFFICE);

				HashMap<String, String> map = new HashMap<String, String>();

				map.put(TAG_ID, id);
				map.put(TAG_NAME, name);
				map.put(TAG_EMAIL, email);
				map.put(TAG_PHONE_MOBILE, mobile);

				contactList.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		ListAdapter adapter = new SimpleAdapter(MainActivity.this, contactList,
				R.layout.list_item, new String[] { TAG_NAME, TAG_EMAIL,
						TAG_PHONE_MOBILE }, new int[] { R.id.name, R.id.email,
						R.id.mobile_tv });
		setListAdapter(adapter);
		ListView lv = getListView();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String name = ((TextView)view.findViewById(R.id.name)).getText().toString();
				String cost = ((TextView) view.findViewById(R.id.email)).getText().toString();
				String description = ((TextView) view.findViewById(R.id.mobile_tv))
						.getText().toString();

				Intent intent = new Intent(MainActivity.this,
						SingleMenuItemActivity.class);
				intent.putExtra(TAG_NAME, name);
				intent.putExtra(TAG_EMAIL, cost);
				intent.putExtra(TAG_PHONE_MOBILE, description);
				startActivity(intent);

			}
		});

	}

}
