package com.paradigmcreatives.snakeandladder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ThirdActivity extends Activity {
	private EditText mEditText_name;
	private EditText mEditText_age;
	private Button submit_btn;
	/*private static final String NAME = "name";
	SharedPreferences settings;*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.third);
		
		mEditText_name = (EditText) findViewById(R.id.edittext1);
		mEditText_age = (EditText) findViewById(R.id.edittext2);
		/*String s = mEditText_name.getText().toString();
		settings = getSharedPreferences(NAME, MODE_PRIVATE);
		SharedPreferences.Editor username	=settings.edit();
		username.putString("username", s);
		username.commit();*/
		submit_btn = (Button) findViewById(R.id.submit);
		submit_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ThirdActivity.this, SecondActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		
	}

}
