package com.paradigmcreatives.database;

import com.paradigmcreatives.globalconfig.GlobalConfiguration;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class SQLiteConfig extends Config{

	private final SQLiteDatabase myDatabase;
	private final SQLiteStatement mySetStatusStatement;
	private final SQLiteStatement myGetStausStatement;
	private final SQLiteStatement myDropStatusStatement;
	private final SQLiteStatement mySetCurrentstatustatement;
	private final SQLiteStatement myDropCurrentstatusStatement;
	private final SQLiteStatement mySetGeoLocationStatement;
	private final SQLiteStatement myDropGeoLocationStatement;
	
	private final SQLiteStatement mySetMaxMinLocationStatement;
	private final SQLiteStatement myDropMaxMinLocationStatement;
	
	public SQLiteConfig(Context context){
		myDatabase = context.openOrCreateDatabase("config.db", Context.MODE_PRIVATE, null);
		switch (myDatabase.getVersion()) {
		case 0:
			myDatabase.execSQL("CREATE TABLE status (status_flag VARCHAR )");
			myDatabase.execSQL("CREATE TABLE currentstatus (curlat INTEGER, curlng INTEGER, endlat INTEGER, endlng INTEGER, srtaddress VARCHAR, endaddress VARCHAR, alertTime INTEGER, avgspeed INTEGER, remainingDisatance VARCHAR, remainingTime VARCHAR )");
			myDatabase.execSQL("CREATE TABLE geolocation (lat VARCHAR, lng VARCHAR  )");
			myDatabase.execSQL("CREATE TABLE maxminlocation (minlat INTEGER, maxlat INTEGER, minlng INTEGER, maxlng INTEGER)");
			break;
		}
		myDatabase.setVersion(2);
		mySetStatusStatement = myDatabase.compileStatement("INSERT OR REPLACE INTO status ( status_flag ) VALUES ( ? )");
		myGetStausStatement = myDatabase.compileStatement("SELECT status_flag FROM status");
		myDropStatusStatement = myDatabase.compileStatement("DELETE FROM status");
		mySetCurrentstatustatement = myDatabase.compileStatement("INSERT OR REPLACE INTO currentstatus (curlat, curlng, endlat, endlng, srtaddress, endaddress, alertTime, avgspeed, remainingDisatance, remainingTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		myDropCurrentstatusStatement = myDatabase.compileStatement("DELETE FROM currentstatus");
		mySetGeoLocationStatement = myDatabase.compileStatement("INSERT OR REPLACE INTO geolocation (lat, lng) VALUES (?, ?)");
		myDropGeoLocationStatement = myDatabase.compileStatement("DELETE FROM geolocation");
		
		mySetMaxMinLocationStatement = myDatabase.compileStatement("INSERT OR REPLACE INTO maxminlocation ( minlat, maxlat, minlng, maxlng) VALUES ( ?, ?, ?, ? )");
		myDropMaxMinLocationStatement = myDatabase.compileStatement("DELETE FROM maxminlocation");
		
	}

	// Retriving only the flag so that it ensure that its data is present or not.
	@Override
	public boolean getStatusFlag() {
		boolean flag;
		try{
			String s = myGetStausStatement.simpleQueryForString();
			s = s.toLowerCase().intern();
			if (s == "true") {
				flag = true;
			}else{
				flag = false;
			}
			
		}catch (SQLException e) {
			flag = false;
			//System.err.println(GlobalConfiguration.APP_NAME +" ERROR getting status flag from db "+e);
		}
		return flag;
	}

/*	@Override
	public Cursor getAllValue() {
		// Retriving all values from the row
		Cursor answer = null;
		try {
			answer = myDatabase.rawQuery("SELECT srtlat, srtlng, endlat, endlng, srtaddress, endaddress, crtaddress, alerttime, durationtime, avgspeed, flag FROM config", null);
		} catch (SQLException e) {
			answer = null;
		}
		return answer;
	}*/

	// we are inserting status of the flag. setting string true/false.
	@Override
	public void setStatusValue(String statusFlag) {
		
		mySetStatusStatement.bindString(1, statusFlag);
		try {
			mySetStatusStatement.execute();
		} catch (SQLException e) {
			System.err.println(GlobalConfiguration.APP_NAME+" Error setting status falg to db "+e);
		}
		
	}

	// Deleting rows from the database
	@Override
	public boolean dropStatusTable() {
		boolean result = false;
		try {
			myDropStatusStatement.execute();
			result = true;
		} catch (SQLException e) {
			result = false;
			System.err.println(GlobalConfiguration.APP_NAME +" Error in deleting all rows in db "+e);
		}
		return result;
	}

	@Override
	public boolean dropCurrentLocTable() {
		// Deleting rows from the database
		boolean flag = false;
		try {
			myDropCurrentstatusStatement.execute();
			flag = true;
		} catch (SQLException e) {
			flag = false;
			System.err.println(GlobalConfiguration.APP_NAME+" Error in deleting all rows in db "+e);
		}
		return flag;
	}

	

	@Override
	public Cursor getCurrentStatusTable() {
		Cursor answer = null;
		try {
			answer = myDatabase.rawQuery("SELECT * FROM currentstatus", null);
			
			/*answer.moveToFirst();
			do{
				double srtLat = answer.getDouble(answer.getColumnIndex("curlat"));
				double srtLng = answer.getDouble(answer.getColumnIndex("curlng"));
				double endLat = answer.getDouble(answer.getColumnIndex("endlat"));
				double endLng = answer.getDouble(answer.getColumnIndex("endlng"));
				double at = answer.getDouble(answer.getColumnIndex("alertTime"));
				double ag = answer.getDouble(answer.getColumnIndex("avgspeed"));
				double rt = answer.getDouble(answer.getColumnIndex("remainingTime"));
				System.out.println(" ---------- gettingh bvalue : "+ srtLat);
				
			}while(answer.moveToNext());*/
			
		} catch (SQLException e) {
			answer = null;
			System.err.println(GlobalConfiguration.APP_NAME+" Error getting  "+e);
		}
		return answer;
		
	}

	@Override
	public void setValuesToCurrentLoc(double curlat, double curlng,
			double endlat, double endlng, String srtaddress, String endaddress, double alertTime, double avgSpeed,
			String remainingDistance, String remainingTime) {
		
		//System.out.println("----------setting ----------");
		
		mySetCurrentstatustatement.bindDouble(1, curlat);
		mySetCurrentstatustatement.bindDouble(2, curlng);
		mySetCurrentstatustatement.bindDouble(3, endlat);
		mySetCurrentstatustatement.bindDouble(4, endlng);
		mySetCurrentstatustatement.bindString(5, srtaddress);
		mySetCurrentstatustatement.bindString(6, endaddress);
		mySetCurrentstatustatement.bindDouble(7, alertTime);
		mySetCurrentstatustatement.bindDouble(8, avgSpeed);
		mySetCurrentstatustatement.bindString(9, remainingDistance);
		mySetCurrentstatustatement.bindString(10, remainingTime);
			try {
				mySetCurrentstatustatement.execute();
				//System.out.println(" -----executed ------");
			} catch (SQLException e) {
			
				System.err.println(GlobalConfiguration.APP_NAME+" Error insering row in table "+ e);
				
		}
		
	}

	// Delete all the data in the table
	@Override
	public boolean dropGeoLocationTable() {
		boolean flag = false;
		try {
			myDropGeoLocationStatement.execute();
			flag = true;
		} catch (SQLException e) {
			flag = false;
			System.err.println(GlobalConfiguration.APP_NAME + " Error deleting all rows in db "+ e);
		}
		return flag;
	}

	// Set the values in the GeoLocation Tables 
	// lat and lng are stored in the String. if we store in int then we may problem with pricision values
	@Override
	public void setValuesToGeoLocation(String latInString, String lngInString) {
		mySetGeoLocationStatement.bindString(1, latInString);
		mySetGeoLocationStatement.bindString(2, lngInString);
			try {
				mySetGeoLocationStatement.execute();
				//System.out.println(" -----executed ------");
			} catch (SQLException e) {
				System.err.println(GlobalConfiguration.APP_NAME+" Error setting row in table "+ e);
		}
		
	}

	// Return the all the values of the table
	@Override
	public Cursor getGeoLocationTable() {
		Cursor answer = null;
		try {
			answer = myDatabase.rawQuery("SELECT * FROM geolocation", null);
			
		} catch (SQLException e) {
			answer = null;
			System.err.println(GlobalConfiguration.APP_NAME+" Error getting row from table "+ e);
		}
		return answer;
	}

	@Override
	public boolean dropMaxMinLocationTable() {
		boolean flag = false;
		try {
			myDropMaxMinLocationStatement.execute();
			flag = true;
		} catch (SQLException e) {
			flag = false;
			System.err.println(GlobalConfiguration.APP_NAME + " Error deleting all rows in db "+ e);
		}
		return flag;
	}

	@Override
	public void setValuesToMaxMinLocation(double minlat, double maxlat,double minlng, double maxlng) {
		
		mySetMaxMinLocationStatement.bindDouble(1, minlat);
		mySetMaxMinLocationStatement.bindDouble(2, maxlat);
		mySetMaxMinLocationStatement.bindDouble(3, minlng);
		mySetMaxMinLocationStatement.bindDouble(4, maxlng);
			try {
				
				mySetMaxMinLocationStatement.execute();
								
			} catch (SQLException e) {
				System.err.println(GlobalConfiguration.APP_NAME+" Error setting row in table "+ e);
		}
		
	}

	@Override
	public Cursor getMaxMinLocationTable() {
		Cursor answer = null;
		try {
			answer = myDatabase.rawQuery("SELECT * FROM maxminlocation", null);
			
		} catch (SQLException e) {
			answer = null;
			System.err.println(GlobalConfiguration.APP_NAME+" Error getting row from table "+ e);
		}
		return answer;
	}
	
}
