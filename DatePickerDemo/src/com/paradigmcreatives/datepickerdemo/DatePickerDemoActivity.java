package com.paradigmcreatives.datepickerdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;


import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;


public class DatePickerDemoActivity extends Activity {
	private DatePicker picker;
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        picker = (DatePicker)findViewById(R.id.datePicker);
    new AlertDialog.Builder(this)
    .setTitle("Date")
    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			
		}
	})
	.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			
			
		}
	}).show();
        
    }

		
    }
    
    
