package com.example.internalstore;

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

public class Settings extends Activity implements OnClickListener {
	
	private CheckBox checkBox;
	private Button button;
	private EditText editText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		
		checkBox = (CheckBox) findViewById(R.id.checkbox);
		editText = (EditText) findViewById(R.id.edittext1);
		button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(this);
		loadPrefs();
	}
	
	private void loadPrefs() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		boolean cbValue = sp.getBoolean("CHECKBOX", false);
		String name = sp.getString("NAME", "YourName");
		if(cbValue){
			checkBox.setChecked(true);
		}else{
			checkBox.setChecked(false);
		}
		editText.setText(name);
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

	@Override
	public void onClick(View v) {
		savePrefs("CHECKBOX", checkBox.isChecked());
		if (checkBox.isChecked())
			savePrefs("NAME", editText.getText().toString());
		
		finish();
	}

}
