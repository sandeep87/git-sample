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

public class PhotoAlbumActivity extends TopActivity {

	private GridView gridView;

	private ArrayList <Items> photoGalleryList;

	private GridAdapter adapter;

	private DownloadStateReceiver mDownloadStateReceiver;

	private IntentFilter statusIntentFilter;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		addContent(R.layout.grid_layout);

		// The filter's action is BROADCAST_ACTION
		statusIntentFilter = new IntentFilter(Constants.BROADCAST_ACTION);

		// Sets the filter's category to DEFAULT
		statusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

		// Instantiates a new DownloadStateReceiver
		mDownloadStateReceiver = new DownloadStateReceiver();

		String albumTitle = getIntent().getStringExtra(Constants.EXTRA_ALBUM_TITLE);
		int photoGalleryId = getIntent().getIntExtra(Constants.EXTRA_ALBUM_ID, 1);
		TextView txtAlbumTitle = (TextView) findViewById(R.id.txt_album_title);
		Util.setTextFont(this, txtAlbumTitle);
		txtAlbumTitle.setText(albumTitle);
		TextView txtAlbumHeader = (TextView) findViewById(R.id.txt_album_header);
		Util.setTextFont(this, txtAlbumHeader);
		txtAlbumHeader.setText(getResources().getString(R.string.photos));

		if (getIntent().hasExtra(Constants.EXTRA_ALBUM_ITEMS)) {
			photoGalleryList = getIntent().getParcelableArrayListExtra(Constants.EXTRA_ALBUM_ITEMS);
		}

		gridView = (GridView) findViewById(R.id.photos_gridview);
		adapter = new GridAdapter(PhotoAlbumActivity.this, photoGalleryList, "photo");
		gridView.setAdapter(adapter);
		
		System.out.println("Rajesh album count "+photoGalleryList.size());

		if (photoGalleryList != null && photoGalleryList.size() > 0) {
			gridView.setOnScrollListener(new EndlessScrollListener(this, adapter, photoGalleryId, EndlessScrollListener.RequestType.PHOTO_GALLERY_REQUEST, photoGalleryList.get(0).getNumberOfPages()));

		}

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick (AdapterView <?> arg0, View view, int position, long arg3) {
				// Sending image id to FullScreenActivity
				Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);
				// passing array index
				intent.putParcelableArrayListExtra(Constants.EXTRA_PHOTO_KEY, photoGalleryList);
				intent.putExtra(Constants.EXTRA_PHOTO_POSITION_ID, position);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume () {
		// TODO Auto-generated method stub
		super.onResume();
		LocalBroadcastManager.getInstance(this).registerReceiver(mDownloadStateReceiver, statusIntentFilter);
	}

	@Override
	protected void onPause () {
		// TODO Auto-generated method stub
		super.onPause();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mDownloadStateReceiver);

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

				case in.ccl.database.Constants.STATE_ACTION_PHOTO_COMPLETE:
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