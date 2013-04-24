package com.paradigmcreatives.passingdata;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class PassingData extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_passing_data);
		Button main_btn = (Button) findViewById(R.id.btn_one);
		main_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(PassingData.this,
						SecondActivity.class);
				mIntent.putExtra("str1", "This is a string");
				mIntent.putExtra("age1", 25);

				Bundle extras = new Bundle();
				extras.putString("str2", "This is another string");
				extras.putInt("age2", 35);

				mIntent.putExtras(extras);
				startActivityForResult(mIntent, 1);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
				Toast.makeText(this,
						Integer.toString(data.getIntExtra("age3", 0)),
						Toast.LENGTH_SHORT).show();
				Toast.makeText(this, data.getData().toString(),
						Toast.LENGTH_SHORT).show();

			}
		}
	}

}
