package com.paradigmcreatives.activity;

import java.io.InputStream;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.paradigmcreatives.crashreport.ParadigmExceptionHandler;
import com.paradigmcreatives.database.Config;
import com.paradigmcreatives.dialog.CustomProgressDialog;
import com.paradigmcreatives.drivingDirection.MyDrivingDirection;
import com.paradigmcreatives.globalconfig.GlobalConfiguration;
import com.paradigmcreatives.listner.MyLocationListner;
import com.paradigmcreatives.mapoverlay.CurrentLocOverlay;
import com.paradigmcreatives.mapoverlay.PinsOverlay;
import com.paradigmcreatives.mapoverlay.RouteOverlay;
import com.paradigmcreatives.util.Utility;

public class MapViewActivity extends MapActivity {

	private double mSrtLat;
	private double mSrtLng;
	private double mEndLat;
	private double mEndLng;
	private Context mContext;
	private String mStartAddress;
	private String endAddress;
	private MapView myMapView;
	private MyDrivingDirection mDirection;
	private MyLocationListner myLocationListner;
	private Handler mHandler;
	private CustomProgressDialog customProgressDialog;
	private String destInput;
	private int userAlertTime=0;
	private Button mDoneButton;
	private int durationTime;
	MapController mapController;
	
	TextView zButton;
	TextView zButton1;
	GeoPoint currentGeoPoints;
	public static String alarmTone = "";
	public static ArrayList<GeoPoint> mFinalGeopoints;
	public static ArrayList<String> mTempData;
	public static String distanceInKm;
	public static String durationInMinutes;
	private String statusCode;
	private int minLatitude;
	private int maxLatitude;
	private int minLongitude;
	private int maxLongitude;
	//private boolean flag=false;
    private TextView mAddress;
    private static TextView mDistance;
    MyLocationOverlay myLocationOverlay = null;
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
            
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
            
        }
    }
	@Override
	protected void onCreate(Bundle onsavedBunble) {

		super.onCreate(onsavedBunble);
		mContext = MapViewActivity.this;
		//myLocationListner = new MyLocationListner(mContext);
		PendingIntent intent = PendingIntent.getActivity(getApplication().getBaseContext(), 0, new Intent(getIntent()), getIntent().getFlags());
		 Thread.setDefaultUncaughtExceptionHandler(new ParadigmExceptionHandler(this, intent));
		
		 System.out.println("onCreate");
		Intent mainActivityIntent = getIntent();
		mStartAddress = mainActivityIntent.getStringExtra("address");
		mSrtLat = mainActivityIntent.getDoubleExtra("lat", 0);
		mSrtLng = mainActivityIntent.getDoubleExtra("lng", 0);

		 currentGeoPoints = new GeoPoint((int)(mSrtLat * 1E6),(int)(mSrtLng * 1E6));

		setContentView(R.layout.mapview_activity_layout);

		myMapView = (MapView)findViewById(R.id.mapviewfinal);
		mAddress = (TextView)findViewById(R.id.address);
		mDistance = (TextView)findViewById(R.id.distance);
		customProgressDialog = new CustomProgressDialog(mContext);

		customProgressDialog.loading("Loading Map...", "Loading Map from Google server", true);
		
	//	displayCurrentLocationOnMap(currentGeoPoints);

		mDoneButton = (Button)findViewById(R.id.done_button);
		Button searchButton = (Button)findViewById(R.id.search_button);
		final EditText searchEditTextBox = (EditText)findViewById(R.id.search_edit);


		// making done visible gone and make it on when the user got the valid destionation route
		// check for on in the search()
		mDoneButton.setVisibility(View.GONE);

		final InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		showSoftKeyBoard(searchEditTextBox, inputMethodManager);

		searchEditTextBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) { 
				if (actionId != EditorInfo.IME_ACTION_NEXT && actionId != EditorInfo.IME_NULL) {
					String destInput = searchEditTextBox.getText().toString();
					if (destInput.equals("")) {
						mDoneButton.setVisibility(View.GONE);
						Toast.makeText(mContext, "Provide Valid Input In Search Box...", Toast.LENGTH_SHORT).show();
					}else{
						closeSoftKeyBoard(searchEditTextBox, inputMethodManager);
						//customProgressDialog.loading("Fetching Maps...", "Loading Driving Direction...", true);
						search(destInput);
					}
					return false;
				}
				return true;
			}
		});

		final String[] items = new String[60];
		int temp = 1;
		
		for (int i = 0; i < 60; i++) {
			items[i] = Integer.toString(temp) +" " +" Mins";
			temp = temp +1;
		}

		mDoneButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//if (doneButtonFlag) {
				//myLocationListner.stopLocationListner();
				final Dialog dialog = new Dialog(mContext);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.custom_dialog_with_list);
				final TextView heading =(TextView)dialog.findViewById(R.id.cdl_title);
				ListView listView = (ListView)dialog.findViewById(R.id.cdl_listview);
				final Button button = (Button)dialog.findViewById(R.id.cdl_positive);
				heading.setText(" Alert Me Before : ");
				listView.setAdapter(new ArrayAdapter<String>(mContext,R.layout.list_with_radio_button, items));
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int position,
							long arg3) {
						userAlertTime = position+1;
						heading.setText(" Alert Me Before : "+userAlertTime+ " Mins");
					}
				});

				button.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						// sending to tab activity
						durationTime = mDirection.getDuration();
						durationTime = durationTime/60;
						//System.out.println("duration before :: " + durationTime);
						if (userAlertTime == 0) {
							// inform user to select atleast one time
							//dialog.dismiss();// wantedly i ve stoped so that user will know the mistake on the screen.
							// below method creates a dialog.
							dialogDisplay(" Alert !!! ","Please select time and proceed");
						}else
						{
							if (userAlertTime <= durationTime) {
					            Intent intent_pickring = new Intent( RingtoneManager.ACTION_RINGTONE_PICKER);
					            intent_pickring.putExtra( RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_NOTIFICATION);
					            intent_pickring.putExtra( RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
					            intent_pickring.putExtra( RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,(Uri)null);
					            dialog.dismiss();
					            startActivityForResult(intent_pickring, 111);
							}else{
								//dialog.dismiss(); //wantedly stoped to show user excatly the error
								dialogDisplay(" Alert !!! ","Your Alert Time should be less than or equal to duration time. Your duration time is : "+durationTime +" Mins");
							}
						} 
					}
				});

				dialog.show();
			}
		});

		searchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				closeSoftKeyBoard(searchEditTextBox,inputMethodManager);
			 destInput = searchEditTextBox.getText().toString();
				if (destInput.equals("")) {
					mDoneButton.setVisibility(View.GONE);
					Toast.makeText(mContext, "Provide Valid Input In Search Box...", Toast.LENGTH_SHORT).show();
				}else{
					//doingMapOperation(startAddress,destInput);
					// let me try and bring out the more accurate like getting geo points from the destInput
					// i am getting the geo points from the destination string provided by user
					// i am setting the endlat and endlng  in method getGeoPointsForDestination.
					// i ve placed all the code in the method search, so that when orientation is changed 
					// i ll simply call this search method iff i want to restart activity only
					closeSoftKeyBoard(searchEditTextBox, inputMethodManager);
					//customProgressDialog.loading("Fetching Maps...", "Loading Driving Direction...", true);
					search(destInput);
					//customProgressDialog.dismiss();
				}
			}
		});

		mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == 0) {
					Bundle b = msg.getData();
		    		mDistance.setText(" "+b.getString("distance"));
				RouteOverlay overlay = new RouteOverlay(mFinalGeopoints, mContext, myMapView);
				
				//phaniGeoPoint srtLocationGeoPoints = new GeoPoint((int)(mSrtLat * 1e6), (int)(mSrtLng * 1e6));
				//phaniGeoPoint endLocationGeoPoints = new GeoPoint((int)(mEndLat * 1e6), (int)(mEndLng * 1e6));
				
				//phaniPinsOverlay pinsOverlay = new PinsOverlay(mContext, srtLocationGeoPoints, endLocationGeoPoints);
				
				GeoPoint gPoint =mFinalGeopoints.get(0);
				int moveToLat = gPoint.getLatitudeE6();
				int moveToLng = gPoint.getLongitudeE6();
				GeoPoint moveTo = new GeoPoint(moveToLat, moveToLng);
				MapController mapController = myMapView.getController();
				//i am trying a logic that according to srtlat,srtlng and endlat,endlng 
				// try to set the zoom level so that USER can see srt and end pin on the screen.
				//phanisetZoomOption(mapController);
				setZoomOption(mapController, moveTo);
				List<Overlay> listOfOverlays = myMapView.getOverlays();
				listOfOverlays.clear();
				listOfOverlays.add(overlay);
				//phanilistOfOverlays.add(pinsOverlay);
				
				myMapView.invalidate();
				customProgressDialog.dismiss();
				mDoneButton.setVisibility(View.VISIBLE);
				}else if (msg.what == 1) {
					//System.out.println(" status code : "+statusCode);
					customProgressDialog.dismiss();
					if (GlobalConfiguration.OVER_QUERY_LIMIT.equals(statusCode)) {
						Toast.makeText(mContext, "Query Over Limit From Google", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(mContext, "No Driving Direction. Try Another Destination", Toast.LENGTH_LONG).show();
					}
				}
				
			}
		};

		customProgressDialog.dismiss();
		 zButton =(TextView)findViewById(R.id.zoomButton1);
		 zButton1 =(TextView)findViewById(R.id.zoomButton2);
		
		zButton.setOnClickListener(zoomInButton);
		zButton1.setOnClickListener(zoomOutButton);
		/*zButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mapController = myMapView.getController();
				mapController.zoomIn();
				//Toast.makeText(MapViewActivity.this, "Zoomin", Toast.LENGTH_SHORT).show();
				
			}
		});
zButton1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mapController = myMapView.getController();
				mapController.zoomOut();
				//Toast.makeText(MapViewActivity.this, "ZoomOUT", Toast.LENGTH_SHORT).show();
				
			}
		});*/
	}
	//zButton.onClickListener(zoomInButton)=new onClickListener(){}
	private View.OnClickListener zoomInButton = new View.OnClickListener() {

	      @Override

	      public void onClick(View v) {
	    	  mapController = myMapView.getController();
				mapController.zoomIn();
	        

	      }

	  };
		
	  private View.OnClickListener zoomOutButton = new View.OnClickListener() {

	      @Override

	      public void onClick(View v) {
           
	    	  mapController = myMapView.getController();
				mapController.zoomOut();
	        

	      }

	  };
	
	protected void onRestart(){
		System.out.println("onRestart");
	}
	
	
   
	public boolean onCreateOptionsMenu(Menu menu){
	    super.onCreateOptionsMenu(menu);
	    MenuInflater oMenu = getMenuInflater();
	    oMenu.inflate(R.menu.submenu, menu);
	    return true;
	}
	public boolean onOptionsItemSelected(MenuItem item){
		 if (item.getItemId()==R.id.my_location) {
		    	//myMapView.setStreetView(true);
		    	displayCurrentLocationOnMap(currentGeoPoints);
		         return true;
		   
		    }
		if (item.getItemId()==R.id.satelite) {
	    	
	    	 myMapView.setSatellite(true);
	         myMapView.setStreetView(false);
	         myMapView.invalidate();
	         return true;
		}
	    if (item.getItemId()==R.id.streetView) {
	    	//myMapView.setStreetView(true);
	         myMapView.setSatellite(false);
	         myMapView.invalidate();
	         return true;
	   
	    }
	   
	    if (item.getItemId()==R.id.traffic) {
	    	boolean raji=myMapView.isTraffic();
	    		System.out.println("Traffiic"+raji);
	    	if(raji){
	    	myMapView.setTraffic(true);
	    	}else { 
	    		Toast.makeText(this,"No Traffic found here ", Toast.LENGTH_LONG).show();
	    	}	
	    	myMapView.setStreetView(false);
	         myMapView.setSatellite(false);
	         myMapView.invalidate();
	         return true;
	   
	    }
	    return false;
	}
	/*private void displayCurrentLocationOnMap(GeoPoint currentGeoPoints) {
	//just testing with new UI 
		// i am displaying current location of the user by showing pin on map
		CurrentLocOverlay currentLocOverlay = new CurrentLocOverlay(mContext, currentGeoPoints);
		MapController mapController = myMapView.getController();
		mapController.animateTo(currentGeoPoints);
		byte i =zoomLevel(0.5);
		myLocationOverlay = new MyLocationOverlay(mContext, myMapView);
		myLocationOverlay.enableMyLocation();
		mapController.setZoom(i);
		//mapController.setCenter(currentGeoPoints);
		List<Overlay> listOfOverlays = myMapView.getOverlays();
		listOfOverlays.clear();
		listOfOverlays.add(currentLocOverlay);
		
	}*/
	
	
	private void displayCurrentLocationOnMap(GeoPoint currentGeoPoints) {
		// i am displaying current location of the user by showing pin on map
		MyLocationOverlay myLocOverlay = new MyLocationOverlay(this, myMapView);
	Boolean loc =	myLocOverlay.enableMyLocation();
	System.out.println("loc"+loc);
	Boolean comp =	myLocOverlay.enableCompass();
	System.out.println("comp"+comp);
		MapController mapController = myMapView.getController();
		myMapView.getOverlays().add(myLocOverlay);
		mapController.setCenter(currentGeoPoints);
		myMapView.invalidate();
		}
	

	@Override
	public void onBackPressed() {
		// Moving back to first activity. and clearing all the stack history.
		super.onBackPressed();
		Intent intent = new Intent(mContext, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
	
	/*@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		System.exit(0);
	}*/
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		//System.out.println(" onActivityResult " + resultCode);
		
		if (resultCode == RESULT_OK) {
			//System.out.println("onactivity result");
            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (uri != null) {
                alarmTone = uri.toString();
            }
            
          // System.out.println( "rajesh inserte in db at map view activity");
            
            Intent intent = new Intent(mContext, TabViewActivity.class);
            //System.out.println( "rajesh1 inserte in db at map view activity");
			intent.putExtra("srtLat", mSrtLat);
			intent.putExtra("srtlng", mSrtLng);
			intent.putExtra("endlat", mEndLat);
			intent.putExtra("endlng", mEndLng);
			intent.putExtra("alerttime", userAlertTime);
			intent.putExtra("durationtime", Integer.toString(durationTime));
			intent.putExtra("srtaddress",mStartAddress);
			endAddress = mLocation;
			intent.putExtra("endaddress",endAddress);
			
			intent.putExtra("distanceInMeters", mDirection.getdistance());
			
			intent.putExtra("insertRow", true);
			 //System.out.println( "rajesh4 inserte in db at map view activity");
			Config.Instance().setValuesToMaxMinLocation(minLatitude, maxLatitude, minLongitude, maxLongitude);
			
			Config.Instance().setValuesToCurrentLoc(mSrtLat, mSrtLng, mEndLat, mEndLng, mStartAddress, endAddress, userAlertTime, 18.00, mDirection.getdistanceInKm(), mDirection.getDurationInMinutes());
     		// System.out.println( "rajesh3 inserte in db at map view activity");
     		 finish();
			startActivity(intent);
			
		}
		else{
			System.out.println(" NO RING TONE FOR EMULATOR");
		}
	}

	//phaniprotected void setZoomOption(MapController mapController) {
	protected void setZoomOption(MapController mapController, GeoPoint moveTo) {
		// try to show srt and end points on the screen by setting zoom controls and by animate method

		 minLatitude = Integer.MAX_VALUE;
		 maxLatitude = Integer.MIN_VALUE;
		 minLongitude = Integer.MAX_VALUE;
		 maxLongitude = Integer.MIN_VALUE;
		 
		 ArrayList<GeoPoint> gp = new ArrayList<GeoPoint>();
		 
		 gp.add(new GeoPoint((int)(mSrtLat * 1e6), (int)(mSrtLng * 1e6)));
		 gp.add(new GeoPoint((int)(mEndLat * 1e6), (int)(mEndLng * 1e6)));
		 
		//phanifor (GeoPoint item : gp) { //item Contain list of Geopints
		 for (GeoPoint item : mFinalGeopoints) {
			int lat = item.getLatitudeE6();
			int lon = item.getLongitudeE6();
			maxLatitude = Math.max(lat, maxLatitude);
			minLatitude = Math.min(lat, minLatitude);
			maxLongitude = Math.max(lon, maxLongitude);
			minLongitude = Math.min(lon, minLongitude);
		}
				
		mapController.animateTo(new GeoPoint((maxLatitude + minLatitude)/2,(maxLongitude + minLongitude)/2 )); 
		mapController.zoomToSpan(Math.abs(maxLatitude - minLatitude), Math.abs(maxLongitude - minLongitude));
	}

	/*protected void search(String destInput) {
		// if the below method is true then make done button visible.
		
		if(getGeoPointsForDestination(destInput)){
	//	if(true){
			//doingMapOperation(mSrtLat, mSrtLng, mEndLat, mEndLng);
			
			//mEndLat= 17.4958999;
			//mEndLng = 78.514259;
			doingMapOperation(mSrtLat, mSrtLng, mEndLat, mEndLng);
		
			//doneButtonFlag = true;
		}else{
			customProgressDialog.dismiss();
			//doneButtonFlag = false;
			Toast.makeText(mContext, "Destination Location Not Found or Some Server Problem", Toast.LENGTH_LONG).show();
		}
	}*/

	// Forcing to CLOSE the soft key board
	private void closeSoftKeyBoard(EditText searchEditButton,InputMethodManager inputMethodManager) {
		inputMethodManager.hideSoftInputFromWindow(searchEditButton.getApplicationWindowToken(), 0);
		//inputMethodManager.hideSoftInputFromInputMethod(searchEditButton.getWindowToken(), 0);
	}

	// Forcing to OPEN the soft key board
	private void showSoftKeyBoard(EditText searchEditButton, InputMethodManager inputMethodManager) {
		inputMethodManager.showSoftInput(searchEditButton, InputMethodManager.HIDE_NOT_ALWAYS);
	}


	protected void doingMapOperation(final double srtLat2, final double srtLng2,
			final double endLat2, final double endlng2) {
		new Thread(){
			

			public void run(){
				mDirection = new MyDrivingDirection();
				String url = mDirection.buildURLByLocation(srtLat2, srtLng2, endLat2, endlng2);
				InputStream inputStream = mDirection.getConnection(url);
				StringBuilder stringInputStream = mDirection.convertStreamToString(inputStream);
				
				boolean canDrawDrivingDirection = mDirection.getJsonCheckStausCode(stringInputStream.toString());
				//System.out.println(" boolean value : "+canDrawDrivingDirection);
				if (canDrawDrivingDirection) {
				mDirection.jsonDataparsing(stringInputStream.toString());
				mDirection.jsonGeoPointsParsing(stringInputStream.toString());
				mFinalGeopoints = new ArrayList<GeoPoint>();
				mFinalGeopoints = mDirection.getgeoPoints();
				distanceInKm = mDirection.getdistanceInKm();
				durationInMinutes = mDirection.getDurationInMinutes();
				mTempData = new ArrayList<String>();
				mTempData = mDirection.getTempData();
				Bundle b =  new Bundle();
				Message msg = new Message();
				String time_distance =durationInMinutes +" - "+distanceInKm;
				b.putString("distance",time_distance);
				msg.setData(b);
				msg.what =0;
				mHandler.sendMessage(msg);
				
				}else{
					statusCode = mDirection.getStatusCode();
					mHandler.sendEmptyMessage(1);
				}
			}
		}.start();
	}

	protected void search(String destInput) {
		
		//String optionalData[] = null;
		final ArrayList< String> optionalData = new ArrayList<String>();
		//i am trying to get the geo point from the string
		// i am getting only 3 result from method getFromLocatinName
		// finally i ll return true if everthing works fine or false saying destination location not found.
		try {
			Geocoder geocoder = new Geocoder(mContext);
			final List<Address> location = geocoder.getFromLocationName(destInput, 3);
			//System.out.println(" address list size : "+addresses.size());
			if (location.size() >= 0) {
				for (int i = 0; i < location.size(); i++) {
					System.out.println("location "+location);
					if (location.get(i) != null) {
						optionalData.add(getDetailedAddress(location.get(i)));
					}
				}
			}else{
				//customProgressDialog.dismiss();
				//doneButtonFlag = false;
				Toast.makeText(mContext, "Destination Location Not Found.", Toast.LENGTH_SHORT).show();
			}
			
			//System.out.println(" list size : "+location.size());
			//System.out.println(" my array size : "+optionalData.size());
			
			if (optionalData.size() > 0) {
				final Dialog dialog = new Dialog(mContext);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.custom_sugguestion);
				final TextView heading =(TextView)dialog.findViewById(R.id.cd_title);
				
				heading.setText("Route Suggestions");
				
				ListView sugguestionListView = (ListView)dialog.findViewById(R.id.list_sugguesttion);
				ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1,optionalData);
				sugguestionListView.setAdapter(myAdapter);
				sugguestionListView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						mDistance.setVisibility(View.VISIBLE);
						mAddress.setVisibility(View.VISIBLE);
						Address loc = location.get(position);
						mEndLat = loc.getLatitude();
						mEndLng = loc.getLongitude();
						//flag = true;
						mAddress.setText(" "+optionalData.get(position));
					//	endAddress ="Lothkuntha";  //phani
						/*new Thread(){
							public void run() {
								double FixedFinalDistance = Utility.distance(mSrtLat, mSrtLng, mEndLat, mEndLng);
								Bundle b = new Bundle();
								b.putString("distance", FixedFinalDistance+"");
								Message msg = new Message();
								msg.setData(b);
						        MapViewActivity.mHandler1.sendMessage(msg);
							};
						}.start();*/
						dialog.dismiss();
						customProgressDialog.loading("Fetching Maps...", "Loading Driving Direction...", true);
						doingMapOperation(mSrtLat, mSrtLng, mEndLat, mEndLng);
					}
				});		
				dialog.show();
			}else{
				Address loc = location.get(0);
				mEndLat = loc.getLatitude();
				mEndLng = loc.getLongitude();
				customProgressDialog.loading("Fetching Maps...", "Loading Driving Direction...", true);
				doingMapOperation(mSrtLat, mSrtLng, mEndLat, mEndLng);
			}
			
		/*	if (addresses.size() > 0) {
				Address location = addresses.get(0);
				endAddress = getDetailedAddress(location);
				if (endAddress != null) {
					mEndLat = location.getLatitude();
					mEndLng = location.getLongitude();
					flag = true;
				}
			}else{
				return flag;
			}*/
		} catch (Exception e) {
			//return flag;
			//customProgressDialog.dismiss();
			//doneButtonFlag = false;
			e.printStackTrace();
			Toast.makeText(mContext, "Network is unavailable or any other I/O problem occured", Toast.LENGTH_SHORT).show();
		}
		//return flag;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return true;
	}

