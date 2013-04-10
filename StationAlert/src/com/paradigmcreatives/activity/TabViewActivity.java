package com.paradigmcreatives.activity;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.style.BulletSpan;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
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
import com.paradigmcreatives.mapoverlay.CircleOverlay;
import com.paradigmcreatives.mapoverlay.PinsOverlay;
import com.paradigmcreatives.mapoverlay.RouteOverlay;
import com.paradigmcreatives.service.AlertService;

public class TabViewActivity extends MapActivity implements OnTabChangeListener {

	private static final String TRACK_TAB_TAG = "Track Info";
	private static final String MAP_TAB_TAG = "Map";
	private static final String SETTING_TAB_TAG = "Setting";
	private TabHost mTabHost;
	private MapView mMapView;
	private double mSrtLat;
	private double mSrtLng;
	private double mEndLat;
	private double mEndLng;
	private String mStartAddress;
	private String mEndAddress;
	private Context mContext;
	private Handler mHandler;
	MapController mapController;
	private MyDrivingDirection mDirection;
	private ArrayList<GeoPoint> mFinalGeopoints;
	private Map<Integer, Map<String, String>> mDirivingData;
	private LinearLayout trackMainLayout;
	private LinearLayout settingMainLayout;
	private int userAlertTime;
	private CustomProgressDialog mCustomProgressDialog;
	private String durationTime;
	//private int distance;
	private boolean dbStatus;
 public static CurrentLocationNavigation mCurrentLocationNavigation;
	MyLocationOverlay myLocationOverlay = null;
	RouteOverlay overlay ;
	private ArrayList<String> mTempData;
	GeoPoint moveTo;
	//public static int checkedNumber = 0;
	private ListView listView;
	private int latestAlertTime;
	//private CurrentLocationListner mCurrentLocationListner;
	//private LocationManager mLocationManager;
	public static Handler UiHandler;
	public static boolean UiFlag;
	public static boolean radiusFlag;
	GeoPoint currentGeoPoints;

	private int distanceInMeters;
	private List<Overlay> listOfOverlays;
	private PinsOverlay pinsOverlay;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		mContext = TabViewActivity.this;
		setContentView(R.layout.tab_view);


		//System.out.println(" On Create Tab activity");
		PendingIntent intent = PendingIntent.getActivity(getApplication().getBaseContext(), 0, new Intent(getIntent()), getIntent().getFlags());
		//Thread.setDefaultUncaughtExceptionHandler(new ParadigmExceptionHandler(this, intent));

		UiFlag = true;

		Intent lastIntent = getIntent();
		if (intent != null) {
			// New User and we are registering him to database.
			dbStatus = lastIntent.getBooleanExtra("dbStatus", false);
			// dbStatus true means already user registered with one location and get details from DB(no need to register him again).
			// these intent coming from the first MainActivity.
			if (dbStatus) {
				Cursor cursor = Config.Instance().getCurrentStatusTable();
				cursor.moveToFirst();
				do {
					mSrtLat = cursor.getDouble(cursor.getColumnIndex("srtlat"));
					mSrtLng = cursor.getDouble(cursor.getColumnIndex("srtlng"));
					mEndLat = cursor.getDouble(cursor.getColumnIndex("endlat"));
					mEndLng = cursor.getDouble(cursor.getColumnIndex("endlng"));
				} while (cursor.moveToNext());

			} else {
				// dbStatus False means not registered need to add first to DB and register USer after the Worker Thread is completed(since we get data from it)
				// and these intent coming from MapViewActivity.
				mSrtLat = lastIntent.getDoubleExtra("srtLat", 0);
				mSrtLng = lastIntent.getDoubleExtra("srtlng", 0);
				mEndLat = lastIntent.getDoubleExtra("endlat", 0);
				mEndLng = lastIntent.getDoubleExtra("endlng", 0);
				userAlertTime = lastIntent.getIntExtra("alerttime", 0);
				durationTime = lastIntent.getStringExtra("durationtime");
				mStartAddress = lastIntent.getStringExtra("srtaddress");
				mEndAddress = lastIntent.getStringExtra("endaddress");
				distanceInMeters = lastIntent.getIntExtra("distanceInMeters", 0);
				Config.Instance().setStatusValue("true");
			}

		} else {
			// we ll check when these condition will be satisfied.
			// old user already registered we getting values from dbase.

		}

		//mCurrentLocationListner = new CurrentLocationListner();


		mTabHost = (TabHost) findViewById(android.R.id.tabhost);


		currentGeoPoints = new GeoPoint((int)(mSrtLat * 1E6),(int)(mSrtLng * 1E6));
         System.out.println("current points"+currentGeoPoints);
		Resources resources = getResources();
		// setup must be called if you are not inflating the tabhost from XML
		mTabHost.setup();

		trackMainLayout = (LinearLayout) findViewById(R.id.track_main);
		mMapView = (MapView) findViewById(R.id.final_mapview);
		settingMainLayout = (LinearLayout) findViewById(R.id.time_main);

		mCustomProgressDialog = new CustomProgressDialog(mContext);


