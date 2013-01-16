package in.ccl.ui;

import in.ccl.adapters.GridAdapter;
import in.ccl.util.Constants;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

public class VideoGalleryActivity extends TopActivity {

	public static ArrayList <Integer> photo_albums = new ArrayList <Integer>();

	private GridView gridView;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContent(R.layout.grid_layout);
		int albumId = getIntent().getIntExtra(Constants.EXTRA_ALBUM_ID, -1);
		TextView txtAlbumHeader = (TextView) findViewById(R.id.txt_album_header);
		txtAlbumHeader.setText(getResources().getString(R.string.videos));

		TextView txtSeperator = (TextView) findViewById(R.id.divider);
		txtSeperator.setVisibility(View.GONE);

		gridView = (GridView) findViewById(R.id.photos_gridview);

		gridView.setAdapter(new GridAdapter(VideoGalleryActivity.this, PhotoGalleryActivity.addDummyData(), "video_gallery"));

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick (AdapterView <?> arg0, View view, int position, long arg3) {
				// Sending image id to FullScreenActivity
				Intent photoAlbumIntent = new Intent(getApplicationContext(), VideoAlbumActivity.class);
				photoAlbumIntent.putExtra(Constants.EXTRA_ALBUM_ID, 1);
				photoAlbumIntent.putExtra(Constants.EXTRA_ALBUM_TITLE, "Chennai Rhinos vs Mumbai Heroes");

				startActivity(photoAlbumIntent);
			}
		});

	}
}
