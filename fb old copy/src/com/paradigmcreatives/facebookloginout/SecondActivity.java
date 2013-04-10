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
import com.facebook.android.FacebookError;

public class SecondActivity extends Activity {
	private Button logout_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second);
		logout_btn = (Button) findViewById(R.id.logout);

		logout_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {

				new AsyncFacebookRunner(FaceBookLoginOutActivity.mFacebook)
						.logout(getApplicationContext(), new RequestListener() {

							@Override
							public void onMalformedURLException(
									MalformedURLException e, Object state) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onIOException(IOException e,
									Object state) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onFileNotFoundException(
									FileNotFoundException e, Object state) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onFacebookError(FacebookError e,
									Object state) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onComplete(String response, Object state) {
								Looper.prepare();
								Toast.makeText(SecondActivity.this,
										"You are loggedout successfully",
										Toast.LENGTH_LONG).show();

								finish();
								System.out.println("your response:" + response);
								Looper.loop();
							}
						});

			}
		});
	}

}
