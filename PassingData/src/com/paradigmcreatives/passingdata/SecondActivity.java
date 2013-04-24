package com.paradigmcreatives.passingdata;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SecondActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second);

		Toast.makeText(this, getIntent().getStringExtra("str1"),
				Toast.LENGTH_SHORT).show();
		Toast.makeText(this,
				Integer.toString(getIntent().getIntExtra("age1", 0)),
				Toast.LENGTH_SHORT).show();

		Bundle bundle = getIntent().getExtras();
		Toast.makeText(this, bundle.getString("str2"), Toast.LENGTH_SHORT)
				.show();
		Toast.makeText(this, Integer.toString(bundle.getInt("age2")),
				Toast.LENGTH_SHORT).show();

		Button second_btn = (Button) findViewById(R.id.btn_two);
		second_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("age3", 45);
				intent.setData(Uri
						.parse("Something passed back to the mainactivity"));
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

}
