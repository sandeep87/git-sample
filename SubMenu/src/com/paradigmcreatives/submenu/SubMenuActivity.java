package com.paradigmcreatives.submenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

public class SubMenuActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflator = new MenuInflater(getApplication());
		/*SubMenu menu2 = menu.addSubMenu("pra");
		menu2.add("don");*/
		inflator.inflate(R.menu.submenu, menu);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.messages:
			Toast.makeText(this, "messages", Toast.LENGTH_LONG).show();
			return true;
		case R.id.contacts:
			Toast.makeText(this, "contacts", Toast.LENGTH_LONG).show();
			return true;
		case R.id.calllog:
			Toast.makeText(this, "calllog", Toast.LENGTH_LONG).show();
			return true;
		case R.id.gallery:
			Toast.makeText(this, "gallery", Toast.LENGTH_LONG).show();
			return true;
		case R.id.settings:
			Toast.makeText(this, "settings", Toast.LENGTH_LONG).show();
			return true;
		case R.id.games:
			Toast.makeText(this, "games", Toast.LENGTH_LONG).show();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	}
}