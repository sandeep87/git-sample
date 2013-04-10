package com.paradigmcreatives.bachao.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import com.paradigmcreatives.bachao.logging.Logger;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class Util {

	private static Util singleInstance;

	private LocationManager locationManager;

	private double latitude;

	private double longitude;

	private String mapUrl;

	private String address_location = "";

	private Context mContext;

	/**
	 * private constructor
	 */
	private Util () {
	}

	/**
	 * Creating single object of this class. not required to create a new object each time when it was invoked.
	 * 
	 * @return single object of Util class.
	 */
	public static Util getInstance () {

		if (singleInstance == null) {
			synchronized (Util.class) {
				if (singleInstance == null) {
					singleInstance = new Util();
				}
			}
		}
		return singleInstance;
	}

	/**
	 * Checks network information i.e network is available or not.
	 * 
	 * @param context Context
	 * @return true if network is connected or connecting, else false.
	 */
	public boolean isOnline (Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null) {
			return networkInfo.isConnectedOrConnecting();
		}
		return false;
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
		return null;

	}

	public String getLatLong (Context context) {
		mContext = context;
		locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
		List <String> provider = locationManager.getAllProviders();
		String mBestProvider = null;

		if (provider.size() > 0) {
			mBestProvider = provider.get(0); // select bestProvider
		}
		/** getting Current location from provider */
		Location mCurrentLocation = locationManager.getLastKnownLocation(mBestProvider);

		if (mCurrentLocation != null) {
			latitude = mCurrentLocation.getLatitude();
			longitude = mCurrentLocation.getLongitude();
			mapUrl = " http://maps.google.com/?q=" + latitude + "," + longitude;
			String shortenUrl = urlShorten(mapUrl);
			String url;
			try {
				final JSONObject json = new JSONObject(shortenUrl);
				final String id = json.getString("id");
				if (json.has("id")) {
					url = id;
					return url;
				}
				else {
					url = " ";
					return url;
				}
			}
			catch (JSONException e) {
				e.printStackTrace();
				url = " ";
				return url;
			}
		}
		else {
			mapUrl = " ";
			return mapUrl;
		}

	}

	public String urlShorten (final String longUrl) {
		final String GOOGLE_URL = "https://www.googleapis.com/urlshortener/v1/url";
		try {
			// Set connection timeout to 5 secs and socket timeout to 10 secs
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 5000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			int timeoutSocket = 10000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

			HttpClient hc = new DefaultHttpClient(httpParameters);

			HttpPost request = new HttpPost(GOOGLE_URL);
			request.setHeader("Content-type", "application/json");
			request.setHeader("Accept", "application/json");

			JSONObject obj = new JSONObject();
			obj.put("longUrl", longUrl);
			request.setEntity(new StringEntity(obj.toString(), "UTF-8"));

			HttpResponse response = hc.execute(request);

			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				return out.toString();
			}
			else {
				return null;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getDetailedAddress (Context context) {
		mContext = context;
		Geocoder mGeocoder = new Geocoder(mContext, Locale.getDefault());
		List <Address> add = null;
		// String data = null;
		try {
			add = mGeocoder.getFromLocation(latitude, longitude, 2);
			StringBuilder sb = new StringBuilder();
			if (add.size() > 0) {
				Address address = add.get(0);
				for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
					sb.append(address.getAddressLine(i)).append("  ");

				}
				address_location = sb.toString();
			}
		}
		catch (IOException e1) {
			Logger.logStackTrace(e1);
		}
		return address_location;

	}

	public void sendSms (String phone, String message, Context context) {

		mContext = context;
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		PendingIntent sentPI = PendingIntent.getBroadcast(mContext, 0, new Intent(SENT), 0);
		PendingIntent sentDEL = PendingIntent.getBroadcast(mContext, 0, new Intent(DELIVERED), 0);

		try {
			mContext.registerReceiver(new BroadcastReceiver() {

				@Override
				public void onReceive (Context context, Intent intent) {
					switch (getResultCode()) {
						case Activity.RESULT_OK:
							break;
						case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
							Toast.makeText(mContext, "Generic failure", Toast.LENGTH_SHORT).show();
							break;
						case SmsManager.RESULT_ERROR_NO_SERVICE:
							Toast.makeText(mContext, "No service", Toast.LENGTH_SHORT).show();
							break;
						case SmsManager.RESULT_ERROR_NULL_PDU:
							Toast.makeText(mContext, "Null PDU", Toast.LENGTH_SHORT).show();
							break;
						case SmsManager.RESULT_ERROR_RADIO_OFF:
							Toast.makeText(mContext, "Radio off", Toast.LENGTH_SHORT).show();
							break;
					}
					mContext.unregisterReceiver(this);
				}
			}, new IntentFilter(SENT));
		}
		catch (IllegalArgumentException e) {
			Log.i("br1", e.getMessage());
		}

		try {
			mContext.registerReceiver(new BroadcastReceiver() {

				@Override
				public void onReceive (Context context, Intent intent) {
					switch (getResultCode()) {
						case Activity.RESULT_OK:
							Toast.makeText(mContext, "SMS delivered", Toast.LENGTH_SHORT).show();
							break;
						case Activity.RESULT_CANCELED:
							Toast.makeText(mContext, "SMS not delivered", Toast.LENGTH_SHORT).show();
							break;
					}
					mContext.unregisterReceiver(this);
				}
			}, new IntentFilter(DELIVERED));
		}
		catch (IllegalArgumentException e) {
			Log.i("br1", e.getMessage());
		}

		SmsManager manager = SmsManager.getDefault();
		if (message != null)
			manager.sendTextMessage(phone, null, message, sentPI, sentDEL);

		ContentValues values = new ContentValues();

		values.put("address", phone);
		values.put("body", message);

		mContext.getContentResolver().insert(Uri.parse("content://sms/sent"), values);

	}

}
