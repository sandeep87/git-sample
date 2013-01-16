package in.ccl.ui;

import in.ccl.adapters.GridAdapter;
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

	private ArrayList<Integer> photo_albums = new ArrayList<Integer>();

	private GridView gridView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContent(R.layout.grid_layout);
		int albumId = getIntent().getIntExtra(Constants.EXTRA_ALBUM_ID, -1);
		String albumTitle = getIntent().getStringExtra(
				Constants.EXTRA_ALBUM_TITLE);
		TextView txtAlbumTitle = (TextView) findViewById(R.id.txt_album_title);
		txtAlbumTitle.setText(albumTitle);
		TextView txtAlbumHeader = (TextView) findViewById(R.id.txt_album_header);
		txtAlbumHeader.setText(getResources().getString(R.string.photos));

		gridView = (GridView) findViewById(R.id.photos_gridview);

		gridView.setAdapter(new GridAdapter(PhotoAlbumActivity.this,
				addPhotoDummyData(), "photo"));

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				// Sending image id to FullScreenActivity
				Intent i = new Intent(getApplicationContext(),
						PhotoActivity.class);
				// passing array index
				i.putExtra("id", position);
				startActivity(i);
			}
		});

	}

	public static ArrayList<Items> addPhotoDummyData() {
		ArrayList<Items> photoGalleryList = new ArrayList<Items>();
		for (int i = 1; i < 20; i++) {
			Items photoGalleryItem = new Items();
			photoGalleryItem.setId(i);
			photoGalleryItem
					.setUrl("http://ccl.in/images/gallery/Telugu Warriors CCL Team Logo Launch/Telugu Warriors CCL Team Logo Launch ("
							+ i + ").jpg");
			photoGalleryList.add(photoGalleryItem);
		}
		return photoGalleryList;

	}
}
