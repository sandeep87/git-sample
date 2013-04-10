package com.paradigmcreatives.snakeandladder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SecondActivity extends Activity {
	private Button newgame_btn;
	private Button continue_btn;
	private Button help_btn;
	private Button exit_btn;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second);
		
		newgame_btn = (Button) findViewById(R.id.newgame_id);
		newgame_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SecondActivity.this,ForthActivity.class);
				startActivity(intent);
			}
		});
		continue_btn = (Button) findViewById(R.id.continue_id);
		continue_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SecondActivity.this,ForthActivity.class);
				startActivity(intent);
				
			}
		});
		help_btn = (Button) findViewById(R.id.help_id);
		help_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SecondActivity.this, HelpActivity.class);
				startActivity(intent);
			}
		});
		exit_btn = (Button) findViewById(R.id.exit_id);
		exit_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
	}

}
