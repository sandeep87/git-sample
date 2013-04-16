package com.paradigmcreatives.textcolor;

import android.app.Activity;
import android.os.Bundle;

public class TextColorActivity extends Activity {
    /** Called when the activity is first created. */
	private String mess;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        String mess = getResources().getString(R.string.mess_1);
    }
}