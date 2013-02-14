package in.ccl.ui;

import in.ccl.adapters.GridAdapter;
import in.ccl.database.CCLPullService;
import in.ccl.database.CalendarItemsCursor;
import in.ccl.database.DataProviderContract;
import in.ccl.helper.Util;
import in.ccl.imageloader.EndlessScrollListener;
import in.ccl.model.Items;
import in.ccl.util.Constants;
import java.util.ArrayList;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class CalendarActivity extends TopActivity {
    
    private ArrayList <Items> calendarItemsArrayList;

	private GridView gridviewCalendar;

	private GridAdapter adapter;

	private TextView txt_calendar_header;

	private CalenderStateReceiver mCalenderStateReceiver;

	private IntentFilter statusIntentFilter;
    
    
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContent(R.layout.calendar_layout);
        
        
     // The filter's action is BROADCAST_ACTION
	statusIntentFilter = new IntentFilter(Constants.BROADCAST_ACTION);

	// Sets the filter's category to DEFAULT
	statusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

	// Instantiates a new DownloadStateReceiver
	mCalenderStateReceiver = new CalenderStateReceiver();

	gridviewCalendar = (GridView) findViewById(R.id.grid_calendar_layout);
	txt_calendar_header = (TextView) findViewById(R.id.txt_calendar_header);

	Util.setTextFont(this, txt_calendar_header);
	txt_calendar_header.setText(getResources().getString(R.string.calender));
	
	
	
	if (getIntent().hasExtra(Constants.EXTRA_CALENDAR_KEY)) {
		calendarItemsArrayList = getIntent().getParcelableArrayListExtra(Constants.EXTRA_CALENDAR_KEY);
		System.out.println("nagehs calender itemlist"+calendarItemsArrayList.size());
	}

	if (calendarItemsArrayList.size() > 0 && calendarItemsArrayList != null) {
		adapter = new GridAdapter(CalendarActivity.this, calendarItemsArrayList, "calendar");
		gridviewCalendar.setAdapter(adapter);
	}
        
	if (calendarItemsArrayList != null && calendarItemsArrayList.size() > 0) {
	    gridviewCalendar.setOnScrollListener(new EndlessScrollListener(CalendarActivity.this, adapter, 0, EndlessScrollListener.RequestType.DOWNLOAD_IMAGE_REQUEST, calendarItemsArrayList.get(0).getNumberOfPages()));

	}
	
	
	gridviewCalendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick (AdapterView <?> arg0, View view, int position, long arg3) {

			Intent intent = new Intent(CalendarActivity.this, PhotoActivity.class);
			intent.putParcelableArrayListExtra(Constants.EXTRA_PHOTO_KEY, calendarItemsArrayList);
			intent.putExtra(Constants.EXTRA_PHOTO_POSITION_ID, position);
			startActivity(intent);
		}
	});
	
	
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

	
    }

    
    BroadcastReceiver receiver = new BroadcastReceiver() {
	      @Override
	      public void onReceive(Context context, Intent intent) {
	          String action = intent.getAction();
	          if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
	    Toast.makeText(CalendarActivity.this, getResources().getString(R.string.downloading_succuss),Toast.LENGTH_SHORT).show();

	          }
	      }
	  };

    @Override
	protected void onResume () {
		super.onResume();		
		LocalBroadcastManager.getInstance(this).registerReceiver(mCalenderStateReceiver, statusIntentFilter);
		if (Util.getInstance().isOnline(CalendarActivity.this)) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run () {
					Intent mServiceIntent = new Intent(CalendarActivity.this, CCLPullService.class).setData(Uri.parse(getResources().getString(R.string.calender_url)));
					mServiceIntent.putExtra("KEY", "calendar_updates");
					startService(mServiceIntent);

				}
			}, 20000);
		}

	}
    
    
    
    
    protected void onPause () {
	LocalBroadcastManager.getInstance(CalendarActivity.this).unregisterReceiver(mCalenderStateReceiver);
	super.onPause();
}

             
    protected boolean isAppInstalled (String packageName) {
	
	Intent mIntent = getPackageManager().getLaunchIntentForPackage(packageName);
	if (mIntent != null) {
		return true;
	}
	else {
		return false;
	}
}
    
    
      	private class CalenderStateReceiver extends BroadcastReceiver {

		private CalenderStateReceiver () {
		}

		/**
		 * 
		 * This method is called by the system when a broadcast Intent is matched by this class' intent filters
		 * 
		 * @param context An Android context
		 * @param intent The incoming broadcast Intent
		 */
		@Override
		public void onReceive (Context context, Intent intent) {

			// Gets the status from the Intent's extended data, and chooses the appropriate action

			switch (intent.getIntExtra(Constants.EXTENDED_DATA_STATUS, Constants.STATE_ACTION_COMPLETE)) {

				case in.ccl.database.Constants.STATE_ACTION_UPDATE_CALENDAR_IMAGE_COMPLETE:

					Cursor cursor = getContentResolver().query(DataProviderContract.CALENDAR_IMAGE_TABLE_CONTENTURI, null, null, null, null);
					CalendarItemsCursor mCalendarItemsCursor = new CalendarItemsCursor();
					ArrayList <Items> calendarList = mCalendarItemsCursor.getItems(cursor);
					if (cursor != null) {
						cursor.close();
					}
					if (calendarList.size() > 0 && calendarList != null) {
						adapter = new GridAdapter(CalendarActivity.this, calendarList, "calendar");
						gridviewCalendar.setAdapter(adapter);
					}
					break;
				default:
					break;
			}
		}
	}        
              
    
}
