package in.ccl.ui;

import in.ccl.adapters.GridAdapter;
import in.ccl.database.CCLPullService;
import in.ccl.database.PhotoAlbumCurosr;
import in.ccl.helper.Util;
import in.ccl.imageloader.EndlessScrollListener;
import in.ccl.model.Items;
import in.ccl.util.Constants;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

	private int photoGalleryId;

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
		photoGalleryId = getIntent().getIntExtra(Constants.EXTRA_ALBUM_ID, 1);
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

		if (photoGalleryList != null && photoGalleryList.size() > 0) {
			gridView.setOnScrollListener(new EndlessScrollListener(this, adapter, photoGalleryId, EndlessScrollListener.RequestType.PHOTO_GALLERY_REQUEST, photoGalleryList.get(0).getNumberOfPages()));
		}

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick (AdapterView <?> arg0, View view, int position, long arg3) {
				//arg0.getAdapter().getItem(position);
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
		if (Util.getInstance().isOnline(PhotoAlbumActivity.this)) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run () {
					Intent mServiceIntent = new Intent(PhotoAlbumActivity.this, CCLPullService.class).setData(Uri.parse(getResources().getString(R.string.photo_gallery_url) + photoGalleryId));
					mServiceIntent.putExtra("KEY", "photo_updates");
					startService(mServiceIntent);
				}
			}, 20000);
		}

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

				case in.ccl.database.Constants.STATE_ACTION_PHOTO_PAGES_DOWNLOAD_COMPLETE:
					if (intent != null && intent.hasExtra("list")) {
						ArrayList <Items> list = intent.getParcelableArrayListExtra("list");
						adapter.updateList(list);
					}
					break;
				case in.ccl.database.Constants.STATE_ACTION_PHOTO_UPDATES_COMPLETE:
					System.out.println("Loading data updates ");
					photoGalleryList = PhotoAlbumCurosr.getPhotos(PhotoAlbumActivity.this, photoGalleryId);
					adapter = new GridAdapter(PhotoAlbumActivity.this, photoGalleryList, "photo");
					gridView.setAdapter(adapter);
					if (photoGalleryList.size() > 0) {
						gridView.setOnScrollListener(new EndlessScrollListener(PhotoAlbumActivity.this, adapter, photoGalleryId, EndlessScrollListener.RequestType.VIDEO_REQUEST, photoGalleryList.get(0).getNumberOfPages()));
					}
					break;
				default:
					break;
			}
		}
	}

}