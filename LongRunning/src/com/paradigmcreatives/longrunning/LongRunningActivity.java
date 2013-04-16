package com.paradigmcreatives.longrunning;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class LongRunningActivity extends Activity {
    /** Called when the activity is first created. */
    
    private static final String[] LIST_ITEMS = new String[]{"one", "two", "three", "five"};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setupButton();
        setupListView();

    }

    private void setupListView() {
        ListView lv = (ListView) findViewById(R.id.list_view);
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, LIST_ITEMS));
    }

    private void setupButton() {
        Button button = (Button) findViewById(R.id.load_btn);
        button.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v) {
                createNotification();
            }
        });
    }

    private void createNotification() {
        new DownloadTask(getApplicationContext()).execute(0);
    }
}


