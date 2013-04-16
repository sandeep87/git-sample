package com.example.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class MainActivity extends Activity {

	private CheckBox mCheckBox;
	private EditText mEditText;
	private Button mButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mCheckBox = (CheckBox) findViewById(R.id.checkbox1);
		mEditText = (EditText) findViewById(R.id.edittext1);
		mButton = (Button) findViewById(R.id.button1);
		mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				savePrefs("CHECKBOX", mCheckBox.isChecked());
				if (mCheckBox.isChecked()) 
					savePrefs("NAME", mEditText.getText().toString());
				finish();
				
			}
		});
		loadPrefs();
	}

	private void loadPrefs() {
		
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		boolean cbValue = sp.getBoolean("CHECKBOX", false);
		String name = sp.getString("NAME", "Your name");
		
		if (cbValue) {
			mCheckBox.setChecked(true);
		}
		else {
			mCheckBox.setChecked(false);
		}
		mEditText.setText(name);
		
	}
	
	private void savePrefs(String key, boolean value) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		Editor edit = sp.edit();
		edit.putBoolean(key, value);
		edit.commit();
		
	}
	
	private void savePrefs(String key, String value) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		Editor edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}
	
	

}
