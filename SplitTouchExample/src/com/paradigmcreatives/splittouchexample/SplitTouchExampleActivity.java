package com.paradigmcreatives.splittouchexample;


import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class SplitTouchExampleActivity extends Activity {
	 public static final String[] sMovieStrings = {
	        "Cocktail", "Players", "Over the Top", "Rocky", "Rambo"};
	    public static final String[] sActorStrings = {
	        "Sylvester Stallone", "Amir Khan", "Kareena Kapoor", "Salmaan Khan", "Priyanka Chopra"};
	     
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	 
	        setContentView(R.layout.activity_split_touch_example);
	        ListView list1 = (ListView) findViewById(R.id.list1);
	        ListView list2 = (ListView) findViewById(R.id.list2);
	        ListAdapter adapter = new ArrayAdapter<String>(this,
	                R.layout.simple_list_item_1, sMovieStrings);
	        ListAdapter adapter2 = new ArrayAdapter<String>(this,
	                R.layout.simple_list_item_1, sActorStrings);
	   
	        list1.setAdapter(adapter);
	        list2.setAdapter(adapter2);
	 
	        list1.setOnItemClickListener(itemClickListener);
	        list2.setOnItemClickListener(itemClickListener2);
	         
	    }
	 
	     
	    private final OnItemClickListener itemClickListener2 = new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            String[] responses = getResources().getStringArray(R.array.actor_responses);
	            String response = responses[position];
	 
	 
	            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
	            		SplitTouchExampleActivity.this);
	            dlgAlert.setMessage(response);
	            dlgAlert.setTitle("Biodata");
	            dlgAlert.setPositiveButton("OK", null);
	            dlgAlert.setCancelable(true);
	            dlgAlert.create().show();
	        }
	    };
	     
	    private final OnItemClickListener itemClickListener = new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            String[] responses = getResources().getStringArray(R.array.movie_responses);
	            String response = responses[position];
	 
	 
	            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
	            		SplitTouchExampleActivity.this);
	            dlgAlert.setMessage(response);
	            dlgAlert.setTitle("MovieDetail");
	            dlgAlert.setPositiveButton("OK", null);
	            dlgAlert.setCancelable(true);
	            dlgAlert.create().show();
	        }
	    };

}
