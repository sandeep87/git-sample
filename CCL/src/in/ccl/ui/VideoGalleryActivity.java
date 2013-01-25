package in.ccl.ui;

import in.ccl.adapters.GridAdapter;
import in.ccl.database.CCLPullService;
import in.ccl.database.VideoAlbumCursor;
import in.ccl.helper.ServerResponse;
import in.ccl.helper.Util;
import in.ccl.model.Items;
import in.ccl.util.Constants;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class VideoGalleryActivity extends TopActivity {

	public static ArrayList <Integer> photo_albums = new ArrayList <Integer>();

	private GridView gridView;

	private ArrayList <Items> videoGalleryList;

	private String albumTitle;

	private ImageView imgFolder;

	private int videoGalleryId;

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

		if (getIntent().hasExtra(Constants.EXTRA_VIDEO_KEY)) {
			videoGalleryList = getIntent().getParcelableArrayListExtra(Constants.EXTRA_VIDEO_KEY);
		}

		TextView txtAlbumHeader = (TextView) findViewById(R.id.txt_album_header);
		txtAlbumHeader.setText(getResources().getString(R.string.videos_gallery));
		Util.setTextFont(this, txtAlbumHeader);
		TextView txtSeperator = (TextView) findViewById(R.id.divider);
		txtSeperator.setVisibility(View.GONE);

		imgFolder = (ImageView) findViewById(R.id.img_folder);
		imgFolder.setImageResource(R.drawable.videos_folder);
		imgFolder.setVisibility(View.VISIBLE);
		gridView = (GridView) findViewById(R.id.photos_gridview);
		if (videoGalleryList != null) {
			gridView.setAdapter(new GridAdapter(VideoGalleryActivity.this, videoGalleryList, "video_gallery"));
		}
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick (AdapterView <?> arg0, View view, int position, long arg3) {

				albumTitle = videoGalleryList.get(position).getTitle();
				videoGalleryId = videoGalleryList.get(position).getId();
				ArrayList <Items> list = VideoAlbumCursor.getVideos(VideoGalleryActivity.this, videoGalleryId);
				if (list == null || list.size() <= 0) {
					if (Util.getInstance().isOnline(VideoGalleryActivity.this)) {
						Intent mServiceIntent = new Intent(VideoGalleryActivity.this, CCLPullService.class).setData(Uri.parse(getResources().getString(R.string.video_gallery_url) + videoGalleryList.get(position).getId()));
						mServiceIntent.putExtra("KEY", "videos");
						startService(mServiceIntent);
					}
					else {
						Toast.makeText(VideoGalleryActivity.this, getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
					}
				}
				else {
					Intent photoAlbumIntent = new Intent(VideoGalleryActivity.this, VideoAlbumActivity.class);
					photoAlbumIntent.putExtra(Constants.EXTRA_VIDEO_ITEMS, list);
					photoAlbumIntent.putExtra(Constants.EXTRA_ALBUM_ID, videoGalleryId);
					photoAlbumIntent.putExtra(Constants.EXTRA_ALBUM_TITLE, albumTitle);
					startActivity(photoAlbumIntent);
				}

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
					ArrayList <Items> list = VideoAlbumCursor.getVideos(VideoGalleryActivity.this, videoGalleryId);
					Intent photoAlbumIntent = new Intent(VideoGalleryActivity.this, VideoAlbumActivity.class);
					photoAlbumIntent.putExtra(Constants.EXTRA_VIDEO_ITEMS, list);
					photoAlbumIntent.putExtra(Constants.EXTRA_ALBUM_ID, videoGalleryId);
					photoAlbumIntent.putExtra(Constants.EXTRA_ALBUM_TITLE, albumTitle);
					startActivity(photoAlbumIntent);

					break;
				default:
					break;
			}
		}
	}

}
