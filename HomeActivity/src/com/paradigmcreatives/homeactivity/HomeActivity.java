package com.paradigmcreatives.homeactivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class HomeActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1,
				this.fetchTwisterPublicTimeLine()));
	}
	
	public ArrayList<String> fetchTwisterPublicTimeLine() {
		ArrayList<String> listItems = new ArrayList<String>();
		
		try {
			URL twitter = new URL("http://twitter.com/statuses/public_timeline.json");
			URLConnection tc = twitter.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(tc.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				JSONArray ja = new JSONArray(line);
				for (int i = 0; i < ja.length(); i ++) {
					JSONObject jo = (JSONObject) ja.get(i);
					listItems.add(jo.getString("text"));
				}
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listItems;
		
	}

}
