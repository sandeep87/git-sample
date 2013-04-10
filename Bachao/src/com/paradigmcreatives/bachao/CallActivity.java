package com.paradigmcreatives.bachao;

import com.paradigmcreatives.bachao.util.Constants;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CallActivity extends Activity {

	private CheckBox callCheck;

	private EditText callField;

	private Button callSave;

	private String phoneNumber;

	private SharedPreferences preferences;

	private SharedPreferences.Editor edit;

	private String preferenceNumber;

	private boolean preferenceCheck;

	private ImageView btnClear;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_call);

		callCheck     = (CheckBox) findViewById(R.id.check_call);
		callField     = (EditText) findViewById(R.id.id_phone_number_field);
		callSave      = (Button) findViewById(R.id.id_save);
		btnClear      = (ImageView) findViewById(R.id.button_close);

		btnClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick (View v) {
				callField.setText("");

			}
		});

		// clear button visibility code
		callField.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged (CharSequence s, int start, int before, int count) {
				if (s.length() == 0) {
					btnClear.setVisibility(View.INVISIBLE);
				}
				else {
					btnClear.setVisibility(View.VISIBLE);

				}

			}

			@Override
			public void beforeTextChanged (CharSequence s, int start, int count, int after) {
				if (s.length() == 0) {
					btnClear.setVisibility(View.INVISIBLE);
				}
				else {
					btnClear.setVisibility(View.VISIBLE);

				}

			}

			@Override
			public void afterTextChanged (Editable s) {
				if (s.length() == 0) {
					btnClear.setVisibility(View.INVISIBLE);
				}
				else {
					btnClear.setVisibility(View.VISIBLE);

				}

			}
		});

		//
		preferences = getApplicationContext().getSharedPreferences(Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
		edit = preferences.edit();
		preferenceNumber = preferences.getString("key_number", null);
		preferenceCheck = preferences.getBoolean("CALL_CHECK", false);

		if (preferenceCheck == true) {
			callCheck.setChecked(true);
		}

		if (preferenceNumber != null) {
			callField.setText(preferenceNumber);
		}

		// set cursor position to end of the field
		int position = callField.getText().length();
		Editable etext = callField.getText();
		Selection.setSelection(etext, position);

		callCheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick (View v) {
				if (callCheck.isChecked()) {
					callCheck.setButtonDrawable(R.drawable.check);
				}
				else {
					callCheck.setButtonDrawable(R.drawable.add);
				}
			}
		});

		// save listener
		callSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick (View v) {
				phoneNumber = callField.getText().toString().trim();

				if (callCheck.isChecked()) {
					edit.putBoolean("CALL_CHECK", true);
				}
				else {
					edit.putBoolean("CALL_CHECK", false);
				}
				edit.putString("key_number", phoneNumber);

				edit.commit();
				if (callCheck.isChecked() && phoneNumber.length() == 0) {
					Toast.makeText(CallActivity.this, getResources().getString(R.string.call_configure_number), Toast.LENGTH_LONG).show();
				}
				else {
					finish();
				}

			}
		});

	}

	@Override
	public void onBackPressed () {

		if (callCheck.isChecked()) {
			edit.putBoolean("CALL_CHECK", true);
		}
		else {
			edit.putBoolean("CALL_CHECK", false);
		}
		edit.commit();

		phoneNumber = callField.getText().toString().trim();
		String prefNumber = preferences.getString("key_number", null);
		boolean value = preferences.getBoolean("CALL_CHECK", false);
		if (value && phoneNumber.length() == 0) {
			Toast.makeText(CallActivity.this, getResources().getString(R.string.call_input_message), Toast.LENGTH_LONG).show();

		}
		else {
			if (phoneNumber.length() > 0 && !phoneNumber.equalsIgnoreCase(prefNumber)) {
				AlertDialog.Builder build = new AlertDialog.Builder(CallActivity.this);
				build.setTitle(getResources().getString(R.string.abandom_text));
				build.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

					@Override
					public void onClick (DialogInterface dialog, int which) {
						phoneNumber = callField.getText().toString().trim();
						String prefNumber = preferences.getString("key_number", null);
						if (prefNumber == null && phoneNumber.length() != 0) {
							callField.setText(null);
						}
						else if (prefNumber != null && !phoneNumber.equalsIgnoreCase(prefNumber)) {
							callField.setText(prefNumber);
						}
						CallActivity.this.finish();
					}
				});

				build.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

					@Override
					public void onClick (DialogInterface dialog, int which) {
						dialog.dismiss();
						Toast.makeText(CallActivity.this, getResources().getString(R.string.call_error_tap), Toast.LENGTH_SHORT).show();
					}
				});

				AlertDialog alert = build.create();
				alert.show();

			}
			else {
				super.onBackPressed();
			}
		}

	}
}
