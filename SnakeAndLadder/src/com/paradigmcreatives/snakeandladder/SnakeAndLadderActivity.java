package com.paradigmcreatives.snakeandladder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SnakeAndLadderActivity extends Activity {
    /** Called when the activity is first created. */
	private Button go_btn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fisrt);
System.out.println("the game");
        
        go_btn = (Button) findViewById(R.id.go);
        go_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SnakeAndLadderActivity.this,ThirdActivity.class);
				startActivity(intent);
				finish();
				
			}
		});
    }
}
