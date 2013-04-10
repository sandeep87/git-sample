package com.paradigmcreatives.crashreport;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;

/**
 * Handles all the un-handled exception occurring anywhere in the application.
 * It also sends a report to the server. After the report is sent, it restarts
 * the application.<br>
 * <br>
 * 
 * <b>Usage</b><br>
 * In your main activity's <code>onCreate</code> method write<br>
 * <code>
 *         intent = PendingIntent.getActivity(getApplication().getBaseContext(), 0, new Intent(getIntent()), getIntent().getFlags());
 * </code> <br>
 * <br>
 * Pass the <code>intent</code> above to the constructor of
 * <code>ParadigmExceptionHandler</code> along with the <code>Context</code> of
 * the application.<br>
 * <br>
 * Finally set the
 * <code>Thread.setDefaultUncaughtExceptionHandler(ParadigmExceptionHandler instance)</code>
 * with the instance of <code>ParadigmExceptionHandler</code> created earlier.
 * 
 * @author robin
 * 
 */

public class ParadigmExceptionHandler implements
		Thread.UncaughtExceptionHandler {

	private Context context;
	// private UncaughtExceptionHandler defaultUEH;
	private PendingIntent intent;
	private String versionName = "", packageName = "", filePath = "", phoneModel = "",
			androidVersion = "", board = "", brand = "", device = "", display = "", fingerPrint = "", host = "";
	private String id = "", model = "", product = "", tags = "", time = "", type = "", user = "",
			availableInternalMemory = "", totalInternalMemory = "";
	private String cause = "";
	
	private Handler handler;


	public ParadigmExceptionHandler(Context context, PendingIntent intent) {
		super();
		this.context = context;
		this.intent = intent;
		// defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
	}

	public ParadigmExceptionHandler() {
		super();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {

		cause += "To String: " + ex.toString() + "\n";
		
		// Getting the cause of exception and its stack trace
		
		if(ex.getLocalizedMessage() != null) {
			cause += "Localized Message: " + ex.getLocalizedMessage() + "\n";
		}
		
		if (ex.getMessage() != null) {
			cause += "Message: " + ex.getMessage() + "\n";
		}

		if (ex.getCause() != null) {
			cause += "Cause: " + ex.toString() + "\n";
		}

		StackTraceElement[] ele = ex.getStackTrace();
		if (ele != null) {
			for (StackTraceElement el : ele) {
				cause += "   File: " + el.getFileName() + "   Class:"
						+ el.getClassName() + "   Method:" + el.getMethodName()
						+ "   Line:" + el.getLineNumber() + "\n";
			}
		}
		
		// Getting the data of phone
		ArrayList<NameValuePair> data = collectLocalInformation(context);

		// Adding the cause to data
		BasicNameValuePair bnvp = new BasicNameValuePair("exception", cause);
		data.add(bnvp);


		handler = new Handler() {
			public void handleMessage(Message message) {
				System.out.println("ParadigmExceptionHandler.handler.new Handler() {...}.handleMessage()");
				AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
				mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000,intent);
				System.exit(2);
			}
		};		
		
		// Submitting the report
		
		WebServiceWrapper wsw = new WebServiceWrapper(context, "http://72.167.247.185/projectreport/submitreport",
				"Sending Crash Report", WebServiceWrapper.Method.POST, handler, intent);

		wsw.execute(data);

//		printLocalInformation();

		// Restarting the application
//		AlarmManager mgr = (AlarmManager) context
//				.getSystemService(Context.ALARM_SERVICE);
//		mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, intent);
//		System.exit(2);
		// defaultUEH.uncaughtException(thread, ex);

		/*
		 * if(context != null) { //Exit the application
		 * android.os.Process.killProcess(android.os.Process.myPid()); }
		 */
	}

	/**
	 * Gets the available internal memory
	 * 
	 * @return
	 */
	public long getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * Gets the total internal memory
	 * 
	 * @return
	 */
	public long getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

	/**
	 * Collects phone and application specific data and creates an
	 * <code>ArrayList</code> in the form required by the
	 * <code>WebServiceWrapper</code>. This data is sent to the server.
	 * 
	 * @param context
	 *            Application's context
	 * @return
	 */
	ArrayList<NameValuePair> collectLocalInformation(Context context) {
		ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();

		PackageManager pm = context.getPackageManager();
		try {
			PackageInfo pi;
			// Version
			pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			// Package name
			packageName = pi.packageName;
			// Files dir for storing the stack traces
			filePath = context.getFilesDir().getAbsolutePath();
			// Device model
			phoneModel = android.os.Build.MODEL;
			// Android version
			androidVersion = android.os.Build.VERSION.RELEASE;

			board = android.os.Build.BOARD;
			brand = android.os.Build.BRAND;
			// CPU_ABI = android.os.Build.;
			device = android.os.Build.DEVICE;
			display = android.os.Build.DISPLAY;
			fingerPrint = android.os.Build.FINGERPRINT;
			host = android.os.Build.HOST;
			id = android.os.Build.ID;
			// Manufacturer = android.os.Build.;
			model = android.os.Build.MODEL;
			product = android.os.Build.PRODUCT;
			tags = android.os.Build.TAGS;
			time = android.os.Build.TIME + "";
			type = android.os.Build.TYPE;
			user = android.os.Build.USER;

			// Internal Memory
			totalInternalMemory = getTotalInternalMemorySize() + "";
			availableInternalMemory = getAvailableInternalMemorySize() + "";

		} catch (NameNotFoundException e) {
			e.printStackTrace();
			System.out.println("ParadigmExceptionHandler.RecoltInformations()"
					+ e);
		}

		// Adding the values
		BasicNameValuePair bnvp = new BasicNameValuePair("versionName",
				versionName);
		data.add(bnvp);

		bnvp = new BasicNameValuePair("versionName", versionName);
		data.add(bnvp);

		bnvp = new BasicNameValuePair("packageName", packageName);
		data.add(bnvp);

		bnvp = new BasicNameValuePair("phoneModel", phoneModel);
		data.add(bnvp);

		bnvp = new BasicNameValuePair("brand", brand);
		data.add(bnvp);

		bnvp = new BasicNameValuePair("board", board);
		data.add(bnvp);

		bnvp = new BasicNameValuePair("device", device);
		data.add(bnvp);

		bnvp = new BasicNameValuePair("display", display);
		data.add(bnvp);

		bnvp = new BasicNameValuePair("fingerPrint", fingerPrint);
		data.add(bnvp);

		bnvp = new BasicNameValuePair("host", host);
		data.add(bnvp);

		bnvp = new BasicNameValuePair("product", product);
		data.add(bnvp);

		bnvp = new BasicNameValuePair("model", model);
		data.add(bnvp);

		bnvp = new BasicNameValuePair("id", id);
		data.add(bnvp);

		bnvp = new BasicNameValuePair("user", user);
		data.add(bnvp);

		bnvp = new BasicNameValuePair("type", type);
		data.add(bnvp);

		bnvp = new BasicNameValuePair("tags", tags);
		data.add(bnvp);

		bnvp = new BasicNameValuePair("totalInternalMemory",
				totalInternalMemory);
		data.add(bnvp);

		bnvp = new BasicNameValuePair("availableInternalMemory",
				availableInternalMemory);
		data.add(bnvp);

		return data;
	}

	void printLocalInformation() {

		System.out.println("Version: " + versionName);
		System.out.println("PKG: " + packageName);
		System.out.println("File: " + filePath);
		System.out.println("Model: " + phoneModel);

		System.out.println("Android Version: " + androidVersion);
		System.out.println("Board: " + board);
		System.out.println("Brand: " + brand);
		System.out.println("Device: " + device);

		System.out.println("Display: " + display);
		System.out.println("Fngr: " + fingerPrint);
		System.out.println("Host: " + host);
		System.out.println("ID: " + id);

		System.out.println("Model: " + model);
		System.out.println("Product: " + product);
		System.out.println("Tags: " + tags);
		System.out.println("Time: " + time);

		System.out.println("User: " + user);
		System.out.println("Avail Mem: " + availableInternalMemory);
		System.out.println("Total Mem: " + totalInternalMemory);
	}

}
