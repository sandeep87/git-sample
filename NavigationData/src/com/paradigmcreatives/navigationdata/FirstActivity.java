package com.paradigmcreatives.navigationdata;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class FirstActivity extends Activity {
	/** Called when the activity is first created. */
	private EditText editText;
	private Button button;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.firstactivity);
		editText = (EditText) findViewById(R.id.editText);
		button = (Button) findViewById(R.id.button);

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FirstActivity.this,
						SecondActivity.class);
				intent.putExtra("key", editText.getText().toString());
				startActivity(intent);

			}
		});
	}
}