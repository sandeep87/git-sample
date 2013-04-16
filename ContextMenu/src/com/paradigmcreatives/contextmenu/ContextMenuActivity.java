package com.paradigmcreatives.contextmenu;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class ContextMenuActivity extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names)));

        registerForContextMenu(getListView());
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo) {
     	super.onCreateContextMenu(menu, v, menuInfo);
     	MenuInflater inflater = getMenuInflater();
     	inflater.inflate(R.menu.menu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo mContextMenuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
    	String[] names = getResources().getStringArray(R.array.names);
    	switch (item.getItemId()) {
		case R.id.edit:
			/*Toast.makeText(this, "You have chosen the " + getResources().getString(R.string.edit) +
                    " context menu option for " + names[(int)mContextMenuInfo.id],
                    Toast.LENGTH_SHORT).show();*/
			Toast.makeText(getApplicationContext(), "You chose the edit option", Toast.LENGTH_LONG).show();
        return true;
		case R.id.save:
			Toast.makeText(getApplicationContext(), "You chose the save option", Toast.LENGTH_LONG).show();
	        return true;
		case R.id.delete:
			Toast.makeText(getApplicationContext(), "You chose the delete option", Toast.LENGTH_LONG).show();
	        return true;
		case R.id.view:
			Toast.makeText(getApplicationContext(), "You chose the view option", Toast.LENGTH_LONG).show();
	        return true;

		default:
			return super.onContextItemSelected(item);
		}
    	
    }
}