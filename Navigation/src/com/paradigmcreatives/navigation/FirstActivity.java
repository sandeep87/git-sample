package com.paradigmcreatives.navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FirstActivity extends Activity {
    /** Called when the activity is first created. */
	private Button button;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.firstactivity);
        
        button = (Button) findViewById(R.id.Button1);
        button.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v){
        		Intent intent = new Intent(getApplicationContext(),SecondActivity.class);
        		startActivity(intent);
        	}
        });
    }
}