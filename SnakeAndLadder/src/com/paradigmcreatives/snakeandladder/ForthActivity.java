package com.paradigmcreatives.snakeandladder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ForthActivity extends ThirdActivity {
	private Button throw_btn;
	private Button exit_btn;
	//private static final String NAME = "name";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forth);
		 throw_btn = (Button) findViewById(R.id.throw_dice);
		 throw_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
			}
		});
		 
		exit_btn = (Button) findViewById(R.id.exit_bt);
		/*new ThirdActivity();
		SharedPreferences settings;
		
		settings = getPreferences(MODE_PRIVATE);
	String s =settings.getString("username", "defValue");*/
	//exit_btn.setText(s);
		exit_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	}

}
