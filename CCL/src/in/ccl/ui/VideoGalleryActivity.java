package in.ccl.ui;

import in.ccl.adapters.GridAdapter;
import in.ccl.helper.ServerResponse;
import in.ccl.helper.Util;
import in.ccl.model.Items;
import in.ccl.net.DownLoadAsynTask;
import in.ccl.parser.CCLParser;
import in.ccl.util.Constants;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class VideoGalleryActivity extends TopActivity implements ServerResponse {

	public static ArrayList <Integer> photo_albums = new ArrayList <Integer>();

	private GridView gridView;

	private ArrayList <Items> videoGalleryList;

	private String albumTitle;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContent(R.layout.grid_layout);

		if (getIntent().hasExtra(Constants.EXTRA_VIDEO_KEY)) {
			videoGalleryList = getIntent().getParcelableArrayListExtra(Constants.EXTRA_VIDEO_KEY);
		}

		TextView txtAlbumHeader = (TextView) findViewById(R.id.txt_album_header);
		txtAlbumHeader.setText(getResources().getString(R.string.videos));
		Util.setTextFont(this, txtAlbumHeader);
		TextView txtSeperator = (TextView) findViewById(R.id.divider);
		txtSeperator.setVisibility(View.GONE);

		gridView = (GridView) findViewById(R.id.photos_gridview);
		if (videoGalleryList != null) {
			gridView.setAdapter(new GridAdapter(VideoGalleryActivity.this, videoGalleryList, "video_gallery"));
		}
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick (AdapterView <?> arg0, View view, int position, long arg3) {

				if (Util.getInstance().isOnline(VideoGalleryActivity.this)) {
					DownLoadAsynTask asyncTask = new DownLoadAsynTask(VideoGalleryActivity.this, VideoGalleryActivity.this, false);
					asyncTask.execute(getResources().getString(R.string.video_gallery_url) + videoGalleryList.get(position).getId());
					albumTitle = videoGalleryList.get(position).getTitle();
				}
				else {
					Toast.makeText(VideoGalleryActivity.this, getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
				}

			}
		});
	}

	@Override
	public void setData (String result) {
		if (result != null) {
			Intent photoAlbumIntent = new Intent(getApplicationContext(), VideoAlbumActivity.class);
			photoAlbumIntent.putExtra(Constants.EXTRA_VIDEO_ITEMS, CCLParser.videoAlbumParser(result));
			photoAlbumIntent.putExtra(Constants.EXTRA_ALBUM_TITLE, albumTitle);
			startActivity(photoAlbumIntent);
		}
		else {
			Toast.makeText(VideoGalleryActivity.this, getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
		}

	}
}
