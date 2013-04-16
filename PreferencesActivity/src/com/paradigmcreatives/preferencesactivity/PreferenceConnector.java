package com.paradigmcreatives.preferencesactivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceConnector {

	public static final String NAME = "NAME";
	public static final String SURNAME = "SURNAME";
	public static final String AGE = "AGE";
	public static final String PREF_NAME = "People Preferences";
	public static final int MODE = Context.MODE_PRIVATE;
	
	public static void writeBoolean (Context context, String key, boolean value) {
		getEditor(context).putBoolean(key, value).commit();
	}

	public static boolean readBoolean (Context context, String key, boolean value) {
		return getPreferences(context).getBoolean(key, value);
	}
	
	public static void writeInteger (Context context, String key, int value) {
		getEditor(context).putInt(key, value).commit();
	}
	
	public static int readInteger (Context context, String key, int value) {
		return getPreferences(context).getInt(key, value);
	}

	public static void writeString(Context context, String key, String value) {
		getEditor(context).putString(key, value).commit();
	}
	
	public static String readString(Context context, String key, String value) {
		return getPreferences(context).getString(key, value);
	}
	
	public static void writeFloat(Context context, String key, float value) {
		getEditor(context).putFloat(key, value).commit();
	}
	
	public static float readFloat(Context context, String key, float value) {
		return getPreferences(context).getFloat(key, value);
	}
	
	public static void writeLong(Context context, String key, long value) {
		getEditor(context).putLong(key, value).commit();
	}
	
	public static long readLong(Context context, String key, long value) {
		return getPreferences(context).getLong(key, value);
	}

	public static SharedPreferences getPreferences(Context context) {
		return context.getSharedPreferences(PREF_NAME, MODE);
	}
	
	public static Editor getEditor(Context context) {
		return getPreferences(context).edit();
	}
	
}
