package com.example.internalstore;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Menu extends Activity implements OnClickListener {

	String activities[] = { "ST", "SETTINGS", "INTERNALSTORE" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		for (int i = 0; i < activities.length; i++) {
			if (id == getResources().getIdentifier("b" + i, "id",
					"com.example.internalstore")) {
				Intent intent = new Intent("com.example.internalstore"
						+ activities[i]);
				startActivity(intent);
			}
		}

	}

}
