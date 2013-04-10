package com.paradigmcreatives.activity;


import java.io.IOException;

import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paradigmcreatives.crashreport.ParadigmExceptionHandler;
import com.paradigmcreatives.database.Config;
import com.paradigmcreatives.dialog.CustomProgressDialog;
import com.paradigmcreatives.listner.MyLocationListner;

public class MainActivity extends Activity implements LocationListener{
	
	private Context mContext;
	private boolean mNetwork;
	private String mAddress;
	private boolean mPassive;
	private boolean mGps;
	private double mSrtLat;
	private double mSrtLng;
	private LocationManager mLocationManager;
	private boolean myLocationFlag;
	private TextView mCurrentLocation;
	public static  Handler mHandler;
	private Button mButEnterLoc;
	private MyLocationListner myLocationListner;
	private List<String> providers;
	private Thread newThread;
	private CustomProgressDialog customProgressDialog;
	private Object userAddress;
	private double userLat;
	private double userLng;
	private InputMethodManager inputMethodManager;
	private Button mButInfo;
	public static boolean applicationOut;
	
	private int gps=0;
	private LocationManager locationManager;
	private Timer timer;
	private Handler handler;
	private boolean flag;
	private boolean ioe;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = MainActivity.this;

		PendingIntent intent = PendingIntent.getActivity(getApplication().getBaseContext(), 0, new Intent(getIntent()), getIntent().getFlags());
		Thread.setDefaultUncaughtExceptionHandler(new ParadigmExceptionHandler(this, intent));

		Intent lastIntent = getIntent();
		if (lastIntent != null) {
			applicationOut = lastIntent.getBooleanExtra("applicationOut", false);
			//System.out.println(" application out "+applicationOut);
		}
		
		
		
