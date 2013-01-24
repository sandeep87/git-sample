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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PhotoGalleryActivity extends TopActivity implements ServerResponse, AbsListView.OnScrollListener {

	private GridView gridView;

	private ArrayList <Items> photoGalleryList;

	private String AlbumTitle;

	private int photoGalleryId;

	private int visibleThreshold = 5;

	private int currentPage = 0;

	private int previousTotal = 0;

	private boolean loading = true;

	private GridAdapter adapter;

	private ImageView imgFolder;
	
	public enum RequestType {
		NO_REQUEST, GALLERY_REQUEST, ALBUM_REQUEST;
	}

	RequestType mRequestType = RequestType.NO_REQUEST;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContent(R.layout.grid_layout);

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
		gridView.setOnScrollListener(this);
		if (photoGalleryList != null) {
			adapter = new GridAdapter(PhotoGalleryActivity.this, photoGalleryList, "photo_gallery");
			gridView.setAdapter(adapter);
		}
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick (AdapterView <?> arg0, View view, int position, long arg3) {
				mRequestType = RequestType.GALLERY_REQUEST;
				 if(Util.getInstance().isOnline(PhotoGalleryActivity.this)){
						DownLoadAsynTask asyncTask = new DownLoadAsynTask(PhotoGalleryActivity.this, PhotoGalleryActivity.this, false);
						asyncTask.execute(getResources().getString(R.string.photo_gallery_url) + photoGalleryList.get(position).getId());
					}else{
						Toast.makeText(PhotoGalleryActivity.this, getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
					}
			
				AlbumTitle = photoGalleryList.get(position).getTitle();
				photoGalleryId = photoGalleryList.get(position).getId();
			}
		});
	}

	@Override
	public void setData (String result) {
		if(result != null){
			if (mRequestType == RequestType.GALLERY_REQUEST) {
				Intent photoAlbumIntent = new Intent(getApplicationContext(), PhotoAlbumActivity.class);
				photoAlbumIntent.putExtra(Constants.EXTRA_ALBUM_ITEMS, CCLParser.photoParser(result));
				photoAlbumIntent.putExtra(Constants.EXTRA_ALBUM_ID, photoGalleryId);
				photoAlbumIntent.putExtra(Constants.EXTRA_ALBUM_TITLE, AlbumTitle);
				startActivity(photoAlbumIntent);
			}
			else if (mRequestType == RequestType.ALBUM_REQUEST) {

			}
		}else{
			Toast.makeText(PhotoGalleryActivity.this, getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();

		}
	

	}

	@Override
	public void onScroll (AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (loading) {
			if (totalItemCount > previousTotal) {
				loading = false;
				previousTotal = totalItemCount;
				currentPage++;
			}
		}
		if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
			System.out.println("Send server request and update it " + (currentPage + 1));
			if(Util.getInstance().isOnline(PhotoGalleryActivity.this)){
				DownLoadAsynTask asyncTask = new DownLoadAsynTask(PhotoGalleryActivity.this, this, false);
				asyncTask.execute(getResources().getString(R.string.photo_album_url));
				loading = true;
			}else{
				Toast.makeText(PhotoGalleryActivity.this, getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
			}
		
		}
	}

	@Override
	public void onScrollStateChanged (AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}
}
