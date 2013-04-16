package com.paradigmcreatives.snakeandladder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ThirdActivity extends Activity {
	private EditText mEditText_name;
	private EditText mEditText_age;
	private Button submit_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.third);

		mEditText_name = (EditText) findViewById(R.id.edittext1);
		mEditText_age = (EditText) findViewById(R.id.edittext2);
		submit_btn = (Button) findViewById(R.id.submit);
		submit_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//String s = mEditText_name.getText().toString().trim();
				Intent intent = new Intent(ThirdActivity.this,
						ForthActivity.class);
				//intent.putExtra("key", s);
				startActivity(intent);
				
			}
		});

	}

}
