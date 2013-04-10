package com.paradigmcreatives.bachao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.paradigmcreatives.bachao.adapters.ContactsAdapter;
import com.paradigmcreatives.bachao.logging.Logger;
import com.paradigmcreatives.bachao.util.Constants;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.Editable;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class SMSSettingsActivity extends Activity {

	private ListView mContactsList;

	private Button mSaveButton;

	private CheckBox mCheckBox;

	private ArrayList <String> mNumbers;

	private ArrayList <String> mNames;

	private ContactsAdapter mAdapter;

	private SharedPreferences mPreferences;

	private SharedPreferences.Editor edit;

	private EditText messageBox;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms);

		String[] num = new String[5];
		String[] nam = new String[5];

		mPreferences = getApplicationContext().getSharedPreferences(Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
		edit = mPreferences.edit();

		if (mPreferences.getAll() != null) {
			for (int i = 0; i < 5; i++) {
				num[i] = mPreferences.getString("NUMBER_" + i, null);
				nam[i] = mPreferences.getString("NAME_" + i, null);
			}
		}
		else {
			for (int i = 0; i < 5; i++) {
				num[i] = null;
				nam[i] = null;
			}
		}

		mNumbers = new ArrayList <String>(Arrays.asList(num));
		mNames = new ArrayList <String>(Arrays.asList(nam));

		mCheckBox = (CheckBox) findViewById(R.id.check);
		mSaveButton = (Button) findViewById(R.id.save);
		mContactsList = (ListView) findViewById(R.id.contacts_list);
		messageBox = (EditText) findViewById(R.id.edit_message);

		if (mPreferences.getAll() != null && mPreferences.getBoolean("SMS_CHECK", false)) {
			mCheckBox.setChecked(true);
		}
		String msg = mPreferences.getString("SMS_MESSAGE", null);
		if (msg != null) {
			messageBox.setText(msg);
		}
		else {
			messageBox.setText("");
		}

		// set cursor position to end of the field
		int position = messageBox.getText().length();
		Editable etext = messageBox.getText();
		Selection.setSelection(etext, position);

		mAdapter = new ContactsAdapter(SMSSettingsActivity.this, mNumbers, mNames);
		mContactsList.setAdapter(mAdapter);

		mCheckBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick (View v) {
				if (mCheckBox.isChecked()) {
					mCheckBox.setButtonDrawable(R.drawable.check);
				}
				else {
					mCheckBox.setButtonDrawable(R.drawable.add);
				}
			}
		});

		// save button to save the selected contacts in mPreferences
		mSaveButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick (View v) {
				SharedPreferences.Editor edit = mPreferences.edit();
				String msg = messageBox.getText().toString().trim();
				edit.putString("SMS_MESSAGE", msg);
				for (int i = 0; i < 5; i++) {
					edit.putString("NUMBER_" + i, mNumbers.get(i));
					edit.putString("NAME_" + i, mNames.get(i));
				}
				if (mCheckBox.isChecked()) {
					edit.putBoolean("SMS_CHECK", true);
					edit.commit();
					checkBox();
				}
				else {
					edit.putBoolean("SMS_CHECK", false);
					edit.commit();
					finish();
				}
			}
		});

	}

	// method to check whether there is at least one contact in contact list
	public void checkBox () {
		ArrayList <String> check = new ArrayList <String>();
		for (int i = 0; i < 5; i++) {
			if (mNumbers.get(i) != null) {
				check.add(mNumbers.get(i));
			}
		}
		if (check.size() == 0) {
			if (mCheckBox.isChecked()) {
				Toast.makeText(SMSSettingsActivity.this, getResources().getString(R.string.contact_selection), Toast.LENGTH_LONG).show();
			}
			else {
				finish();
			}
		}
		else {
			finish();
		}
	}

	@Override
	protected void onActivityResult (int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case 0:
					handleContacts(data, requestCode);
					break;
				case 1:
					handleContacts(data, requestCode);
					break;
				case 2:
					handleContacts(data, requestCode);
					break;
				case 3:
					handleContacts(data, requestCode);
					break;
				case 4:
					handleContacts(data, requestCode);
					break;
			}
		}
		else {
			// gracefully handle failure
			Log.w("ContactsActivity", "Warning activity not okay");
		}
	}

	// method to handle the contact selected from the contacts
	public void handleContacts (Intent data, final int i) {
		Cursor cursor = null;
		String phoneNo = " ";
		String name = " ";
		List <String> allNumbers = new ArrayList <String>();

		int phoneIdx = 0;

		try {
			Uri result = data.getData();

			// get the contact id from the Uri
			String id = result.getLastPathSegment();

			// query for everything phone
			cursor = getContentResolver().query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + "=?", new String[] { id }, null);

			phoneIdx = cursor.getColumnIndex(Phone.DATA);

			// let's just get every phone number
			if (cursor.moveToFirst()) {
				name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				mNames.set(i, name);
				while (cursor.isAfterLast() == false) {
					phoneNo = cursor.getString(phoneIdx);
					allNumbers.add(phoneNo);
					cursor.moveToNext();
				}

			}
			else {
				Toast.makeText(SMSSettingsActivity.this, getResources().getString(R.string.no_contact), Toast.LENGTH_LONG).show();
			}
		}
		catch (Exception e) {
			Logger.logStackTrace(e);
		}
		finally {
			if (cursor != null) {
				cursor.close();
			}

			final String[] items = allNumbers.toArray(new String[allNumbers.size()]);
			AlertDialog.Builder builder = new AlertDialog.Builder(SMSSettingsActivity.this);
			builder.setTitle(getResources().getString(R.string.choose_number));
			builder.setItems(items, new DialogInterface.OnClickListener() {

				public void onClick (DialogInterface dialog, int item) {
					String selectedNumber = items[item].toString();
					addContact(selectedNumber, i);
				}
			});

			AlertDialog alert = builder.create();
			if (allNumbers.size() > 1) {
				alert.show();
			}
			else {
				String selectedNumber = phoneNo.toString();
				addContact(selectedNumber, i);
			}
		}

	}

	// method to check whether the contacts already exists, if not add to the contacts list
	private void addContact (String selectedNumber, int i) {
		ArrayList <String> check = new ArrayList <String>();
		for (int j = 0; j < 5; j++) {
			if (mNumbers.get(j) != null && mNumbers.get(j).equalsIgnoreCase(selectedNumber)) {
				check.add(mNumbers.get(j));
			}
		}

		if (!selectedNumber.equals(" ")) {
			if (check.size() == 1) {
				Toast.makeText(SMSSettingsActivity.this, getResources().getString(R.string.contact_exist), Toast.LENGTH_SHORT).show();
			}
			else {
				mNumbers.set(i, selectedNumber);
				mAdapter.notifyDataSetChanged();
				check.clear();
			}
		}
	}

	@Override
	public void onBackPressed () {
		SharedPreferences.Editor edit = mPreferences.edit();
		if (mCheckBox.isChecked()) {
			edit.putBoolean("SMS_CHECK", true);
		}
		else {
			edit.putBoolean("SMS_CHECK", false);
		}
		edit.commit();
		ArrayList <String> selected = new ArrayList <String>();
		ArrayList <String> preferences = new ArrayList <String>();

		String msg = messageBox.getText().toString().trim();
		String prefMessage = mPreferences.getString("SMS_MESSAGE", null);
		for (int i = 0; i < 5; i++) {
			if (mNumbers.get(i) != null) {
				selected.add(mNumbers.get(i));
			}
			if ((mPreferences.getString("NUMBER_" + i, null)) != null) {
				preferences.add(mPreferences.getString("NUMBER_" + i, null));
			}
		}

		if (mCheckBox.isChecked()) {
			if (selected.size() == 0) {
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.contact_alert), Toast.LENGTH_SHORT).show();
				
			}
			else if (selected.size() != preferences.size() && !msg.equalsIgnoreCase(prefMessage)) {
				showAlertDialog();
			}
			else if (selected.size() != preferences.size() && msg.equalsIgnoreCase(prefMessage)) {
				showAlertDialog();
			}
			else if (selected.size() == preferences.size() && !msg.equalsIgnoreCase(prefMessage)) {
				showAlertDialog();
			}
			else {
				super.onBackPressed();
			}
		}
		else {
			if (preferences.size() == 0 && selected.size() == 0) {
				super.onBackPressed();
			}
			else if (selected.size() != preferences.size() && !msg.equalsIgnoreCase(prefMessage)) {
				showAlertDialog();
			}
			else if (selected.size() != preferences.size() && msg.equalsIgnoreCase(prefMessage)) {
				showAlertDialog();
			}
			else if (selected.size() == preferences.size() && !msg.equalsIgnoreCase(prefMessage)) {
				showAlertDialog();
			}
			else {
				super.onBackPressed();
			}
		}
	}

	private void showAlertDialog () {
		AlertDialog.Builder build = new AlertDialog.Builder(SMSSettingsActivity.this);
		build.setTitle(getResources().getString(R.string.abandom_text));
		build.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {

			@Override
			public void onClick (DialogInterface dialog, int which) {
				ArrayList <String> selected = new ArrayList <String>();
				ArrayList <String> preferences = new ArrayList <String>();

				for (int i = 0; i < 5; i++) {
					if (mNumbers.get(i) != null) {
						selected.add(mNumbers.get(i));
					}
					if ((mPreferences.getString("NUMBER_" + i, null)) != null) {
						preferences.add(mPreferences.getString("NUMBER_" + i, null));
					}
				}

				if (preferences.size() == 0) {
					edit.putBoolean("SMS_CHECK", false);
					edit.commit();
				}

				SMSSettingsActivity.this.finish();
			}
		});

		build.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {

			@Override
			public void onClick (DialogInterface dialog, int which) {
				dialog.dismiss();
				Toast.makeText(SMSSettingsActivity.this, getResources().getString(R.string.call_error_tap), Toast.LENGTH_SHORT).show();
			}
		});

		AlertDialog alert = build.create();
		alert.show();
	}

}
