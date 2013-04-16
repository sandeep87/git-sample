package ar.com.c0de.library.test;

import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import ar.com.c0de.Json;
import ar.com.c0de.OnHttpCallListener;
import ar.com.c0de.http;

import ar.com.c0de.object.Access;
import ar.com.c0de.object.User;

public class Test extends Activity implements OnHttpCallListener {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	
		URL url;

		try {

			url = new URL("http://186.19.91.207/android-library-json2.php");

			http.call(this, url);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();			
		}
    	
    }

	public void onHttpCallCancel() {
		// TODO Auto-generated method stub
		Log.d("TEST", "onHttpCallCancel");
	}

	public void onHttpCallComplete(String arg0) {
		// TODO Auto-generated method stub
		
		Log.d("TEST", arg0);
		
		try {

			User user = Json.getObjectFromJson(
				new JSONObject(arg0), 
				User.class
			);

			Log.d("TEST", "**************************************************");

			Log.d("TEST", "Name: " + user.Name);
			Log.d("TEST", "Pass: " + user.Pass);

			if (user.access != null) {
				for (Access ua : user.access) {
					Log.d("TEST", "idSector : " + String.valueOf(ua.idSector));
				}
			}

			Log.d("TEST", "**************************************************");

		} catch (Exception e) {

			Log.d("TEST", e.toString());

		}
	}

	public void onHttpCallProgress(URL arg0, Integer... arg1) {
		// TODO Auto-generated method stub
		Log.d("TEST", "onHttpCallProgress");
	}
	
}