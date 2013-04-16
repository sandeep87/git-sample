package com.paradigmcreatives.sqliteproduct;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	private Button btn_add, btn_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		btn_add = (Button) findViewById(R.id.add_btn);
		btn_view = (Button) findViewById(R.id.view_btn);
		btn_add.setOnClickListener(this);
		btn_view.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.add_btn:
			Intent addIntent = new Intent(MainActivity.this, AddRecord.class);
			break;

		case R.id.view_btn:
			Intent viewIntent = new Intent(MainActivity.this, ViewRecord.class);
			break;

		default:
			break;
		}

	}

}
