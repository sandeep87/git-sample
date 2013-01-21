package in.ccl.ui;

import in.ccl.adapters.GridAdapter;
import in.ccl.helper.Util;
import in.ccl.imageloader.EndlessScrollListener;
import in.ccl.model.Items;
import in.ccl.util.Constants;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

public class PhotoAlbumActivity extends TopActivity {

	private GridView gridView;

	private ArrayList <Items> photoGalleryList;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContent(R.layout.grid_layout);
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
		GridAdapter adapter = new GridAdapter(PhotoAlbumActivity.this, photoGalleryList, "photo");
		gridView.setAdapter(adapter);

		gridView.setOnScrollListener(new EndlessScrollListener(this, adapter, photoGalleryId, EndlessScrollListener.RequestType.PHOTO_GALLERY_REQUEST, photoGalleryList.get(0).getNumberOfPages()));

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
}
