package in.ccl.ui;

import in.ccl.adapters.GridAdapter;
import in.ccl.model.Items;
import in.ccl.util.Constants;

import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

public class VideoAlbumActivity extends TopActivity {

	ArrayList <Integer> photo_albums = new ArrayList <Integer>();

	private GridView gridView;
	private ArrayList <Items> videoAlbumList;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContent(R.layout.grid_layout);
		String albumTitle = getIntent().getStringExtra(Constants.EXTRA_ALBUM_TITLE);
		
		TextView txtAlbumTitle = (TextView) findViewById(R.id.txt_album_title);
		txtAlbumTitle.setText(albumTitle);
		TextView txtAlbumHeader = (TextView) findViewById(R.id.txt_album_header);
		txtAlbumHeader.setText(getResources().getString(R.string.videos));

	
		gridView = (GridView) findViewById(R.id.photos_gridview);
		if (getIntent().hasExtra(Constants.EXTRA_VIDEO_ITEMS)) {
			videoAlbumList = getIntent().getParcelableArrayListExtra(Constants.EXTRA_VIDEO_ITEMS);
		}

		gridView.setAdapter(new GridAdapter(VideoAlbumActivity.this,videoAlbumList, "video"));

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick (AdapterView <?> arg0, View view, int position, long arg3) {
				String videoId = videoAlbumList.get(position).getVideoUrl();
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
				intent.putExtra("VIDEO_ID", videoId);
				startActivity(intent);
			}
		});

	}
}
