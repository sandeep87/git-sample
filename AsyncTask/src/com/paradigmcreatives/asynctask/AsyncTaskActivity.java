package com.paradigmcreatives.asynctask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class AsyncTaskActivity extends Activity {
	/** Called when the activity is first created. */
	private TextView mTextView;
	private Button mButton;
	private ProgressDialog mProgressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mTextView = (TextView) findViewById(R.id.textview);
		mButton = (Button) findViewById(R.id.readWebpage);
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage("Loading...");
		mProgressDialog.setTitle("In progress");
		mButton.setVisibility(1);
		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mButton.setVisibility(View.GONE);

				readWebpage();

			}
		});
	}

	private class DownloadWebpage extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			mProgressDialog.show();
		};

		protected String doInBackground(String... urls) {
			String response = "";
			for (String url : urls) {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);
				try {
					HttpResponse execute = client.execute(httpGet);
					InputStream content = execute.getEntity().getContent();
					BufferedReader br = new BufferedReader(
							new InputStreamReader(content));
					String s = "";
					while ((s = br.readLine()) != null) {
						response += s;

					}

				} catch (ClientProtocolException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				}

			}
			return response;

		}

		@Override
		protected void onPostExecute(String result) {

			mProgressDialog.dismiss();
			mTextView.setText(result);
			mTextView.setTextColor(Color.parseColor("#FFFF00"));
		}

	}

	public void readWebpage() {
		DownloadWebpage task = new DownloadWebpage();
		task.execute(new String[] { "http://developer.android.com" });

	}
}