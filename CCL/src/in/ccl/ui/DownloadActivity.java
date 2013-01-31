package in.ccl.ui;

import in.ccl.adapters.GridAdapter;
import in.ccl.helper.Util;
import in.ccl.imageloader.EndlessScrollListener;
import in.ccl.model.Items;
import in.ccl.util.Constants;
import java.util.ArrayList;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

public class DownloadActivity extends TopActivity {

	private ArrayList<Items> downloadItemsArrayList;	
	
	private GridView gridviewDownload;
	
	private GridAdapter adapter;
	
	private TextView txt_download_header;
	
	private DownloadStateReceiver mDownloadStateReceiver;
	
	private IntentFilter statusIntentFilter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContent(R.layout.download_layout);
		
		// The filter's action is BROADCAST_ACTION
		statusIntentFilter = new IntentFilter(Constants.BROADCAST_ACTION);

		// Sets the filter's category to DEFAULT
		statusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);
		

		// Instantiates a new DownloadStateReceiver
		mDownloadStateReceiver = new DownloadStateReceiver();
		
		
		 gridviewDownload               =        (GridView) findViewById(R.id.grid_download_layout);
		 txt_download_header            =         (TextView) findViewById(R.id.txt_download_header);
		
		Util.setTextFont(this, txt_download_header);
		txt_download_header.setText(getResources().getString(R.string.downloads));

		if (getIntent().hasExtra(Constants.EXTRA_DOWNLOAD_KEY)) {
			downloadItemsArrayList = getIntent().getParcelableArrayListExtra(Constants.EXTRA_DOWNLOAD_KEY);
		}

		if(downloadItemsArrayList.size() >0 && downloadItemsArrayList != null){
		 adapter = new GridAdapter(DownloadActivity.this,downloadItemsArrayList, "downloads");
		  gridviewDownload.setAdapter(adapter);
		}
		
		System.out.println("no of  list is"+downloadItemsArrayList.size());
		
		if (downloadItemsArrayList != null && downloadItemsArrayList.size() > 0) {
			System.out.println("no of pages"+downloadItemsArrayList.get(0).getNumberOfPages());
			gridviewDownload.setOnScrollListener(new EndlessScrollListener(DownloadActivity.this, adapter, 0, EndlessScrollListener.RequestType.DOWNLOAD_IMAGE_REQUEST, downloadItemsArrayList.get(0).getNumberOfPages()));

		}
		
		gridviewDownload.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View view,	int position, long arg3) {

						Intent intent = new Intent(DownloadActivity.this,PhotoActivity.class);
						intent.putParcelableArrayListExtra(	Constants.EXTRA_PHOTO_KEY,downloadItemsArrayList);
						intent.putExtra(Constants.EXTRA_PHOTO_POSITION_ID,position);
						startActivity(intent);
					}
				});
	}

	
	@Override
	protected void onResume () {
		// TODO Auto-generated method stub
		super.onResume();
		LocalBroadcastManager.getInstance(DownloadActivity.this).registerReceiver(mDownloadStateReceiver, statusIntentFilter);
	}
	@Override
	protected void onPause () {
		// TODO Auto-generated method stub
		super.onPause();
		LocalBroadcastManager.getInstance(DownloadActivity.this).unregisterReceiver(mDownloadStateReceiver);

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
	
	
	
	private class DownloadStateReceiver extends BroadcastReceiver {

		private DownloadStateReceiver () {
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

				case in.ccl.database.Constants.STATE_ACTION_DOWNLOAD_IMAGE_COMPLETE:
					if (intent != null && intent.hasExtra("list")) {
						ArrayList <Items> list = intent.getParcelableArrayListExtra("list");
						adapter.updateList(list);
					}
					break;
				default:
					break;
			}
		}
	}
	
	
	
	
}
