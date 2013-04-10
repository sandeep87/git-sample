package com.example.internalstore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class InternalStore extends Activity implements OnClickListener {
	private Button width_btn, height_btn, calc_btn;
	private TextView area;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_internal_store);
		
		width_btn = (Button) findViewById(R.id.button1);
		height_btn = (Button) findViewById(R.id.button2);
		calc_btn = (Button) findViewById(R.id.button3);
		area = (TextView) findViewById(R.id.textview1);
		
		width_btn.setOnClickListener(this);
		height_btn.setOnClickListener(this);
		calc_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		Intent intent = new Intent(InternalStore.this, Numbers.class);
		
		switch(v.getId()) {
		case R.id.button1:
			intent.putExtra("numbers", "width");
			startActivityForResult(intent, 1);
			break;
			
		case R.id.button2:
			intent.putExtra("numbers", "height");
			startActivityForResult(intent, 1);
			break;
			
		case R.id.button3:
			int a = Integer.valueOf(width_btn.getText().toString());
			int b = Integer.valueOf(height_btn.getText().toString());
			area.setText(a*b+"sq ft");
			break;
			
			default:
				break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (data.getExtras().containsKey("Width Info")) {
			width_btn.setText(data.getStringExtra("Width Info"));
		}
		
		if (data.getExtras().containsKey("Height Info")) {
			height_btn.setText(data.getStringExtra("Height Info"));
		}
	}

}
