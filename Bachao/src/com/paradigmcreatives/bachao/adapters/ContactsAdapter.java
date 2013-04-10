package com.paradigmcreatives.bachao.adapters;

import java.util.ArrayList;

import com.paradigmcreatives.bachao.R;
import com.paradigmcreatives.bachao.util.Constants;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ContactsAdapter extends BaseAdapter {

	private Context mContext;

	private ArrayList <String> mNumbers = new ArrayList <String>();

	private ArrayList <String> mNames = new ArrayList <String>();

	public ContactsAdapter (Context context, ArrayList <String> numbers, ArrayList <String> names) {
		mContext = context;
		mNumbers = numbers;
		mNames = names;
	}

	@Override
	public int getCount () {
		return mNumbers.size();
	}

	@Override
	public Object getItem (int position) {
		return mNumbers.get(position);
	}

	@Override
	public long getItemId (int position) {
		return position;
	}

	@Override
	public View getView (final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		ViewHolder holder;
		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			view = inflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();
			holder.name = (TextView) view.findViewById(R.id.contact_name);
			holder.number = (TextView) view.findViewById(R.id.contact_number);
			holder.add = (TextView) view.findViewById(R.id.add);
			view.setTag(holder);
		}
		else {
			holder = (ViewHolder) view.getTag();
		}

		if (mNumbers.get(position) != null && mNames.get(position) != null) {
			holder.number.setText(mNumbers.get(position).toString());
			holder.name.setText(mNames.get(position).toString());
			holder.add.setText(mContext.getResources().getString(R.string.edit));
		}
		else {
			holder.name.setText(mContext.getResources().getString(R.string.new_contact));
			holder.number.setText(" ");
			holder.add.setText(mContext.getResources().getString(R.string.add));
		}
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick (View v) {
				Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
				((Activity) mContext).startActivityForResult(contactPickerIntent, position);
			}
		});

		view.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick (View v) {
				final String[] list = new String[] { "Delete", "Edit" };
				final SharedPreferences mPreferences = mContext.getSharedPreferences(Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
				// alert dialog to delete or edit the contact
				AlertDialog.Builder build = new AlertDialog.Builder(mContext);
				build.setTitle("Choose");
				build.setItems(list, new DialogInterface.OnClickListener() {

					@Override
					public void onClick (DialogInterface dialog, int which) {
						// to delete the contact
						if (list[which].equals("Delete")) {
							mPreferences.edit().remove("NUMBER_" + position);
							mPreferences.edit().remove("NAME_" + position);
							mNumbers.set(position, null);
							mNames.set(position, null);
							notifyDataSetChanged();
						}
						else if (list[which].equals("Edit")) {
							// to edit the contact
							Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
							((Activity) mContext).startActivityForResult(contactPickerIntent, position);
						}
					}
				});

				AlertDialog alertDialog = build.create();

				if (mNumbers.get(position) != null && mNames.get(position) != null) {
					alertDialog.show();
				}
				return true;
			}
		});
		return view;
	}

	public class ViewHolder {

		TextView name;

		TextView number;

		TextView add;
	}

}
