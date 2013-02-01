package in.ccl.ui;

import in.ccl.adapters.GridAdapter;
import in.ccl.database.CCLPullService;
import in.ccl.database.DataProviderContract;
import in.ccl.database.PhotoAlbumCurosr;
import in.ccl.database.VideoAlbumCursor;
import in.ccl.helper.Util;
import in.ccl.model.Items;
import in.ccl.util.Constants;

import java.util.ArrayList;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PhotoGalleryActivity extends TopActivity /* implements AbsListView.OnScrollListener */{

	private GridView gridView;

	private ArrayList <Items> photoGalleryList;

	private String AlbumTitle;

	private int photoGalleryId;

	// private boolean loading = true;

	private GridAdapter adapter;

	private ImageView imgFolder;

	public enum RequestType {
		NO_REQUEST, GALLERY_REQUEST, ALBUM_REQUEST;
	}

	private DownloadStateReceiver mDownloadStateReceiver;

	private IntentFilter statusIntentFilter;

	RequestType mRequestType = RequestType.NO_REQUEST;

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

		if (getIntent().hasExtra(Constants.EXTRA_PHOTO_KEY)) {
			photoGalleryList = getIntent().getParcelableArrayListExtra(Constants.EXTRA_PHOTO_KEY);
		}
		TextView txtAlbumHeader = (TextView) findViewById(R.id.txt_album_header);
		Util.setTextFont(this, txtAlbumHeader);
		txtAlbumHeader.setText(getResources().getString(R.string.photos_gallery));

		TextView txtSeperator = (TextView) findViewById(R.id.divider);
		txtSeperator.setVisibility(View.GONE);

		imgFolder = (ImageView) findViewById(R.id.img_folder);
		imgFolder.setImageResource(R.drawable.photos_folder);
		imgFolder.setVisibility(View.VISIBLE);
		gridView = (GridView) findViewById(R.id.photos_gridview);
		if (photoGalleryList != null) {
			adapter = new GridAdapter(PhotoGalleryActivity.this, photoGalleryList, "photo_gallery");
			gridView.setAdapter(adapter);
		}
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick (AdapterView <?> arg0, View view, int position, long arg3) {

				AlbumTitle = photoGalleryList.get(position).getTitle();
				photoGalleryId = photoGalleryList.get(position).getId();

				mRequestType = RequestType.GALLERY_REQUEST;
				ArrayList <Items> list = PhotoAlbumCurosr.getPhotos(PhotoGalleryActivity.this, photoGalleryId);
				if (list == null || list.size() <= 0) {
					if (Util.getInstance().isOnline(PhotoGalleryActivity.this)) {
						Intent mServiceIntent = new Intent(PhotoGalleryActivity.this, CCLPullService.class).setData(Uri.parse(getResources().getString(R.string.photo_gallery_url) + photoGalleryList.get(position).getId()));
						mServiceIntent.putExtra("KEY", "photos");
						startService(mServiceIntent);

						// asyncTask.execute(activity.getResources().getString(R.string.photo_gallery_url) + itemsList.get(index).getId());
					}
					else {
						Toast.makeText(PhotoGalleryActivity.this, getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
					}
				}
				else {
					Intent photoAlbumIntent = new Intent(PhotoGalleryActivity.this, PhotoAlbumActivity.class);
					photoAlbumIntent.putExtra(Constants.EXTRA_ALBUM_ITEMS, list);
					photoAlbumIntent.putExtra(Constants.EXTRA_ALBUM_ID, photoGalleryId);
					photoAlbumIntent.putExtra(Constants.EXTRA_ALBUM_TITLE, AlbumTitle);
					startActivity(photoAlbumIntent);
				}

			}
		});
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
					ArrayList <Items> list = PhotoAlbumCurosr.getPhotos(PhotoGalleryActivity.this, photoGalleryId);

					Intent photoAlbumIntent = new Intent(PhotoGalleryActivity.this, PhotoAlbumActivity.class);
					photoAlbumIntent.putExtra(Constants.EXTRA_ALBUM_ITEMS, list);
					photoAlbumIntent.putExtra(Constants.EXTRA_ALBUM_ID, photoGalleryId);
					photoAlbumIntent.putExtra(Constants.EXTRA_ALBUM_TITLE, AlbumTitle);
					startActivity(photoAlbumIntent);
					break;
				case in.ccl.database.Constants.STATE_ACTION_PHOTO_ALBUM_UPDATES_COMPLETE:
					Cursor cursor = getContentResolver().query(DataProviderContract.PHOTO_ALBUM_TABLE_CONTENTURI, null, null, null, null);
					if (cursor != null && cursor.getCount() > 0) {
						ArrayList <Items> photoAlbumItems = VideoAlbumCursor.getItems(cursor);
						if (photoAlbumItems.size() > 0) {
							gridView.setAdapter(new GridAdapter(PhotoGalleryActivity.this, photoAlbumItems, "photo_gallery"));
						}
						else {
							// display no items message.
						}
						cursor.close();
					}
					break;

				default:
					break;
			}
		}
	}

	@Override
	protected void onResume () {
		// TODO Auto-generated method stub
		super.onResume();
		LocalBroadcastManager.getInstance(this).registerReceiver(mDownloadStateReceiver, statusIntentFilter);
		if (Util.getInstance().isOnline(PhotoGalleryActivity.this)) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run () {
					Intent mServiceIntent = new Intent(PhotoGalleryActivity.this, CCLPullService.class).setData(Uri.parse(getResources().getString(R.string.photo_album_url)));
					mServiceIntent.putExtra("KEY", "update-photos");
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
}
