package com.pardigmcreatives.jsonparsingdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

public class JSONParser {

	static InputStream is = null;
	static JSONArray jArray = null;
	static String json = "";

	// constructor
	public JSONParser() {

	}

	public JSONArray getJSONFromUrl(String url) {
		StringBuilder sb = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		try {
			HttpResponse response = client.execute(httpget);
			StatusLine statusLine = response.getStatusLine();
			int statuscode = statusLine.getStatusCode();
			if (statuscode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						content));
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line);

				}
			} else {
				Log.e("===>>", "Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Try parse the string to json object

		try {
			jArray = new JSONArray(sb.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}

		return jArray;
	}

}
