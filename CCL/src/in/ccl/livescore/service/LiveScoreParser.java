package in.ccl.livescore.service;

import in.ccl.database.JSONPullParser;

import java.io.InputStream;

import org.json.JSONException;
import org.json.JSONObject;

import com.amazonaws.util.json.JSONArray;

public class LiveScoreParser {

	public static String parseCurrentScore (InputStream inputStream) {
		String result = JSONPullParser.readStream(inputStream);
		System.out.println("JSON RESULT " + result);
		if (result != null) {
			try {
				JSONObject object = new JSONObject(result);
				if (object.has("score")) {
					return object.getString("score");
				}
			}
			catch (JSONException e) {
				System.out.println("JSON OBJECT EXCEPTIN");
				return null;
			}
		}
		return null;
	}

	public static String parseCurrentMatchSchedule (InputStream inputStream) {
		String result = JSONPullParser.readStream(inputStream);
		String matchSchedule = null;
		if (result != null) {
			try {
				JSONObject object = new JSONObject(result);
				if (object.has("matches")) {
					org.json.JSONArray jsonArray = object.getJSONArray("matches");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject innerObject = jsonArray.getJSONObject(i);
						if (innerObject.has("status") && !(innerObject.getString("status").equals("over"))) {
							matchSchedule = innerObject.getString("timestamp");
							break;
						}
					}
				}
			}
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return matchSchedule;
	}

}
