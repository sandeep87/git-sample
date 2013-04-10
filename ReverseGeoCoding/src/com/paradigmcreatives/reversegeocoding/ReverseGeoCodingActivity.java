package com.paradigmcreatives.reversegeocoding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ReverseGeoCodingActivity extends Activity {
	/** Called when the activity is first created. */
	private Button mButton;
	private EditText mEditOne;
	private EditText mEditTwo;
	private TextView mTextView;
	private String latitude, longitude;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mEditOne = (EditText) findViewById(R.id.edittext_one);
		mEditTwo = (EditText) findViewById(R.id.edittext_two);
		mTextView = (TextView) findViewById(R.id.textview);
		
		mButton = (Button) findViewById(R.id.mapit_btn);
		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				latitude = mEditOne.getText().toString();
				longitude = mEditTwo.getText().toString();

				List<Address> address = null;
				Geocoder mGeocoder = new Geocoder(getApplicationContext(),
						Locale.getDefault());
				try {
					address = mGeocoder.getFromLocation(
							Double.valueOf(latitude),
							Double.valueOf(longitude), 5);
				} catch (NumberFormatException e) {

					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				StringBuilder builder = new StringBuilder();
				if (address.size() > 0) {
					Address addressone = address.get(0);
					for (int i = 0; i < addressone.getMaxAddressLineIndex(); i++) {
						builder.append(addressone.getCountryName())
								.append("\n")
								.append(addressone.getAddressLine(i));
						mTextView.setText(builder.toString());
					}
				} else {
					mTextView.setText("No data found");
				}

			}
		});
	}

	
}