		if (flag) {
			Intent intent1 = new Intent(mContext, TabViewActivity.class);
			intent1.putExtra("dbStatus", false);
			intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent1);
		}

		setContentView(R.layout.main_activity_layout);

		//myLocationListner = new MyLocationListner(mContext);

		//customProgressDialog = new CustomProgressDialog(mContext);
		
		inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		initializeUI();
		mAddress = "Failed to fetch current location ...";
		
		 handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == 0) {
					locationManager.removeUpdates(MainActivity.this);
					if (operations()) {
						mCurrentLocation.setText(mAddress);
					}
					customProgressDialog.dismiss();
				}else if (msg.what ==1) {
					// 1 means no location found from gps. ask user his location details 
					alertDialogwithEditText();
					customProgressDialog.dismiss();
				}
			}
		};

		//mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		//providers = checkProviders();
		
		/*newThread= new Thread(){
			@Override
			public void run() {
				super.run();
				Looper.prepare();
				
				if( !myLocationFlag && mNetwork) {
					callingNetwork();
					if(!myLocationFlag && mPassive) {
						callingPassive();
						if (!myLocationFlag && mGps) {
							callingGPS();
						}
					}
				}else if (!myLocationFlag && mPassive) {
					callingPassive();
					if (!myLocationFlag && mGps) {
						callingGPS();
					}
				}else if (!myLocationFlag && mGps) {
					callingGPS();
				}

				// NOTE to get address from location we need to get to have Internet connection.
				// else we will get error saying Parsing Response Failed
				
				if (myLocationFlag) {
					//Note we are trying to bring 3 result set out of 5. since 5 is the max resultset provided by Google
				
					operations();
					
				}
				mHandler.sendEmptyMessage(0);
				Looper.loop();
			}
		};*/

		/*if (!mGps) {
			settingGPS();
		}else{
			if (customProgressDialog != null) {
				customProgressDialog.loading("", "Determining Current Location...", true);
				newThread.start();
			}
		}*/

		/*mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == 0 ) {
					run();
					
				}else if (msg.what == 1) {
					
					mSrtLat = myLocationListner.getlat();
					mSrtLng = myLocationListner.getlng();
					
					myLocationListner.stopLocationListner();
					operations();
				}
				
				//myLocationListner.stopLocationListner();
			}

			private void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						mCurrentLocation.setText(mAddress);
						customProgressDialog.dismiss();
					}
				});
			}
		};*/
	}
	
	
    @Override	
    public void onResume() { 
        super.onResume();
        timer = new Timer();
        flag =Config.Instance().getStatusFlag();
       // System.out.println(" flag :" +gps);
        
        customProgressDialog = new CustomProgressDialog(MainActivity.this);
        
        if(gps<2 & !flag){
	        try {
				// get location manager to check gps availability
	        	locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0, MainActivity.this);
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,0, MainActivity.this);
			
				boolean isGPS = locationManager.isProviderEnabled (LocationManager.GPS_PROVIDER); 
				
				//System.out.println(" gps status : "+isGPS);
	
				if(!isGPS){
					gps++;
					if(gps<2) {
						settingGPS();
					}
					else{
						alertDialogwithEditText();
						//finish();
					}
				}
				else{
					gps=2;				
					//gps is available, do actions here
					customProgressDialog.loading("", "Fetching Current Location...", true);
					//timer.schedule(timerTask, 1 * 60 * 1000);
					timer.schedule(timerTask,  10000);
				}				
			
		 	} catch (Exception e1) {
		 		gps++;
		 		if(gps<2) settingGPS();
		 		else finish();
			}
        }
    }
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		try{
		locationManager.removeUpdates(this);
		//customProgressDialog.dismiss();
		}catch (Exception e) {
			System.err.println(" This caught exception "+e);
		}
		/*try{
		myLocationListner.stopLocationListner();
		}catch (Exception e) {
			
		}*/
	}
	
	/*public void onBackPressed() {
		System.out.println(" on back Main activity");
		finish();
		//System.exit(0);
	};*/

	TimerTask  timerTask = new TimerTask() {
		@Override
		public void run() {
			handler.sendEmptyMessage(1);
		}
	};
	
	/*@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		System.exit(0);
	}*/
	
	protected boolean operations() {
		boolean result= false;
		try{
			List<Address> addresses = getAddressFromLocation();
			Address add = addresses.get(0);
			//System.out.println(" address size : "+addresses.size());
			mAddress = getDetailedAddress(add);
			result = true;
			//System.out.println(" getting address "+addresses.size());
			//mAddress = myLocationListner.getBestAddress(addresses, mAddress);	
			
			//System.out.println(" address : "+mAddress);
		}
		catch (Exception e) {
			result = false;
			mAddress ="Please Try Later";
			if (ioe) {
				Toast.makeText(mContext, "Network is unavailable or any other I/O problem occured", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(mContext, "Something Went Wrong.", Toast.LENGTH_SHORT).show();
			}
			runOnUiThread(new Runnable() {
				public void run() {
					mCurrentLocation.setText(mAddress);
					mButEnterLoc.setVisibility(View.GONE);
				}
			});
		}
		return result;
	}

	protected void aboutOusDialog() {
		WebView webviewAboutUS=new WebView(mContext);
		webviewAboutUS.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		webviewAboutUS.loadUrl("file:///android_asset/AboutUs.html");
		webviewAboutUS.getSettings().setBuiltInZoomControls(false);
		webviewAboutUS.setHorizontalScrollBarEnabled(false);
		webviewAboutUS.setVerticalScrollBarEnabled(false);
		webviewAboutUS.getSettings().setJavaScriptEnabled(true);

		new AlertDialog.Builder(MainActivity.this)  
		.setTitle("About Us")
		.setView(webviewAboutUS)
		.show();
		
	}

	private void initializeUI(){
		mCurrentLocation = (TextView)findViewById(R.id.current_location);
		mButEnterLoc = (Button)findViewById(R.id.button_enter_location);
		mButInfo = (Button)findViewById(R.id.button_info);
		mCurrentLocation.setText(mAddress);
		mButEnterLoc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, MapViewActivity.class);
				intent.putExtra("address", mAddress);
				intent.putExtra("lat", mSrtLat);
				intent.putExtra("lng", mSrtLng);
				startActivity(intent);
				
				/*intent.putExtra("address", "Road no 2 banjara hills");
				intent.putExtra("lat", 17.4243245);
				intent.putExtra("lng", 78.432474);
				startActivity(intent);*/
				
			}
		});
		
		mButInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				aboutOusDialog();
			}
		});
		
	}


	// i ve locked the activity not to restart it when orientation changes.
	// but i am forcing it change the UI since it has changes in port and land mode. 
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// changing the xml layout as configuration changes,so i am re asigning. 
		super.onConfigurationChanged(newConfig);
		setContentView(R.layout.main_activity_layout);
		initializeUI();
	}

	protected boolean getGeoPointsForDestination(String destInput) {
		boolean flag=false;
		//i am trying to get the geo point from the string
		// i am getting only 3 result from method getFromLocatinName
		// finally i ll return true if everthing works fine or false saying destination location not found.
		try {
			Geocoder geocoder = new Geocoder(mContext);
			List<Address> addresses = geocoder.getFromLocationName(destInput, 3);
			if (addresses.size() > 0) {
				Address location = addresses.get(0);
				userAddress = getDetailedAddress(location);
				if (userAddress != null) {
					userLat = location.getLatitude();
					userLng = location.getLongitude();
					
					//System.out.println(" lat and lng : "+userLat + " ----- "+userLng);
					
					flag = true;
				}
			}else{
				return flag;
			}

		} catch (IOException e) {
			return flag;
		}
		return flag;
	}

	
	public List<Address> getAddressFromLocation() {
		Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
		List<Address> addresses = null;
		try {
			addresses = gcd.getFromLocation(mSrtLat, mSrtLng, 3);
		} catch (IOException e) {
			//might be not connected to net
			System.err.println("IOException in Mainactivity  "+e);
			addresses = null;
			ioe = true;
		}
		return addresses;
	}
	
	public String getDetailedAddress(Address add) {
		StringBuffer temp = new StringBuffer();
		//System.out.println("address max indexes :"+add.getMaxAddressLineIndex());
		if (add.getAddressLine(0) != null) {
			temp.append(add.getAddressLine(0)).append("\n");
		} else if (add.getLocality() != null) {
			temp.append(add.getLocality()).append("\n");
		}else if (add.getPostalCode() != null) {
			temp.append(add.getPostalCode()).append("\n");
		}else if (add.getCountryName() != null) {
			temp.append(add.getCountryName());
		}
		return temp.toString();
	}

