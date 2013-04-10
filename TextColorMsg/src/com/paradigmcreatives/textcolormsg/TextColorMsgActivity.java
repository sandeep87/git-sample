package com.paradigmcreatives.textcolormsg;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;

public class TextColorMsgActivity extends Activity {
    /** Called when the activity is first created. */
	private TextView textView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        textView = (TextView)findViewById(R.id.text_view);
        textView.setText(getResources().getString(R.string.hello));
        textView.setTextColor(getResources().getColor(R.color.yellow));
        textView.setBackgroundDrawable(Resources.getSystem().getDrawable(android.R.drawable.alert_light_frame));
        
        
    }
}