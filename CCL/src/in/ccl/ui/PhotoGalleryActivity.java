package in.ccl.ui;

import in.ccl.adapters.GridAdapter;
import in.ccl.helper.ServerResponse;
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

public class PhotoGalleryActivity extends TopActivity implements ServerResponse {

	private GridView gridView;

	private ArrayList <Items> photoGalleryList;

	private String AlbumTitle;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContent(R.layout.grid_layout);

		if (getIntent().hasExtra(Constants.EXTRA_PHOTO_KEY)) {
			photoGalleryList = getIntent().getParcelableArrayListExtra(Constants.EXTRA_PHOTO_KEY);
		}
		TextView txtAlbumHeader = (TextView) findViewById(R.id.txt_album_header);
		txtAlbumHeader.setText(getResources().getString(R.string.photos));

		TextView txtSeperator = (TextView) findViewById(R.id.divider);
		txtSeperator.setVisibility(View.GONE);

		gridView = (GridView) findViewById(R.id.photos_gridview);
		if (photoGalleryList != null) {
			gridView.setAdapter(new GridAdapter(PhotoGalleryActivity.this, photoGalleryList, "photo_gallery"));
		}
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick (AdapterView <?> arg0, View view, int position, long arg3) {
				DownLoadAsynTask asyncTask = new DownLoadAsynTask(PhotoGalleryActivity.this, PhotoGalleryActivity.this, false);
				asyncTask.execute(getResources().getString(R.string.photo_gallery_url) + photoGalleryList.get(position).getId());
				AlbumTitle = photoGalleryList.get(position).getTitle();
			}
		});
	}

	@Override
	public void setData (String result) {
		Intent photoAlbumIntent = new Intent(getApplicationContext(), PhotoAlbumActivity.class);
		photoAlbumIntent.putExtra(Constants.EXTRA_ALBUM_ITEMS, CCLParser.photoParser(result));
		photoAlbumIntent.putExtra(Constants.EXTRA_ALBUM_TITLE, AlbumTitle);
		startActivity(photoAlbumIntent);

	}
}
