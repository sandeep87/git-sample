package com.paradigmcreatives.facebookloginout;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class FaceBookLoginOutActivity extends Activity {

	/** Called when the activity is first created. */
	static Facebook mFacebook = new Facebook("250823521707781");
	private Button login;
	private Button logout;
	private Button post_on_wall;

	private final static String[] FACEBOOK_PERMISSION = { "publish_stream",
			"read_stream" };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		login = (Button) findViewById(R.id.login);
		logout = (Button) findViewById(R.id.logout);
		post_on_wall = (Button) findViewById(R.id.post_on_wall);
		logout.setVisibility(View.GONE);
		post_on_wall.setVisibility(View.GONE);
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mFacebook.authorize(FaceBookLoginOutActivity.this,
						FACEBOOK_PERMISSION, new DialogListener() {

							@Override
							public void onComplete(Bundle values) {
								Toast.makeText(
										getApplicationContext(),
										"You are Logged in."
												+ values.getString("access_token"),
										Toast.LENGTH_LONG).show();
								login.setVisibility(View.GONE);
								logout.setVisibility(View.VISIBLE);
								post_on_wall.setVisibility(View.VISIBLE);
							}

							@Override
							public void onFacebookError(FacebookError e) {
							}

							@Override
							public void onError(DialogError e) {
							}

							@Override
							public void onCancel() {
							}
						});

			}
		});

		logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AsyncFacebookRunner(FaceBookLoginOutActivity.mFacebook)
						.logout(getApplicationContext(), new RequestListener() {

							@Override
							public void onMalformedURLException(
									MalformedURLException e, Object state) {

							}

							@Override
							public void onIOException(IOException e,
									Object state) {

							}

							@Override
							public void onFileNotFoundException(
									FileNotFoundException e, Object state) {

							}

							@Override
							public void onFacebookError(FacebookError e,
									Object state) {

							}

							@Override
							public void onComplete(String response, Object state) {
								Looper.prepare();
								Toast.makeText(FaceBookLoginOutActivity.this,
										"You are loggedout successfully",
										Toast.LENGTH_LONG).show();

								finish();
								System.out.println("your response:" + response);
								Looper.loop();

							}
						});
			}
		});

		post_on_wall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				postOnWall("http://www.paradigmcreatives.com/");
				Toast.makeText(FaceBookLoginOutActivity.this,
						"The message has successfully posted on your wall",
						Toast.LENGTH_LONG).show();
			}
		});

	}

	private void postOnWall(String msg) {
		if (mFacebook.isSessionValid()) {
			Bundle parameters = new Bundle();
			parameters.putString("link", msg);
			try {
				String response = mFacebook.request("me/feed", parameters,
						"POST");
				System.out.println("Response:" + response);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}