/*	protected List<String> checkProviders() {
		// TO check Number of provider ready to give gps location.
		List<String> providers = mLocationManager.getProviders(true);
		for (String provider : providers) {
			//System.out.println(" provider name :"+provider);
			if (provider.equalsIgnoreCase("network")) {
				mNetwork=true;
			}else if (provider.equalsIgnoreCase("passive")) {
				mPassive=true;
			}else if (provider.equalsIgnoreCase("gps")) {
				mGps=true;
			}
		}
		return providers;
	}*/

	protected void settingGPS() {
		// i am requestin user to set gps from setting 
		// giving positive and negative button.
		final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Gps setting");
		builder.setMessage("Your GPS Setting In Device is OFF. Please ON it for accurate Location Positions");

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// calling setting activity for GPS. i am calling activityforresult
				
				startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
			}
		});

		builder.setNegativeButton("No Thanks", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// User is not whilling to set GPS in Device
				Toast.makeText(mContext, "Sorry we cant locate Current Position with out GPS Setting ON", Toast.LENGTH_SHORT).show();
				alertDialogwithEditText();
			}
		});


		AlertDialog alert = builder.create();
		alert.show();
	}

/*	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//i dont need response of the request code 0 becoze i am calling system setting
		if (requestCode == 0) {
			providers.clear();
			providers = checkProviders();// i am calling again to set my boolean value to true.
		}
		if (newThread != null) {
			newThread.start();
		}

	}*/


	private void alertDialogwithEditText(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Enter Current Location");
		//alert.setMessage("Message");

		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);
		showSoftKeyBoard(input, inputMethodManager);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				mAddress= input.getText().toString();
				// Do something with value!

				customProgressDialog.loading("Fetching User Address", "From Google Maps...", false);
				closeSoftKeyBoard(input, inputMethodManager);
				runOnUiThread(new Runnable() {
					public void run() {
						mCurrentLocation.setText(mAddress);
					}
				});

				boolean f = getGeoPointsForDestination(mAddress);
				if (f) {
					mSrtLat = userLat;
					mSrtLng = userLng;
				}else{
					customProgressDialog.dismiss();
					Toast.makeText(mContext, "Not able to Fetch User Address", Toast.LENGTH_SHORT).show();
				}
				customProgressDialog.dismiss();
			}
		});
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
				// lets finish the app.
				closeSoftKeyBoard(input, inputMethodManager);
				finish();
			}
		});

		alert.show();
	}

	private void closeSoftKeyBoard(EditText searchEditButton,InputMethodManager inputMethodManager) {
		// Forcing to CLOSE the soft key board
		inputMethodManager.hideSoftInputFromInputMethod(searchEditButton.getWindowToken(), 0);
	}

	private void showSoftKeyBoard(EditText searchEditButton, InputMethodManager inputMethodManager) {
		// Forcing to OPEN the soft key board
		inputMethodManager.showSoftInput(searchEditButton, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/*private void dialogDisplay(String title, String messagedata){
		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog);
		final TextView heading =(TextView)dialog.findViewById(R.id.cd_title);
		TextView message = (TextView)dialog.findViewById(R.id.cd_message);
		RelativeLayout buttonLayout = (RelativeLayout)dialog.findViewById(R.id.cd_button_layout);
		LayoutInflater factory = LayoutInflater.from(mContext);
		View view = factory.inflate(R.layout.one_button, null);
		buttonLayout.addView(view);
		final Button button = (Button)view.findViewById(R.id.button_positive_only);
		heading.setText(title);
		message.setText(messagedata);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}*/

/*	protected void callingNetwork(){
		myLocationFlag = myLocationListner.getLocationFromProvider(providers.get(providers.indexOf("network")));
		//System.out.println(" network status :" +myLocationFlag);
		if (myLocationFlag) {
			mSrtLat=myLocationListner.getlat();
			mSrtLng=myLocationListner.getlng();
		}
	}*/

/*	protected void callingPassive(){
		myLocationFlag = myLocationListner.getLocationFromProvider(providers.get(providers.indexOf("passive")));
		if (myLocationFlag) {
			mSrtLat=myLocationListner.getlat();
			mSrtLng=myLocationListner.getlng();
			
		}
	}*/

	/*protected void callingGPS(){
		myLocationFlag = myLocationListner.getLocationFromProvider(providers.get(providers.indexOf("gps")));
		if (myLocationFlag) {
			mSrtLat=myLocationListner.getlat();
			mSrtLng=myLocationListner.getlng();
		}
	}*/


	@Override
	public void onLocationChanged(Location location) {
		
		if (location != null) {
			timer.cancel();
			//System.out.println(" provider : "+location.getProvider());
			mSrtLat = location.getLatitude();
			mSrtLng = location.getLongitude();
			
			//System.out.println(" lat : "+mSrtLat);
			//System.out.println(" lng : "+mSrtLng);
			handler.sendEmptyMessage(0);
		}
		
	}


	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	/*private boolean HaveNetworkConnection()
	{
	    boolean HaveConnectedWifi = false;
	    boolean HaveConnectedMobile = false;

	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo)
	    {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	            if (ni.isConnected())
	                HaveConnectedWifi = true;
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	            if (ni.isConnected())
	                HaveConnectedMobile = true;
	    }
	    return HaveConnectedWifi || HaveConnectedMobile;
	}*/

}