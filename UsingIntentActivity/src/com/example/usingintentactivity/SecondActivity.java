package com.example.usingintentactivity;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SecondActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second_activity);
	}
	public void onClick(View v) {
		Intent data = new Intent();
		EditText mEditText = (EditText)findViewById(R.id.txt_usrname);
		data.setData(Uri.parse(txt_usrname.getText().toString()));
		setResult(RESULT_OK, data);
		finish();
	}

}
