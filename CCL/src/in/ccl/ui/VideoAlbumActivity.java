package in.ccl.ui;

import in.ccl.adapters.GridAdapter;
import in.ccl.helper.Util;
import in.ccl.imageloader.EndlessScrollListener;
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
import android.widget.Toast;

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
		int videoGalleryId = getIntent().getIntExtra(Constants.EXTRA_ALBUM_ID, 1);

		TextView txtAlbumTitle = (TextView) findViewById(R.id.txt_album_title);
		txtAlbumTitle.setText(albumTitle);
		Util.setTextFont(this, txtAlbumTitle);
		TextView txtAlbumHeader = (TextView) findViewById(R.id.txt_album_header);
		txtAlbumHeader.setText(getResources().getString(R.string.videos));
		Util.setTextFont(this, txtAlbumHeader);

		gridView = (GridView) findViewById(R.id.photos_gridview);
		if (getIntent().hasExtra(Constants.EXTRA_VIDEO_ITEMS)) {
			videoAlbumList = getIntent().getParcelableArrayListExtra(Constants.EXTRA_VIDEO_ITEMS);
		}

		GridAdapter adapter = new GridAdapter(VideoAlbumActivity.this, videoAlbumList, "video");
		gridView.setAdapter(adapter);

		gridView.setOnScrollListener(new EndlessScrollListener(this, adapter, videoGalleryId, EndlessScrollListener.RequestType.VIDEO_REQUEST, videoAlbumList.get(0).getNumberOfPages()));

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick (AdapterView <?> arg0, View view, int position, long arg3) {
				String packageName = "com.google.android.youtube";
				boolean isYoutubeInstalled = isAppInstalled(packageName);
				if (isYoutubeInstalled) {
					String videoId = videoAlbumList.get(position).getVideoUrl();
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoId));
					intent.putExtra("VIDEO_ID", videoId);
					startActivity(intent);
				}
				else {
           Toast.makeText(VideoAlbumActivity.this,"Youtube application is not installed in your device.", Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	protected boolean isAppInstalled (String packageName) {
		Intent mIntent = getPackageManager().getLaunchIntentForPackage(packageName);
		if (mIntent != null) {
			return true;
		}
		else {
			return false;
		}
	}
}
