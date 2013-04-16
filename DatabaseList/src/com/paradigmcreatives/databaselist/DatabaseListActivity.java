package com.paradigmcreatives.databaselist;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class DatabaseListActivity extends ListActivity {
    MyAdapter mListAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Cursor mycur = null;
        
        mycur = do_stuff_here_to_obtain_a_cursor_of_query_results();
        mListAdapter = new MyAdapter(DatabaseListActivity.this, mycur);
        setListAdapter(mListAdapter);
    }


    private class MyAdapter extends ResourceCursorAdapter {

        public MyAdapter(Context context, Cursor cur) {
            super(context, R.layout.main, cur);
        }

        @Override
        public View newView(Context context, Cursor cur, ViewGroup parent) {
            LayoutInflater li = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return li.inflate(R.layout.main, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cur) {
            TextView tvListText = (TextView)view.findViewById(R.id.list_text);
            CheckBox cbListCheck = (CheckBox)view.findViewById(R.id.list_checkbox);

            tvListText.setText(cur.getString(cur.getColumnIndex(Datenbank.DB_NAME)));
            cbListCheck.setChecked((cur.getInt(cur.getColumnIndex(Datenbank.DB_STATE))==0? false:true))));
        }
    }

    }
}