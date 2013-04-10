package com.paradigmcreatives.facebookloginout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class FaceBookLoginOutActivity extends Activity {
	
	/** Called when the activity is first created. */
	static Facebook mFacebook = new Facebook("250823521707781");
	private Button mButton;
	private TextView mTextView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		
		

		
		mButton = (Button) findViewById(R.id.login);
		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mFacebook.authorize(FaceBookLoginOutActivity.this,
						new DialogListener() {

							@Override
							public void onComplete(Bundle values) {
								Toast.makeText(getApplicationContext(),
										"You are Logged in.", Toast.LENGTH_LONG)
										.show();
								/*try {
									mTextView = (TextView) findViewById(R.id.text_view);
									mTextView.setText("Hi this is my first app");
								    String message = String.valueOf(mTextView.getText());
								    Bundle parameters = new Bundle();
								    parameters.putString("message", message);
								    mFacebook.request("me/feed", parameters, "POST");
								} catch (Exception e) {
								}*/
								/*try{
							        Bundle parameters = new Bundle();
							        parameters.putString("message", "Text is lame. Listen up:");
							        parameters.putString("name", "Name");
							        parameters.putString("link", "http://www.google.com");
							        parameters.putString("caption", "Caption");
							        parameters.putString("description", "Description");

							        String  response = mFacebook.request("me/feed",parameters,"POST");
							        Log.v("response", response);
							    }
							    catch(Exception e){}*/
								
								Intent intent = new Intent(
										FaceBookLoginOutActivity.this,
										SecondActivity.class);
								startActivity(intent);

							}

							private void postOnWall(String msg) {
								Log.d("Tests", "Testing graph API wall post");
							    try {
							        String response = mFacebook.request("me");
							        Bundle parameters = new Bundle();
							        parameters.putString("message", msg);
							        parameters.putString("description", "test test test");
							        response = mFacebook.request("me/feed", parameters,
							                "POST");
							        Log.d("Tests", "got response: " + response);
							        if (response == null || response.equals("") ||
							                response.equals("false")) {
							           Log.v("Error", "Blank response");
							        }
							    } 
							    catch(Exception e) {
							        e.printStackTrace();
							    }
								
							}

							@Override
							public void onFacebookError(FacebookError e) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onError(DialogError e) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onCancel() {

							}

						});

			}
		});
		/*public postOnWall(String msg) {
		    Log.d("Tests", "Testing graph API wall post");
		    try {
		        String response = mFacebook.request("me");
		        Bundle parameters = new Bundle();
		        parameters.putString("message", msg);
		        parameters.putString("description", "test test test");
		        response = mFacebook.request("me/feed", parameters,
		                "POST");
		        Log.d("Tests", "got response: " + response);
		        if (response == null || response.equals("") ||
		                response.equals("false")) {
		           Log.v("Error", "Blank response");
		        }
		    } 
		    catch(Exception e) {
		        e.printStackTrace();
		    }
		}*/
	}
}