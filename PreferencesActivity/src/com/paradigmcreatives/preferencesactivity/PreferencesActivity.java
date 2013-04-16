package com.paradigmcreatives.preferencesactivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

public class PreferencesActivity extends Activity {

	private EditText name;
	private EditText surname;
	private EditText age;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferences);
		init();
	}

	private void init() {
		name = (EditText) findViewById(R.id.edittextname);
		surname = (EditText) findViewById(R.id.edittextsurname);
		age = (EditText) findViewById(R.id.edittextage);
		age.setInputType(InputType.TYPE_CLASS_NUMBER);
	}

	public void save(View view) {

		String nameTxt = name.getText().toString();
		String surnameTxt = surname.getText().toString();
		String ageTxt = age.getText().toString();

		if (nameTxt != null) {
			PreferenceConnector.writeString(this, PreferenceConnector.NAME,
					nameTxt);
		}

		if (surnameTxt != null) {
			PreferenceConnector.writeString(this, PreferenceConnector.SURNAME,
					surnameTxt);
		}

		if (ageTxt != null) {
			PreferenceConnector.writeInteger(this, PreferenceConnector.AGE,
					Integer.parseInt(ageTxt));
		}
	}

	public void reset(View view) {
		PreferenceConnector.getEditor(this).remove(PreferenceConnector.NAME)
				.commit();
		PreferenceConnector.getEditor(this).remove(PreferenceConnector.SURNAME)
				.commit();
		PreferenceConnector.getEditor(this).remove(PreferenceConnector.AGE)
				.commit();
		readPerson();
	}

	private void readPerson() {
		name.setText(PreferenceConnector.readString(this,
				PreferenceConnector.NAME, null));
		surname.setText(PreferenceConnector.readString(this,
				PreferenceConnector.SURNAME, null));
		String agePref = ""
				+ PreferenceConnector.readInteger(this,
						PreferenceConnector.AGE, 0);
		age.setText((agePref.equals("0")) ? null : agePref);
	}

}