		mCustomProgressDialog.loading("Loading Map...","Displaying current location", true);

		// i am stoping to implement the Next Way of WEB SERVICE OPERATION
		//doingMapOperation(mSrtLat, mSrtLng, mEndLat, mEndLng);

		mapOperation();

		//System.out.println(" adding tabs ");
		mTabHost.addTab(mTabHost.newTabSpec(TRACK_TAB_TAG).setIndicator(TRACK_TAB_TAG,resources.getDrawable(R.drawable.ic_tab_menu_info))
				.setContent(new TabContentFactory() {
					public View createTabContent(String tag) {
						// i am creating the view for these tab.
						View view = buildTrackInfoView();
						if (view != null) {
							trackMainLayout.addView(view);
						} else {
							// we ll add later that it failed.
							TextView textView = new TextView(mContext);
							textView.setText("Performing Operation ");
							trackMainLayout.addView(textView);
						}
						return trackMainLayout;
					}
				}));

		mTabHost.addTab(mTabHost.newTabSpec(MAP_TAB_TAG).setIndicator(MAP_TAB_TAG,resources.getDrawable(R.drawable.ic_tab_menu_map))
				.setContent(new TabContentFactory() {
					public View createTabContent(String tag) {
						// adding the map with route

						return mMapView;
					}
				}));

		mTabHost.addTab(mTabHost.newTabSpec(SETTING_TAB_TAG).setIndicator(SETTING_TAB_TAG,resources.getDrawable(R.drawable.ic_tab_menu_settings))
				.setContent(new TabContentFactory() {
					public View createTabContent(String arg0) {
						View view = buildSettingInfo();
						if (view != null) {
							settingMainLayout.addView(view);
						} else {
							// we ll add later that it failed.
						}
						return settingMainLayout;
					}
				}));

		mTabHost.setCurrentTabByTag(MAP_TAB_TAG);
		mTabHost.setOnTabChangedListener(this);

		/*
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// Doing all the heavy operation under these handler
				super.handleMessage(msg);
				System.out.println(" handler called ----------------"+msg.what);

				GeoPoint srtLocationGeoPoints = new GeoPoint((int)(mSrtLat * 1e6), (int)(mSrtLng * 1e6));
				GeoPoint endLocationGeoPoints = new GeoPoint((int)(mEndLat * 1e6), (int)(mEndLng * 1e6));

				pinsOverlay = new PinsOverlay(mContext, srtLocationGeoPoints, endLocationGeoPoints);

				MapController mapController = mMapView.getController();

				setZoomOption(mapController, srtLocationGeoPoints);

				distanceInMeters = Math.round(distanceInMeters/10);

				System.out.println(" radius dis in meters "+distanceInMeters);

				myLocationOverlay = new MyLocationOverlay(mContext, mMapView);

				myLocationOverlay.enableMyLocation();
				myLocationOverlay.enableCompass();
                 System.out.println("compass");
				mCurrentLocationNavigation = new CurrentLocationNavigation(mContext, mMapView);
				if (mCurrentLocationNavigation != null) {
					mCurrentLocationNavigation.enableMyLocation();
					mCurrentLocationNavigation.enableCompass();
				}else{

					System.out.println(" class null --------------");
				}
				mCustomProgressDialog.dismiss();
				mapController = mMapView.getController();
				GeoPoint moveTo = new GeoPoint((int)mSrtLat, (int)mSrtLng);
				setZoomOption(mapController, moveTo);

				updatingTrackView();

				listOfOverlays = mMapView.getOverlays();
				listOfOverlays.clear();
				listOfOverlays.add(pinsOverlay);
				//listOfOverlays.add(circleOverlay);
				listOfOverlays.add(myLocationOverlay);
				   System.out.println("com");
				mCustomProgressDialog.dismiss();
				mMapView.invalidate();



				callingAlertService();// calling the service to start.

				RouteOverlay overlay = new RouteOverlay(mFinalGeopoints,mContext, mMapView);
				GeoPoint gPoint = mFinalGeopoints.get(0);
				int moveToLat = gPoint.getLatitudeE6();
				int moveToLng = gPoint.getLongitudeE6();
				moveTo = new GeoPoint(moveToLat, moveToLng);
				mapController = mMapView.getController();

				mCurrentLocationNavigation = new CurrentLocationNavigation(mContext, mMapView);
				mCurrentLocationNavigation.enableMyLocation();
				mCurrentLocationNavigation.enableCompass();

				GeoPoint c = mCurrentLocationNavigation.getMyLocation();
				System.out.println(" testing lat  : "+c.getLatitudeE6());
				System.out.println(" testing lng : "+c.getLongitudeE6());


				// i am trying a logic that according to srtlat,srtlng and endlat,endlng
				// try to set the zoom level so that USER can see srt and end pin on the screen.
				//setZoomOption(mapController, moveTo);

				List<Overlay> listOfOverlays = mMapView.getOverlays();
				listOfOverlays.clear();
				//listOfOverlays.add(overlay);
				listOfOverlays.add(mCurrentLocationNavigation);
				updatingTrackView();
				mMapView.invalidate();



				callingAlertService();// calling the service to start.


			}

		};*/

