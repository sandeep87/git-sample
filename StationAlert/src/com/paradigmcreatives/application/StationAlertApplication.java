package com.paradigmcreatives.application;

import com.paradigmcreatives.database.SQLiteConfig;

import android.app.Application;

public class StationAlertApplication extends Application{

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// here we are creating the database for the application
		// and i am writing precompiled statement.
		// so its esay to get the database values instead of initilizing all the time.
		new SQLiteConfig(StationAlertApplication.this);
		
		
	}
}
