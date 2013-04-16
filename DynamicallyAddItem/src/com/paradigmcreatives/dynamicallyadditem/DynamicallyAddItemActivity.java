package com.paradigmcreatives.dynamicallyadditem;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class DynamicallyAddItemActivity extends Activity {
   
	private static final OnClickListener Listener = null;
	/** Called when the activity is first created. */
	ArrayList<String> list = new ArrayList<String>();
	ArrayAdapter<String> adapter;
	private Button mButton;
	private EditText mEditText;
	CustomAdapterDemo mAdapterDemo;
	ListView mListView;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic_data);
        mEditText = (EditText)findViewById(R.id.edittext);
        mButton = (Button)findViewById(R.id.addItem_btn);
        mListView = (ListView)findViewById(R.id.list);
        mAdapterDemo = new CustomAdapterDemo(DynamicallyAddItemActivity.this, list);
        mListView.setAdapter(mAdapterDemo);
        mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				list.add(mEditText.getText().toString());
				mEditText.setText("");				
				 mAdapterDemo = new CustomAdapterDemo(DynamicallyAddItemActivity.this, list);
			       mListView.setAdapter(mAdapterDemo);
			       
				
			}
		});
       
        
    }
	
	
}