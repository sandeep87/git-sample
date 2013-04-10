package com.paradigmcreatives.database;

import android.database.Cursor;


public abstract class Config {

	public static Config Instance() {
		return ourInstance;
	}

	private static Config ourInstance;

	protected Config() {
		ourInstance = this;
	}
	
	public abstract void setStatusValue(String statusFlag);
	public abstract boolean getStatusFlag();
	public abstract boolean dropStatusTable();
		
	public abstract boolean dropCurrentLocTable();
	public abstract void setValuesToCurrentLoc(double curlat, double curlng,double endlat, double endlng, String srtaddress, String endaddress, double alertTime, double avgSpeed, String remainingDistance, String remainingTime);
	public abstract Cursor getCurrentStatusTable();
	
	public abstract boolean dropGeoLocationTable();
	public abstract void setValuesToGeoLocation(String latInString, String lngInString);
	public abstract Cursor getGeoLocationTable();
	
	public abstract boolean dropMaxMinLocationTable();
	public abstract void setValuesToMaxMinLocation(double minlat, double maxlat,double minlng, double maxlng);
	public abstract Cursor getMaxMinLocationTable();
	
	
}
