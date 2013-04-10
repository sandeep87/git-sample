package com.example.internalstore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Numbers extends Activity implements OnClickListener {
	
	private EditText number;
	private Button sendinfo_btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.numbers);
		number = (EditText) findViewById(R.id.edittext1);
		sendinfo_btn = (Button) findViewById(R.id.button1);
		sendinfo_btn.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		String string = number.getText().toString();
		Intent intent = getIntent();
		String msg = intent.getStringExtra("numbers");
		
		if (msg.contentEquals("width")) {
			intent.putExtra("Width Info", string);
			setResult(RESULT_OK, intent);
			finish();
		}
		
		if (msg.contentEquals("Height")) {
			intent.putExtra("Height Info", string);
			setResult(RESULT_OK, intent);
			finish();
		}
	}

}
