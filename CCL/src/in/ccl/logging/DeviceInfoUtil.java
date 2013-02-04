package in.ccl.logging;

import in.ccl.ui.R;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;

public class DeviceInfoUtil {

	private final String TAG = "DeviceInfoUtil";

	Context mContext;

	String versionName, packageName, filePath, phoneModel, androidVersion, board, brand, device, display, fingerPrint, host, id, model, product, tags, time, type, user, totalInternalMemory, availableInternalMemory, screenResolution;

	public DeviceInfoUtil (Context mContext) {
		this.mContext = mContext;
	}

	public ArrayList <String> collectDeviceInfo () {
		ArrayList <String> deviceInfoList = new ArrayList <String>();
		PackageManager pm = null;

		if (mContext != null) {
			pm = mContext.getPackageManager();
		}

		try {
			if (pm != null) {
				PackageInfo pi;
				pi = pm.getPackageInfo(mContext.getPackageName(), 0);

				// Version
				versionName = "versionName" + " : " + pi.versionName;

				// Package name
				packageName = "packageName" + " : " + pi.packageName;
			}

			if (mContext != null) {
				// Files dir for storing the stack traces
				filePath = "filePath" + " : " + mContext.getFilesDir().getAbsolutePath();
			}

			// Device model
			phoneModel = "phoneModel  : " + android.os.Build.MODEL;

			// Android version
			androidVersion = "androidVersion : " + android.os.Build.VERSION.RELEASE;

			board = "board : " + android.os.Build.BOARD;
			brand = "brand : " + android.os.Build.BRAND;

			// CPU_ABI = android.os.Build.;
			device = "device : " + android.os.Build.DEVICE;
			display = "display : " + android.os.Build.DISPLAY;
			fingerPrint = "fingerPrint : " + android.os.Build.FINGERPRINT;
			host = "host : " + android.os.Build.HOST;
			id = "id : " + android.os.Build.ID;

			// Manufacturer = android.os.Build.;
			model = "model : " + android.os.Build.MODEL;
			product = "product : " + android.os.Build.PRODUCT;
			tags = "tags : " + android.os.Build.TAGS;
			time = "time : " + android.os.Build.TIME;
			type = "type : " + android.os.Build.TYPE;
			user = "user : " + android.os.Build.USER;

			// Internal Memory
			totalInternalMemory = "totalInternalMemory : " + getTotalInternalMemorySize() + "";
			availableInternalMemory = "availableInternalMemory : " + getAvailableInternalMemorySize() + "";

			if (mContext != null && mContext instanceof Activity) {
				// Screen Resolution
				DisplayMetrics dm = new DisplayMetrics();
				((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
				screenResolution = "screenResolution : " + dm.widthPixels + " x " + dm.heightPixels;
			}

			deviceInfoList.add(versionName);
			deviceInfoList.add(packageName);
			deviceInfoList.add(filePath);
			deviceInfoList.add(phoneModel);
			deviceInfoList.add(androidVersion);
			deviceInfoList.add(board);
			deviceInfoList.add(brand);
			deviceInfoList.add(device);
			deviceInfoList.add(display);
			deviceInfoList.add(fingerPrint);
			deviceInfoList.add(host);
			deviceInfoList.add(id);
			deviceInfoList.add(model);
			deviceInfoList.add(product);
			deviceInfoList.add(tags);
			deviceInfoList.add(time);
			deviceInfoList.add(type);
			deviceInfoList.add(user);
			deviceInfoList.add(totalInternalMemory);
			deviceInfoList.add(availableInternalMemory);
			deviceInfoList.add(screenResolution);

		}
		catch (NameNotFoundException e) {
			Log.e(TAG, e + "");
		}

		return deviceInfoList;
	}

	/**
	 * Gets the total internal memory
	 * 
	 * @return
	 */
	public long getTotalInternalMemorySize () {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

	/**
	 * Gets the available internal memory
	 * 
	 * @return
	 */
	public long getAvailableInternalMemorySize () {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	/**
	 * Check whether external media is writable or not.
	 * 
	 * @return <code>true</code> if external media is available and have some free memory to write, else <code>flase</code>
	 */
	public static boolean mediaWritable () {
		boolean writable = false;
		double megAvailable = 0;

		String state = Environment.getExternalStorageState();

		// If media is mounted and read-only then verify whether space is
		// available or not
		if (state.equals(Environment.MEDIA_MOUNTED) && !state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {

			StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
			long bytesAvailable = (long) stat.getBlockSize() * (long) stat.getBlockCount();

			megAvailable = bytesAvailable / 1048576;

			if (megAvailable > 0) {
				writable = true;
			}

		}

		return writable;
	}

	/**
	 * Check if media is readable or not
	 * 
	 * @return <code>true</code> if external media is readable, otherwise <code>false</code>
	 */
	public static boolean mediaReadable () {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * This method is used to get the path of the SD card
	 * 
	 * @return: Path to the SD card
	 */
	public static String getSdCardPath () {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().getPath() + File.separator;
		}
		else {
			return null;
		}
	}

	/**
	 * Returns the application directory
	 * 
	 * @param context
	 * @return Application directory, <code>null</code> if not available
	 */
	public static String getAppDirectory (Context context) {
		if (context == null) {
			return null;
		}

		String sdCardPath = DeviceInfoUtil.getSdCardPath();
		if (sdCardPath != null) {
			return sdCardPath + context.getResources().getString(R.string.app_name) + File.separator;
		}
		else {
			return null;
		}

	}

}