		mHandler = new Handler(){
			private Object statusCode;

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				if (msg.what == 0) {
					System.out.println("msg"+msg.what);

					GeoPoint srtLocationGeoPoints = new GeoPoint((int)(mSrtLat * 1e6), (int)(mSrtLng * 1e6));
					GeoPoint endLocationGeoPoints = new GeoPoint((int)(mEndLat * 1e6), (int)(mEndLng * 1e6));

					pinsOverlay = new PinsOverlay(mContext, srtLocationGeoPoints, endLocationGeoPoints);

					MapController mapController = mMapView.getController();

					setZoomOption(mapController, srtLocationGeoPoints);

					distanceInMeters = Math.round(distanceInMeters/10);

					System.out.println(" radius dis in meters "+distanceInMeters);

					myLocationOverlay = new MyLocationOverlay(mContext, mMapView);

				/*	myLocationOverlay.enableMyLocation();
					myLocationOverlay.enableCompass();*/
					RouteOverlay overlay = new RouteOverlay(mFinalGeopoints, mContext, mMapView);
					GeoPoint gPoint =mFinalGeopoints.get(0);
					int moveToLat = gPoint.getLatitudeE6();
					int moveToLng = gPoint.getLongitudeE6();
					GeoPoint moveTo = new GeoPoint(moveToLat, moveToLng);
					mapController = mMapView.getController();
					//i am trying a logic that according to srtlat,srtlng and endlat,endlng 
					// try to set the zoom level so that USER can see srt and end pin on the screen.
					//myLocationOverlay = new MyLocationOverlay(mContext, mMapView);
					
				Boolean location =	myLocationOverlay.enableMyLocation();
				        System.out.println("location"+location);
					myLocationOverlay.enableCompass();
					System.out.println("compass");
					listOfOverlays = mMapView.getOverlays();
					listOfOverlays.clear();
					listOfOverlays.add(pinsOverlay);
				//	listOfOverlays.add(circleOverlay);
					listOfOverlays.add(myLocationOverlay);
					System.out.println("com");
					mCustomProgressDialog.dismiss();
				mCurrentLocationNavigation = new CurrentLocationNavigation(mContext, mMapView);
					if (mCurrentLocationNavigation != null) {
						mCurrentLocationNavigation.enableMyLocation();
						mCurrentLocationNavigation.enableCompass();
					}else{

						System.out.println(" class null --------------");
					}
					setZoomOption(mapController, moveTo);
					List<Overlay> listOfOverlays = mMapView.getOverlays();
					listOfOverlays.clear();
					listOfOverlays.add(overlay);
					mMapView.invalidate();
					mCustomProgressDialog.dismiss();
					//mDoneButton.setVisibility(View.VISIBLE);
				}else if (msg.what == 1) {
					System.out.println(" status code : "+statusCode);
					mCustomProgressDialog.dismiss();
					if (GlobalConfiguration.OVER_QUERY_LIMIT.equals(statusCode)) {
						Toast.makeText(mContext, "Query Over Limit From Google", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(mContext, "No Driving Direction. Try Another Destination", Toast.LENGTH_LONG).show();
					}
				}

			}
		};
		//mCustomProgressDialog.dismiss();
		new Thread(new Runnable() {
			@Override
			public void run() {
				getTrackDataFromDB();
				mHandler.sendEmptyMessage(0);
			}
		}).start();
		UiHandler = new Handler(){
			public void handleMessage(Message msg) {
				System.out.println(" UIHANDLER message : "+msg.what);
				int i  =  (int)(AlertService.finalAlertRadius * 2);
				CircleOverlay circleOverlay = new CircleOverlay(i, mEndLat, mEndLng);

				listOfOverlays.clear();

				listOfOverlays.add(pinsOverlay);
				listOfOverlays.add(myLocationOverlay);

				if (!AlertService.firstTimeRadiusFlag) {
					listOfOverlays.add(circleOverlay);
					AlertService.firstTimeRadiusFlag =true;
				}else{
					//listOfOverlays.remove(circleOverlay);
					listOfOverlays.add(circleOverlay);
				}

				mMapView.invalidate();
				getTrackDataFromDB();

				runOnUiThread(new  Runnable() {
					public void run() {
						updatingTrackView();
					}
				});

			};
		};

