package com.paradigmcreatives.navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SecondActivity extends Activity {

	private Button button;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);        
        setContentView(R.layout.secondactivity);
        Button button = (Button) findViewById(R.id.Button2);
        button.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v){
        		Intent intent = new Intent(SecondActivity.this,ThirdActivity.class);
        		startActivity(intent);
        	}
        });
	}
}
