package com.paradigmcreatives.alarmdemo;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

public class AlarmDemoActivity extends Activity {
	TimePicker timePicker;
    DatePicker datePicker;
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
 
        //---Button view---
        Button btnOpen = (Button) findViewById(R.id.btnSetAlarm);
        btnOpen.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View v) {                
                timePicker = (TimePicker) findViewById(R.id.timePicker);
                datePicker = (DatePicker) findViewById(R.id.datePicker);                   
 
                //---use the AlarmManager to trigger an alarm---
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);                 
 
                //---get current date and time---
                Calendar calendar = Calendar.getInstance();       
 
                //---sets the time for the alarm to trigger---
                calendar.set(Calendar.YEAR, datePicker.getYear());
                calendar.set(Calendar.MONTH, datePicker.getMonth());
                calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());                 
                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                calendar.set(Calendar.SECOND, 0);
 
                //---PendingIntent to launch activity when the alarm triggers-
                PendingIntent displayIntent = PendingIntent.getActivity(
                    getBaseContext(), 0, 
                    new Intent("net.learn2develop.AlarmDetails"), 0);               
 
                //---sets the alarm to trigger---
                alarmManager.set(AlarmManager.RTC_WAKEUP, 
                    calendar.getTimeInMillis(), displayIntent);
            }
        }); 
    }
}