		TextView zButton =(TextView)findViewById(R.id.zoomButton1);
		TextView zButton1 =(TextView)findViewById(R.id.zoomButton2);
		zButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mapController = mMapView.getController();
				mapController.zoomIn();
				//Toast.makeText(MapViewActivity.this, "Zoomin", Toast.LENGTH_SHORT).show();

			}
		});
		zButton1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mapController = mMapView.getController();
				mapController.zoomOut();
				//Toast.makeText(MapViewActivity.this, "ZoomOUT", Toast.LENGTH_SHORT).show();

			}
		});
	}


	/*private int findRadius(int duration, int distance, int alertBefore) {
		// System.out.println(" input duration :"+duration+" distance :"+distance+" alertbefore :"+alertBefore);
		int radius = -1;
		int avgSpeed = distance / (duration * 60);
		radius = avgSpeed * alertBefore;
		// System.out.println(" radius :"+radius);
		return radius;
	}*/


	@Override
	protected void onPause() {
		super.onPause();
		UiFlag = false;
		myLocationOverlay.disableCompass();
		myLocationOverlay.disableMyLocation();
	}

	@Override
	protected void onResume() {

		UiFlag = true;

		if (MainActivity.applicationOut) {
			System.out.println(" in TAB VIEW true");
			finish();
		}else{
			System.out.println(" in tab view false");
			if (myLocationOverlay != null) {
				myLocationOverlay.enableMyLocation();
				myLocationOverlay.enableCompass();
			}
		}

		super.onResume();
	}

	// To stop all the resources and free it here.
	@Override
	protected void onDestroy() {
		UiFlag = false;
		myLocationOverlay.disableCompass();
		myLocationOverlay.disableMyLocation();
		super.onDestroy();
	}



	/*@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		System.exit(0);
	}*/

	protected void updatingSettingView() {
		// Now i am updating the view because initially in the main thread with will not wait for worker thread. 
		//so after main thread and completion of the worker thread i am updating it.
		if (settingMainLayout != null) {
			settingMainLayout.removeAllViews();
			View v = buildSettingInfo();
			settingMainLayout.addView(v);
		}
	}

	protected void  updatingTrackView(){
		// Now i am updating the view because initially in the main thread with will not wait for worker thread. 
		//so after main thread and completion of the worker thread i am updating it.
		if (trackMainLayout != null) {
			trackMainLayout.removeAllViews();
			View v = buildTrackInfoView();
			trackMainLayout.addView(v);
		}
	}


	@Override
	public void onBackPressed() {
		// get confirmation from user does he wants to run serivce in BG or kill and move to first activity.
		dialogDisplay(" ALert","Do you want to run Alert Service in Background");
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// Here we are controlling layouts accourding to configuration change
		//buildTrackInfoView();
		buildSettingInfo();
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// true if its drawing route on map
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu_tab_view_map, menu);

		getLayoutInflater().setFactory(new Factory() {
			@Override
			public View onCreateView(String name, Context context,
					AttributeSet attrs) {
				if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {
					try {
						LayoutInflater f = getLayoutInflater();
						final View view = f.createView(name, null, attrs);

						new Handler().post(new Runnable() {
							public void run() {
								// set the background drawable
								view.setBackgroundResource(R.drawable.menu_bg);
								// set the text color
								((TextView) view).setTextColor(Color.BLACK);
							}
						});
						return view;
					} catch (InflateException e) {
					} catch (ClassNotFoundException e) {
					}
				}
				return null;
			}
		});
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_new_dest:
			addNewDestination();
			return true;
		case R.id.refresh:
			refreshAllViews();
			return true;
		case R.id.my_location1 : 	
			displayCurrentLocationOnMap(currentGeoPoints);

			System.out.println("locationnnn");
			return true;

		case R.id.satelite:
			mMapView.setSatellite(true);
			mMapView.setStreetView(false);
			mMapView.invalidate();
			return true;
		case R.id.streetView:
			//mMapView.setStreetView(true);
			mMapView.setSatellite(false);
			mMapView.invalidate();
			return true;
		case R.id.traffic:
			mMapView.setTraffic(true);
			mMapView.setStreetView(false);
			mMapView.setSatellite(false);
			mMapView.invalidate();
			return true;		
		default:
			return super.onOptionsItemSelected(item);
		}
	}



	private void refreshAllViews() {
		// refreshing all the views
		// get data from db and replace in mtempData and give to view to updata screen.

		mCustomProgressDialog.loading("Refreshing", "Fetching Current Location", false);
		getTrackDataFromDB();

		MyLocationListner myLocationListner = new MyLocationListner(mContext);
		if (myLocationListner.getLocationFromProvider("network")) {
			// try to get the current location from network
			try{
				List<Address> addresses = myLocationListner.getAddressFromLocation();
				Address add = addresses.get(0);
				String currentAddress = myLocationListner.getDetailedAddress(add);
				mTempData.remove(0);
				mTempData.add(0, currentAddress);
			}catch (Exception e) {
				// not able to fetech or get the current location so now,
				// put the last know user address from the db
			}

		}else {
			// try to get the current location from passive
			if(myLocationListner.getLocationFromProvider("passive")){
				try{
					List<Address> addresses = myLocationListner.getAddressFromLocation();
					Address add = addresses.get(0);
					String currentAddress = myLocationListner.getDetailedAddress(add);
					mTempData.remove(0);
					mTempData.add(0, currentAddress);
				}catch (Exception e) {
					// not able to fetech or get the current location so now,
					// put the last know user address from the db
				}
			}else{
				//  put the last know user address from the db
			}
		}

		updatingTrackView();
		buildSettingInfo();

		myLocationListner.stopLocationListner();
		mCustomProgressDialog.dismiss();

		buildTrackInfoView();
	}


	private void getTrackDataFromDB() {
		if (mTempData == null) {
			mTempData =  new ArrayList<String>();
		}
		Cursor cursor= Config.Instance().getCurrentStatusTable();
		if (cursor.getCount() >0 ) {
			mTempData.clear();
			cursor.moveToFirst();
			do{
				mTempData.add(cursor.getString(cursor.getColumnIndex("srtaddress")));
				mTempData.add(cursor.getString(cursor.getColumnIndex("endaddress")));
				mTempData.add(Double.toString(cursor.getDouble(cursor.getColumnIndex("avgspeed"))));
				mTempData.add(cursor.getString(cursor.getColumnIndex("remainingDisatance")));
				String s =cursor.getString(cursor.getColumnIndex("remainingTime"));
				mTempData.add(s);

			}while(cursor.moveToNext());
		}else{
			System.out.println(" NO DB ");
			mTempData.add("null");
			mTempData.add("null");
			mTempData.add("null");
			mTempData.add("null");
			mTempData.add("null");
		}

		for (String s : mTempData) {
			System.out.println(" new data to UI  : "+s);
		}


	}

	private void addNewDestination() {
		// here we are displaying a dialog that USER really want to change
		// Destination or not.
		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog);
		final TextView heading = (TextView) dialog.findViewById(R.id.cd_title);
		TextView message = (TextView) dialog.findViewById(R.id.cd_message);
		heading.setText(" Alert !!! ");
		message.setText("Are you sure want to add new destination tracker ?");
		RelativeLayout buttonLayout = (RelativeLayout) dialog
		.findViewById(R.id.cd_button_layout);
		LayoutInflater factory = LayoutInflater.from(mContext);
		View view = factory.inflate(R.layout.two_button, null);
		buttonLayout.addView(view);
		view.findViewById(R.id.button_positive).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						Config.Instance().dropStatusTable();
						Config.Instance().dropCurrentLocTable();
						Config.Instance().dropGeoLocationTable();
						Config.Instance().dropMaxMinLocationTable();

						Intent serviceIntent = new Intent(mContext, AlertService.class);
						if (serviceIntent != null) {
							stopService(serviceIntent);
						}

						/*Intent mainIntent = new Intent(mContext, MainActivity.class);
						mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(mainIntent);*/
						finish();
					}
				});

		view.findViewById(R.id.button_negative).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
		dialog.show();

	}

	@Override
	public void onTabChanged(String tabId) {
		// Tab change listner

	}

	protected View buildTrackInfoView() {

		if (mTempData != null) {
			LinearLayout linearLayout = loadTrackViewFromXML();

			TextView textView = (TextView) linearLayout.findViewById(R.id.text_info);
			textView.setText("Track Info:");
			//LinearLayout dataLinearLayoutMain = (LinearLayout)linearLayout.findViewById(R.id.track_info_layout);
			LinearLayout dataLayoutMain = (LinearLayout) linearLayout.findViewById(R.id.track_info_layout);
			LinearLayout dataLayout = (LinearLayout) View.inflate(mContext,R.layout.new_track_view, null);
			bindingDataToTrackView(dataLayout);
			dataLayoutMain.addView(dataLayout);

			//ListView listView = (ListView) linearLayout.findViewById(R.id.track_info_list);
			//TrackListAdapter adapter = new TrackListAdapter(mContext,mDirivingData);
			//listView.setAdapter(adapter);
			// Drawable drawableInfo =
			// getResources().getDrawable(R.drawable.line);
			// listView.setDivider(drawableInfo);
			//listView.setCacheColorHint(Color.TRANSPARENT);

			return linearLayout;
		} else {
			return null;
		}
	}

	private void bindingDataToTrackView(LinearLayout linearLayout) {
		// i am binding data to view

		TextView youareat = (TextView) linearLayout.findViewById(R.id.youareat);
		TextView youareatData = (TextView) linearLayout.findViewById(R.id.youareat_data);

		TextView dest = (TextView) linearLayout.findViewById(R.id.dest);
		TextView destData = (TextView) linearLayout.findViewById(R.id.dest_data);

		TextView avgSpeed = (TextView) linearLayout.findViewById(R.id.avg_speed);
		TextView avgSpeedData = (TextView) linearLayout.findViewById(R.id.avg_speed_data);

		TextView remainingDist = (TextView) linearLayout.findViewById(R.id.remaining_dist);
		TextView remainigDistData = (TextView) linearLayout.findViewById(R.id.remainig_dist_data);

		TextView estTimeArrival = (TextView) linearLayout.findViewById(R.id.est_time_arrival);
		TextView estTimeArrivalData = (TextView) linearLayout.findViewById(R.id.est_time_arrival_data);


		youareat.setText("You are At");
		youareatData.setText(mTempData.get(0));

		dest.setText("Destination");
		destData.setText(mTempData.get(1));

		avgSpeed.setText("Average Speed");
		avgSpeedData.setText(mTempData.get(2));

		remainingDist.setText("Remaining Distance");
		remainigDistData.setText(mTempData.get(3));

		estTimeArrival.setText("Estimated Time Arrival");
		estTimeArrivalData.setText(mTempData.get(4));

	}

	private LinearLayout loadTrackViewFromXML() {
		// i am loading these view from xml and making in method because when Orientation changes then i ll load again.
		return (LinearLayout) View.inflate(mContext,R.layout.track_info_main, null);
	}

	protected View buildSettingInfo() {
		LinearLayout linearLayout = (LinearLayout) View.inflate(mContext,R.layout.setting_list_view_body, null);
		if (linearLayout != null) {
			TextView textView = (TextView) linearLayout.findViewById(R.id.setting_text_info);
			textView.setText("Change Alert Time :");
			listView = (ListView) linearLayout.findViewById(R.id.setting__info_list);
			// Drawable drawableInfo =
			// getResources().getDrawable(R.drawable.line);
			// listView.setDivider(drawableInfo);

			//System.out.println(" alert time :" +userAlertTime);
			//System.out.println(" duration time : "+durationTime);

			listView.setCacheColorHint(Color.TRANSPARENT);

			final String[] items = new String[60];
			int temp = 1;

			for (int i = 0; i < 60; i++) {
				items[i] = Integer.toString(temp) + " " + " Mins";
				temp = temp + 1;
			}
			listView.setAdapter(new ArrayAdapter<String>(mContext,R.layout.list_with_radio_button, items));
			listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
			listView.setItemsCanFocus(true);
			userAlertTime = userAlertTime - 1;
			listView.setItemChecked(userAlertTime, true);
			listView.setOnItemClickListener(new OnItemClickListener() {
				int intDurationTime = Integer.parseInt(durationTime);
				@Override
				public void onItemClick(AdapterView<?> arg0,View textViewChecked, int position, long arg3) {

					latestAlertTime = position + 1;
					//System.out.println(" list view position :"+ latestAlertTime);
					if (latestAlertTime > intDurationTime) {
						Toast.makeText(mContext,"Alert Time should be Less then Destination Time. Your Destination time is "+ intDurationTime + " Mins",Toast.LENGTH_LONG).show();
						listView.setItemChecked(userAlertTime, true);
					} else {
						dialogToChangeAlertTime("Alert","Are you Sure to Change Alert Time");
					}
				}
			});

			return linearLayout;
		} else {
			return null;
		}
	}

		private ListView clearAll(ListView tempListView) {

		Adapter adapter = tempListView.getAdapter();

		tempListView.setAdapter((ListAdapter) adapter);

		return tempListView;

	}

	/*protected void doingMapOperation(final double srtLat2, final double srtLng2,
			final double endLat2, final double endlng2) {
		new Thread(){


			public void run(){

				System.out.println("doing map");
				mDirection = new MyDrivingDirection();
				String url = mDirection.buildURLByLocation(srtLat2, srtLng2, endLat2, endlng2);
				InputStream inputStream = mDirection.getConnection(url);
				StringBuilder stringInputStream = mDirection.convertStreamToString(inputStream);

				boolean canDrawDrivingDirection = mDirection.getJsonCheckStausCode(stringInputStream.toString());
				System.out.println(" boolean value : "+canDrawDrivingDirection);
				if (canDrawDrivingDirection) {
					mDirection.jsonDataparsing(stringInputStream.toString());
					mDirection.jsonGeoPointsParsing(stringInputStream.toString());
					mFinalGeopoints = new ArrayList<GeoPoint>();
					mFinalGeopoints = mDirection.getgeoPoints();
					//distanceInKm = mDirection.getdistanceInKm();
					//durationInMinutes = mDirection.getDurationInMinutes();
					mTempData = new ArrayList<String>();
					mTempData = mDirection.getTempData();
					Bundle b =  new Bundle();
					Message msg = new Message();
					//String time_distance =durationInMinutes +" - "+distanceInKm;
					//b.putString("distance",time_distance);
					//msg.setData(b);
					//msg.what =0;
					//mHandler.sendMessage(msg);

				}else{
					//	statusCode = mDirection.getStatusCode();
					mHandler.sendEmptyMessage(1);
					System.out.println("phani");
				}
			}
		}.start();
	}
	 */



	protected void mapOperation() {
		new Thread() {
			public void run() {
				System.out.println(" run in map operation ");
				//true means coming from first activity need to get data from database
				//false means coming from second activity need to save data to database and call the Service
				Config config = Config.Instance();
				if (dbStatus) {
					mFinalGeopoints = new ArrayList<GeoPoint>();
					Cursor cursor = config.getGeoLocationTable();
					cursor.moveToFirst();
					do{
						String srtLat = cursor.getString(cursor.getColumnIndex("lat"));
						String srtLng = cursor.getString(cursor.getColumnIndex("lng"));
						mFinalGeopoints.add(new GeoPoint(Integer.parseInt(srtLat), Integer.parseInt(srtLng)));
						System.out.println("GEOpoints"+mFinalGeopoints);
					}while(cursor.moveToNext());
				}else{
					mFinalGeopoints = MapViewActivity.mFinalGeopoints;
					mTempData = MapViewActivity.mTempData;
					System.out.println("GEOpoints1"+mFinalGeopoints);

					if (mFinalGeopoints != null) {
						synchronized (this) {
							for (GeoPoint gp : mFinalGeopoints) {
								config.setValuesToGeoLocation(Integer.toString(gp.getLatitudeE6()), Integer.toString(gp.getLongitudeE6()));
							}
						}
					}
					MapViewActivity.mFinalGeopoints=null;// i am trying to free these huge variable from memory
					MapViewActivity.mTempData=null;// i am trying to free these huge variable from memory

				}
				if (!dbStatus) {
					System.out.println("alertservice");
					callingAlertService();
				}



				mHandler.sendEmptyMessage(0);
				System.out.println(" starting new thread :");
			}
		}.start();

	}


	private void displayCurrentLocationOnMap(GeoPoint currentGeoPoints) {
		// i am displaying current location of the user by showing pin on map
		MyLocationOverlay myLocOverlay = new MyLocationOverlay(this, mMapView);
		Boolean loc =	myLocOverlay.enableMyLocation();
		System.out.println("loc"+loc);
		Boolean comp =	myLocOverlay.enableCompass();
		System.out.println("comp"+comp);
		MapController mapController = mMapView.getController();
		mMapView.getOverlays().add(myLocOverlay);
		mapController.setCenter(currentGeoPoints);
		mMapView.invalidate();
	}
	protected void callingAlertService() {
		// Setting valuses in the second table(current loc table) and finally starting service
		//Config.Instance().setValuesToCurrentLoc(mSrtLat, mSrtLng, mEndLat, mEndLng, mTempData.get(0), mTempData.get(1), userAlertTime, 18, MapViewActivity.distanceInKm,MapViewActivity.durationInMinutes);
		//System.out.println(" callingAlertService ");
		Intent serviceIntent = new Intent(TabViewActivity.this, AlertService.class);
		//serviceIntent.putExtra("srtLat", srtLat);
		//serviceIntent.putExtra("srtlng", srtLng);
		//serviceIntent.putExtra("endlat", endLat);
		//serviceIntent.putExtra("endlng", endLng);
		//serviceIntent.putExtra("alerttime", userAlertTime);
		//serviceIntent.putExtra("durationtime", durationTime);
		startService(serviceIntent);
		// bindService(intent, null, Context.BIND_AUTO_CREATE); // no need of binding

		//setProximityAlert(); these logic was for the proximityAlert by the radius.

	}
	/*private void setProximityAlert() {

		System.out.println(" Setted Proximate alert ");
		float radius = 1000; // meters
		long expiration = -1; // do not expire
		//final String  TREASURE_PROXIMITY_ALERT = "com.paradigmcreatives.proximityalerts.action.PROXIMITY_ALERT";

		final String  TREASURE_PROXIMITY_ALERT = "com.paradigmcreatives.activity.TabViewActivity";

		LocationManager locationManager;
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

		Intent intent = new Intent(TREASURE_PROXIMITY_ALERT);
		PendingIntent pendingProximityIntent = PendingIntent.getBroadcast(this, 0,intent,0);

		locationManager.addProximityAlert(mEndLat, mEndLng, radius, expiration,pendingProximityIntent);

		IntentFilter filter = new IntentFilter(TREASURE_PROXIMITY_ALERT);
		registerReceiver(new ProximityIntentReceiver(), filter);

	}*/

	protected void setZoomOption(MapController mapController, GeoPoint moveTo) {
		// try to show srt and end points on the screen by setting zoom controls
		// and by animate method

		int minLatitude = Integer.MAX_VALUE;
		int maxLatitude = Integer.MIN_VALUE;
		int minLongitude = Integer.MAX_VALUE;
		int maxLongitude = Integer.MIN_VALUE;

		Cursor cursor = Config.Instance().getMaxMinLocationTable();

		if (cursor.getCount() >0 ) {
			cursor.moveToFirst();
			do{
				minLatitude =  (int)cursor.getDouble(cursor.getColumnIndex("minlat"));
				maxLatitude =  (int)cursor.getDouble(cursor.getColumnIndex("maxlat"));
				minLongitude =  (int)cursor.getDouble(cursor.getColumnIndex("minlng"));
				maxLongitude =  (int)cursor.getDouble(cursor.getColumnIndex("maxlng"));

			}while(cursor.moveToNext());
		}
		/*for (GeoPoint item : mFinalGeopoints) {
			int lat = item.getLatitudeE6();
			int lon = item.getLongitudeE6();
			maxLatitude = Math.max(lat, maxLatitude);
			minLatitude = Math.min(lat, minLatitude);
			maxLongitude = Math.max(lon, maxLongitude);
			minLongitude = Math.min(lon, minLongitude);
		}*/


		mapController.animateTo(new GeoPoint((maxLatitude + minLatitude) / 2,(maxLongitude + minLongitude) / 2));
		mapController.zoomToSpan(Math.abs(maxLatitude - minLatitude),Math.abs(maxLongitude - minLongitude));
	}

	// logic to FIRE the ALERT

	/*
	 * private void setProximityAlert() {
	 * System.out.println(" setProximityAlert"); String locService =
	 * Context.LOCATION_SERVICE; LocationManager locationManager;
	 * locationManager = (LocationManager)getSystemService(locService);
	 * 
	 * IntentFilter filter = new IntentFilter(TREASURE_PROXIMITY_ALERT);
	 * ProximityIntentReceiver proximityIntentReceiver = new
	 * ProximityIntentReceiver(); registerReceiver(proximityIntentReceiver,
	 * filter);
	 * 
	 * 
	 * int i =findRadius(Integer.parseInt(durationTime), distance, (alertTime));
	 * System.out.println( "radius : "+i); //double lat = endLat; //double lng =
	 * endLng; //System.out.println(" end lat :"+endLat);
	 * //System.out.println(" end lng :"+endLng); float radius = i; // meters
	 * long expiration = -1; // do not expire Intent intent = new
	 * Intent(TREASURE_PROXIMITY_ALERT); PendingIntent proximityIntent =
	 * PendingIntent.getBroadcast(this, -1,intent,0);
	 * locationManager.addProximityAlert(endLat, endLng, radius,
	 * expiration,proximityIntent); //
	 * unregisterReceiver(proximityIntentReceiver); }
	 */

	/*
	 * public class ProximityIntentReceiver extends BroadcastReceiver {
	 * 
	 * @Override public void onReceive (Context context, Intent intent) { String
	 * key = LocationManager.KEY_PROXIMITY_ENTERING; Boolean entering =
	 * intent.getBooleanExtra(key, false); // what should be shown once the USER
	 * enter the location startActivity(new
	 * Intent(getApplicationContext(),com.paradigmcreatives
	 * .activity.AlarmActivity.class)); } }
	 */

	private void dialogDisplay(String title, String messagedata) {
		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog);
		final TextView heading = (TextView) dialog.findViewById(R.id.cd_title);
		TextView message = (TextView) dialog.findViewById(R.id.cd_message);
		RelativeLayout buttonLayout = (RelativeLayout) dialog.findViewById(R.id.cd_button_layout);
		LayoutInflater factory = LayoutInflater.from(mContext);
		View view = factory.inflate(R.layout.two_button, null);
		buttonLayout.addView(view);
		final Button positive = (Button) view.findViewById(R.id.button_positive);
		heading.setText(title);
		message.setText(messagedata);

		positive.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// let the service run in back ground and call the home screen.
				dialog.dismiss();
				Intent setIntent = new Intent(Intent.ACTION_MAIN);
				setIntent.addCategory(Intent.CATEGORY_HOME);
				setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(setIntent);
			}
		});

		final Button negative = (Button) view.findViewById(R.id.button_negative);
		negative.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Move to First activity
				Intent serviceIntent = new Intent(mContext, AlertService.class);
				if (serviceIntent != null) {
					stopService(serviceIntent);
				}
				Config.Instance().dropStatusTable();
				Config.Instance().dropCurrentLocTable();
				Config.Instance().dropGeoLocationTable();
				Config.Instance().dropMaxMinLocationTable();
				dialog.dismiss();
				finish();
				/*Intent intent = new Intent(TabViewActivity.this,MainActivity.class);
				intent.putExtra("applicationOut", true);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);*/
				//TabViewActivity.this.finish();
			}
		});
		dialog.show();
	}

	private void dialogToChangeAlertTime(String title, String messagedata){
		final Dialog dialog = new Dialog(mContext);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.custom_dialog);
		final TextView heading =(TextView)dialog.findViewById(R.id.cd_title);
		TextView message = (TextView)dialog.findViewById(R.id.cd_message);
		RelativeLayout buttonLayout = (RelativeLayout)dialog.findViewById(R.id.cd_button_layout);
		LayoutInflater factory = LayoutInflater.from(mContext);
		View view = factory.inflate(R.layout.two_button, null);
		buttonLayout.addView(view);
		final Button positive = (Button)view.findViewById(R.id.button_positive);
		heading.setText(title);
		message.setText(messagedata);

		positive.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Changing the alert time so i am stoping service and calling service with new values.

				listView.setItemChecked(latestAlertTime-1, true);
				userAlertTime = latestAlertTime-1;

				Intent intent =  new Intent(mContext, AlertService.class);
				stopService(intent);
				Config.Instance().dropCurrentLocTable();
				Config.Instance().dropCurrentLocTable();
				dialog.dismiss();
				callingAlertService();

			}
		});

		final Button negative = (Button)view.findViewById(R.id.button_negative);
		negative.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// dimiss and set the default value
				dialog.dismiss();
				//userAlertTime = userAlertTime -1;
				//System.out.println(" ------user alert at NEG : "+userAlertTime);
				listView.setItemChecked(userAlertTime, true);
			}
		});
		dialog.show();
	}

	class CurrentLocationNavigation extends MyLocationOverlay{

		public CurrentLocationNavigation(Context context, MapView mapView) {
			super(context, mapView);
			System.out.println("CurrentLocationNavigation construtor  ");
		}

	}
	 


}
