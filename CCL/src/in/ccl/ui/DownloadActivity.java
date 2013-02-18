package in.ccl.ui;

import in.ccl.adapters.GridAdapter;
import in.ccl.database.CCLPullService;
import in.ccl.database.DataProviderContract;
import in.ccl.database.DownloadItemsCursor;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadActivity extends TopActivity {

	private ArrayList <Items> downloadItemsArrayList;

	private GridView gridviewDownload;

	private GridAdapter adapter;

	private TextView txt_download_header;

	private DownloadStateReceiver mDownloadStateReceiver;

	private IntentFilter statusIntentFilter;

	private boolean isAsyncTask = false;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContent(R.layout.download_layout);

		// The filter's action is BROADCAST_ACTION
		statusIntentFilter = new IntentFilter(Constants.BROADCAST_ACTION);

		// Sets the filter's category to DEFAULT
		statusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

		// Instantiates a new DownloadStateReceiver
		mDownloadStateReceiver = new DownloadStateReceiver();

		gridviewDownload = (GridView) findViewById(R.id.grid_download_layout);
		txt_download_header = (TextView) findViewById(R.id.txt_download_header);

		Util.setTextFont(this, txt_download_header);
		txt_download_header.setText(getResources().getString(R.string.hot_downloads));

		if (getIntent().hasExtra(Constants.EXTRA_DOWNLOAD_KEY)) {
			downloadItemsArrayList = getIntent().getParcelableArrayListExtra(Constants.EXTRA_DOWNLOAD_KEY);
		}

		if (downloadItemsArrayList.size() > 0 && downloadItemsArrayList != null) {
			adapter = new GridAdapter(DownloadActivity.this, downloadItemsArrayList, "downloads");
			gridviewDownload.setAdapter(adapter);
		}

		if (downloadItemsArrayList != null && downloadItemsArrayList.size() > 0) {
			gridviewDownload.setOnScrollListener(new EndlessScrollListener(DownloadActivity.this, adapter, 0, EndlessScrollListener.RequestType.DOWNLOAD_IMAGE_REQUEST, downloadItemsArrayList.get(0).getNumberOfPages()));

		}
		gridviewDownload.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick (AdapterView <?> arg0, View view, int position, long arg3) {

				Intent intent = new Intent(DownloadActivity.this, PhotoActivity.class);
				intent.putParcelableArrayListExtra(Constants.EXTRA_PHOTO_KEY, downloadItemsArrayList);
				intent.putExtra(Constants.EXTRA_PHOTO_POSITION_ID, position);
				startActivity(intent);
			}
		});

	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive (Context context, Intent intent) {
			String action = intent.getAction();
			if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
				Toast.makeText(DownloadActivity.this, getResources().getString(R.string.downloading_succuss), Toast.LENGTH_SHORT).show();

			}
		}
	};

	@Override
	protected void onResume () {
		super.onResume();

		if (Util.getInstance().isOnline(DownloadActivity.this)) {
			if (isAsyncTask) {
				adapter.downloadStartOnResume();
				isAsyncTask = false;
			}
		}
		else {
			Toast.makeText(DownloadActivity.this, getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
		}
		
		LocalBroadcastManager.getInstance(this).registerReceiver(mDownloadStateReceiver, statusIntentFilter);
		registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

		if (Util.getInstance().isOnline(DownloadActivity.this)) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run () {
					Intent mServiceIntent = new Intent(DownloadActivity.this, CCLPullService.class).setData(Uri.parse(getResources().getString(R.string.downloads_url)));
					mServiceIntent.putExtra("KEY", "download_updates");
					startService(mServiceIntent);

				}
			}, 20000);
		}

	}

	protected void onPause () {
		LocalBroadcastManager.getInstance(DownloadActivity.this).unregisterReceiver(mDownloadStateReceiver);
		unregisterReceiver(receiver);
		if (adapter != null && adapter.mDownloaderImage != null) {
			if (this.adapter.mDownloaderImage.getStatus() == AsyncTask.Status.RUNNING) {
				isAsyncTask = true;
				adapter.downloadStop();
			}
		}
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

				case in.ccl.database.Constants.STATE_ACTION_UPDATE_DOWNLOAD_IMAGE_COMPLETE:

					Cursor cursor = getContentResolver().query(DataProviderContract.DOWNLOAD_IMAGE_TABLE_CONTENTURI, null, null, null, null);
					DownloadItemsCursor downloadItemCursor = new DownloadItemsCursor();
					ArrayList <Items> downloadlList = downloadItemCursor.getItems(cursor);
					if (downloadlList != null && downloadlList.size() > 0) {
						adapter = new GridAdapter(DownloadActivity.this, downloadlList, "downloads");
						gridviewDownload.setAdapter(adapter);
					}
					break;
				default:
					break;
			}
		}
	}
}