public String mLocation;
	public String getDetailedAddress(Address add) {
		StringBuffer temp = new StringBuffer();
		//System.out.println("address max indexes :"+add.getMaxAddressLineIndex());
		if (add.getAddressLine(0) != null) {
			mLocation = add.getAddressLine(0);
			}
		for(int i=0;i<add.getMaxAddressLineIndex();i++){
			temp.append(add.getAddressLine(i)).append(",");
		}
		/*if (add.getLocality() != null) {
			System.out.println(add.getLocality());
			temp.append(add.getLocality()).append(",");
		}*/
		if (add.getPostalCode() != null) {
		//	System.out.println(add.getPostalCode());
			temp.append(add.getPostalCode()).append(",");
		}
       if (add.getCountryName() != null) {
		//	System.out.println(add.getCountryName());
			temp.append(add.getCountryName());
		}
		//System.out.println(" AREA : "+temp.toString());
		return temp.toString();
	}

	private byte zoomLevel (double distance){
		/* we have zoom level from 1 to 21 so i am programmatically finding zoom level 
		 parameter is i am giving how much distance(kms) should be shown on screen.
		 generally  At zoomLevel 1, the equator of the earth is 256 pixels long
	     Each successive zoom level is magnified by a factor of 2.
		 so i am trying to Returns: the new zoom level, between 1 and 21 inclusive. */
		byte zoom=1;
		double E = 40075;
		zoom = (byte) Math.round(Math.log(E/distance)/Math.log(2)+1);
		// these condition is only to get out of the exception.
		if (zoom>21) zoom=21;
		if (zoom<1) zoom =1;
		return zoom;
	}

	private void dialogDisplay(String title, String messagedata){
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
	}


}
