package com.netcomps.oauth_example;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class OAuthMain extends Activity {

		
	private SharedPreferences prefs;
	TextView console;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        
        console = (TextView) findViewById(R.id.text_console);

        Button launchOauth = (Button) findViewById(R.id.button_start_oauth);
        Button clearCredentials = (Button) findViewById(R.id.button_delete_tokens);
        Button getContacts = (Button) findViewById(R.id.button_get_contacts);
        
        launchOauth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	startActivity(new Intent().setClass(v.getContext(), RequestTokenActivity.class));
            	
            }
        });

        clearCredentials.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	clearCredentials();
            	console.setText("Tokens deleted, getContacts call should fail now.");
            }
        });
        
        getContacts.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	getContacts();
            }
        });
   
    }

	private void getContacts() {
		
		
		String jsonOutput = "";
        try {
        	jsonOutput = makeSecuredReq(C.GET_CONTACTS_FROM_GOOGLE_REQUEST,getConsumer(this.prefs));
         	JSONObject jsonResponse = new JSONObject(jsonOutput);
        	JSONObject m = (JSONObject)jsonResponse.get("feed");
        	JSONArray entries =(JSONArray)m.getJSONArray("entry");
        	String contacts = "";
        	for (int i=0 ; i<entries.length() ; i++) {
        		JSONObject entry = entries.getJSONObject(i);
        		//JSONObject title = entry.getJSONObject("title");
        		JSONArray title = entry.getJSONArray("gd$email");
        		//JSONObject addr = title.getJSONObject(0);
        		System.out.println("print format:"+title);
        		if (title.getJSONObject(0).getString("address")!=null && title.getJSONObject(0).getString("address").length()>0) {
        			contacts += title.getJSONObject(0).getString("address").toString() + "\n";
        		}
        		/*if (addr.getString("addr")!=null && addr.getString("addr").length()>0) {
        			contacts += addr.getString("addr") + "\n";
        		}*/
        	}
        	
        	console.setText(contacts);
		} catch (Exception e) {
			Log.e(C.TAG, "Error executing request",e);
			console.setText("Error retrieving contacts : " + jsonOutput);
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (isOAuthSuccessful()) {
    		// OAuth successful, try getting the contacts
    		console.setText("OAuth successful, try getting the contacts");
    	}
    	else {
    		console.setText("OAuth failed, no tokens, Click on the Do OAuth Button.");
    	}
	}
	
    private void clearCredentials() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		final Editor edit = prefs.edit();
		edit.remove(OAuth.OAUTH_TOKEN);
		edit.remove(OAuth.OAUTH_TOKEN_SECRET);
		edit.commit();
	}
    
    private boolean isOAuthSuccessful() {
    	String token = prefs.getString(OAuth.OAUTH_TOKEN, null);
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, null);
		if (token != null && secret != null)
			return true;
		else 
			return false;
    }

	
	private OAuthConsumer getConsumer(SharedPreferences prefs) {
		String token = prefs.getString(OAuth.OAUTH_TOKEN, "");
		String secret = prefs.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		OAuthConsumer consumer = new CommonsHttpOAuthConsumer(C.CONSUMER_KEY, C.CONSUMER_SECRET);
		consumer.setTokenWithSecret(token, secret);
		return consumer;
	}
	
	private String makeSecuredReq(String url,OAuthConsumer consumer) throws Exception {
		DefaultHttpClient httpclient = new DefaultHttpClient();
    	HttpGet request = new HttpGet(url);
    	Log.i(C.TAG,"Requesting URL : " + url);
    	consumer.sign(request);
    	HttpResponse response = httpclient.execute(request);
    	Log.i(C.TAG,"Statusline : " + response.getStatusLine());
    	InputStream data = response.getEntity().getContent();
    	BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(data));
        String responeLine;
        StringBuilder responseBuilder = new StringBuilder();
        while ((responeLine = bufferedReader.readLine()) != null) {
        	responseBuilder.append(responeLine);
        }
        Log.i(C.TAG,"Response : " + responseBuilder.toString());
        return responseBuilder.toString();
	}	
}