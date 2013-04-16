package com.paradigmcreatives.optionsmenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class OptionsMenuActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflator = getMenuInflater();
    	inflator.inflate(R.menu.options_menu, menu);
    	return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
		case R.id.next_opt:
			Toast.makeText(this, "You have chosen the " + getResources().getString(R.string.next) + " menu option",
                    Toast.LENGTH_SHORT).show();
			return true;
		case R.id.previous_opt:
			Toast.makeText(this, "You have chosen the " + getResources().getString(R.string.previous) + " menu option",
                    Toast.LENGTH_SHORT).show();
			return true;
		case R.id.list_opt:
			Toast.makeText(this, "You have chosen the " + getResources().getString(R.string.list) + " menu option",
                    Toast.LENGTH_SHORT).show();
			return true;

		default:
			return super.onOptionsItemSelected(item);
			
		}
    }
}