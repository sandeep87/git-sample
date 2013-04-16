package com.paradigmcreatives.listfruit;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ListFruitActivity extends ListActivity {
    /** Called when the activity is first created. */
	static final String[] FRUITS = new String[] {"Apple", "Banana", "Coconut", "Mango", "Papaya", "Guava"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fruit);
        setListAdapter(new ArrayAdapter<String>(this, R.layout.fruit, FRUITS));
        ListView listView = getListView();
        listView.setTextFilterEnabled(true);
        
        listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Toast.makeText(getApplicationContext(), ((TextView)view).getText(), Toast.LENGTH_LONG).show();
				
			}
        	
        });
        
    }
}