package com.sairam.contentprovidersample;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ContentPro extends Activity {

	private Button view_btn;
	private Button create_btn;
	private Button delete_btn;
	private Button update_btn;
	private Uri updateUri = People.CONTENT_URI;
	private Uri insertUri = People.CONTENT_URI;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content_pro);

		view_btn = (Button) findViewById(R.id.ViewButton);
		view_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				displayContacts();
				Toast.makeText(ContentPro.this,
						"Completed Displaying contactList", Toast.LENGTH_LONG)
						.show();
			}
		});

		create_btn = (Button) findViewById(R.id.CreateButton);
		create_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				createContact(null, null);
				Toast.makeText(ContentPro.this,
						"The new contact created successfully",
						Toast.LENGTH_LONG).show();

			}
		});

		delete_btn = (Button) findViewById(R.id.DeleteButton);
		delete_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				deleteContact();
			}
		});

		update_btn = (Button) findViewById(R.id.UpdateButton);
		update_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				updateContact(null);
			}
		});
	}

	public void updateContact(String phone) {
		if (updateUri == null) {
			Toast.makeText(
					ContentPro.this,
					"There is nothing to update, Please create a contact and then click update",
					Toast.LENGTH_LONG).show();
		} else {
			ContentValues newPhone = new ContentValues();
			newPhone.put(People.Phones.TYPE, People.TYPE_MOBILE);
			newPhone.put(People.NUMBER, phone);
			getContentResolver().update(updateUri, newPhone, null, null);
			Toast.makeText(ContentPro.this, "The contact updated successfully",
					Toast.LENGTH_LONG).show();
			Log.i(getClass().getSimpleName(), "Updated the phone number");
		}

	}

	public void deleteContact() {
		if (updateUri == null) {
			Toast.makeText(
					ContentPro.this,
					"Please create a contact by clicking create button, then I can delete the same",
					Toast.LENGTH_LONG).show();

		} else {
			getContentResolver().delete(insertUri, null, null);
			Toast.makeText(ContentPro.this,
					"Deleted contact at: " + insertUri.toString(),
					Toast.LENGTH_SHORT).show();
			updateUri = null;
			insertUri = null;
			Toast.makeText(ContentPro.this, "The contact deleted successfully",
					Toast.LENGTH_LONG).show();

			Log.i(getClass().getSimpleName(),
					"Deleted the contact inserted by this program");

		}

	}

	public void createContact(String name, String phone) {
		ContentValues contact = new ContentValues();
		contact.put(People.NAME, name);
		insertUri = getContentResolver().insert(People.CONTENT_URI, contact);
		Log.d(getClass().getSimpleName(), insertUri.toString());
		Uri phoneUri = Uri.withAppendedPath(insertUri,
				People.Phones.CONTENT_DIRECTORY);
		contact.clear();
		contact.put(People.Phones.TYPE, People.TYPE_MOBILE);
		contact.put(People.NUMBER, phone);
		updateUri = getContentResolver().insert(phoneUri, contact);
		Log.d(getClass().getSimpleName(), updateUri.toString());

	}

	public void displayContacts() {
		String[] columns = new String[] { People.NAME, People.NUMBER };
		Uri mContacts = People.CONTENT_URI;
		Cursor mCursor = managedQuery(mContacts, columns, null, null, null);

		if (mCursor.moveToFirst()) {
			String name = null;
			String phoneno;
			do {
				name = mCursor.getString(mCursor.getColumnIndex(People.NAME));
				phoneno = mCursor.getString(mCursor
						.getColumnIndex(People.NUMBER));
				try {
					System.out.println("Phone number:" + Long.valueOf(phoneno));
				} catch (NumberFormatException nef) {
					System.out.println(nef);
				}
				System.out.println("Name:" + name);
				Toast.makeText(ContentPro.this, name + "---->" + phoneno,
						Toast.LENGTH_LONG).show();
			} while (mCursor.moveToNext());
		}
	}

}
