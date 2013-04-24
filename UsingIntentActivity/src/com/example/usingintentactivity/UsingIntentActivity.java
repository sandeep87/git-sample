package com.example.usingintentactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class UsingIntentActivity extends Activity {

	int request_code = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_using_intent);
	}

	public void onClick(View v) {
		startActivityForResult(new Intent(UsingIntentActivity.this,
				SecondActivity.class), request_code);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == request_code) {
			if (resultCode == RESULT_OK) {
				Toast.makeText(UsingIntentActivity.this,
						data.getData().toString(), Toast.LENGTH_LONG).show();
			}
		}
	}

}
