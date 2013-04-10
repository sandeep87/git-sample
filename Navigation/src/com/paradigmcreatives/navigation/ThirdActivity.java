package com.paradigmcreatives.navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ThirdActivity extends Activity {

	private Button button;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);        
        setContentView(R.layout.thirdactivity);
        button = (Button) findViewById(R.id.Button3);
        button.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v){
        		Intent intent = new Intent(ThirdActivity.this,FirstActivity.class);
        		startActivity(intent);
        	}
        });
	}
}
