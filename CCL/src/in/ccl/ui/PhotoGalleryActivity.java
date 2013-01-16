package in.ccl.ui;

import in.ccl.adapters.GridAdapter;
import in.ccl.model.Items;
import in.ccl.util.Constants;

import java.util.ArrayList;
import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

public class PhotoGalleryActivity extends TopActivity {

	private GridView gridView;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContent(R.layout.grid_layout);
		int albumId = getIntent().getIntExtra(Constants.EXTRA_ALBUM_ID, -1);
		TextView txtAlbumHeader = (TextView) findViewById(R.id.txt_album_header);
		txtAlbumHeader.setText(getResources().getString(R.string.photos));

		TextView txtSeperator = (TextView) findViewById(R.id.divider);
		txtSeperator.setVisibility(View.GONE);

		gridView = (GridView) findViewById(R.id.photos_gridview);

		gridView.setAdapter(new GridAdapter(PhotoGalleryActivity.this, addDummyData(), "photo_gallery"));

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick (AdapterView <?> arg0, View view, int position, long arg3) {
				// Sending image id to FullScreenActivity
				Intent photoAlbumIntent = new Intent(getApplicationContext(), PhotoAlbumActivity.class);
				photoAlbumIntent.putExtra(Constants.EXTRA_ALBUM_ID, 1);
				photoAlbumIntent.putExtra(Constants.EXTRA_ALBUM_TITLE, "Chennai Rhinos vs Mumbai Heroes");

				startActivity(photoAlbumIntent);
			}
		});

	}

	public static ArrayList <Items> addDummyData () {
		ArrayList <Items> photoGalleryList = new ArrayList <Items>();
		Items photoGalleryItem = new Items();
		photoGalleryItem.setId(1);
		photoGalleryItem.setTitle("Bengaluru CCL Team Launch");
		photoGalleryItem.setUrl("http://ccl.in/images/gallery/bengaluru-thumb/bengaluru-ccl-1 (69).jpg");
		photoGalleryList.add(photoGalleryItem);
		photoGalleryItem = new Items();
		photoGalleryItem.setId(2);
		photoGalleryItem.setTitle("Chennai CCL Team Launch");
		photoGalleryItem.setUrl("http://ccl.in/images/gallery/chennai-thumb/chennai-ccl-1 (44).jpg");
		photoGalleryList.add(photoGalleryItem);
		photoGalleryItem = new Items();
		photoGalleryItem.setId(3);
		photoGalleryItem.setTitle("CCL Mumbai Heroes Cricket Team Launch");
		photoGalleryItem.setUrl("http://ccl.in/images/gallery/mumbai-thumb/mumbai-ccl-1 (82).jpg");
		photoGalleryList.add(photoGalleryItem);
		photoGalleryItem = new Items();
		photoGalleryItem.setId(4);
		photoGalleryItem.setTitle("Star Actresses at CCL");
		photoGalleryItem.setUrl("http://ccl.in/images/gallery/Star Actresses at CCL thumb/Star Actresses at CCL (1).jpg");
		photoGalleryList.add(photoGalleryItem);
		photoGalleryItem = new Items();
		photoGalleryItem.setId(5);
		photoGalleryItem.setTitle("Telugu Warriors vs Mumbai Heroes match photos");
		photoGalleryItem.setUrl("http://ccl.in/images/gallery/Bengal Tigers CCL Team Launch thumb/Bengal Tigers CCL Team Launch (1).jpg");
		photoGalleryList.add(photoGalleryItem);
		photoGalleryItem = new Items();
		photoGalleryItem.setId(6);
		photoGalleryItem.setTitle("Bengaluru CCL Team Launch");
		photoGalleryItem.setUrl("http://ccl.in/images/gallery/bengaluru-thumb/bengaluru-ccl-1 (69).jpg");
		photoGalleryList.add(photoGalleryItem);

		photoGalleryItem = new Items();
		photoGalleryItem.setId(7);
		photoGalleryItem.setTitle("Chennai Rhinos Team Launch");
		photoGalleryItem.setUrl("http://ccl.in/images/gallery/chennai-rhinos-team-launch-thumb/chennai-rhinos-team-launch (17).jpg");
		photoGalleryList.add(photoGalleryItem);

		photoGalleryItem = new Items();
		photoGalleryItem.setId(8);
		photoGalleryItem.setTitle("Bengaluru CCL Team Launch");
		photoGalleryItem.setUrl("http://ccl.in/images/gallery/bengaluru-thumb/bengaluru-ccl-1 (69).jpg");
		photoGalleryList.add(photoGalleryItem);
		photoGalleryItem = new Items();
		photoGalleryItem.setId(9);
		photoGalleryItem.setTitle("Telugu Warriors vs Mumbai Heroes match photos");
		photoGalleryItem.setUrl("http://ccl.in/images/gallery/Bengal Tigers CCL Team Launch thumb/Bengal Tigers CCL Team Launch (1).jpg");
		photoGalleryList.add(photoGalleryItem);
		photoGalleryItem = new Items();
		photoGalleryItem.setId(1);
		photoGalleryItem.setTitle("Bengaluru CCL Team Launch");
		photoGalleryItem.setUrl("http://ccl.in/images/gallery/bengaluru-thumb/bengaluru-ccl-1 (69).jpg");
		photoGalleryList.add(photoGalleryItem);
		photoGalleryItem = new Items();
		photoGalleryItem.setId(2);
		photoGalleryItem.setTitle("Chennai CCL Team Launch");
		photoGalleryItem.setUrl("http://ccl.in/images/gallery/chennai-thumb/chennai-ccl-1 (44).jpg");
		photoGalleryList.add(photoGalleryItem);
		photoGalleryItem = new Items();
		photoGalleryItem.setId(3);
		photoGalleryItem.setTitle("CCL Mumbai Heroes Cricket Team Launch");
		photoGalleryItem.setUrl("http://ccl.in/images/gallery/mumbai-thumb/mumbai-ccl-1 (82).jpg");
		photoGalleryList.add(photoGalleryItem);
		photoGalleryItem = new Items();
		photoGalleryItem.setId(4);
		photoGalleryItem.setTitle("Star Actresses at CCL");
		photoGalleryItem.setUrl("http://ccl.in/images/gallery/Star Actresses at CCL thumb/Star Actresses at CCL (1).jpg");
		photoGalleryList.add(photoGalleryItem);
		photoGalleryItem = new Items();
		photoGalleryItem.setId(5);
		photoGalleryItem.setTitle("Telugu Warriors vs Mumbai Heroes match photos");
		photoGalleryItem.setUrl("http://ccl.in/images/gallery/Bengal Tigers CCL Team Launch thumb/Bengal Tigers CCL Team Launch (1).jpg");
		photoGalleryList.add(photoGalleryItem);
		photoGalleryItem = new Items();
		photoGalleryItem.setId(6);
		photoGalleryItem.setTitle("Bengaluru CCL Team Launch");
		photoGalleryItem.setUrl("http://ccl.in/images/gallery/bengaluru-thumb/bengaluru-ccl-1 (69).jpg");
		photoGalleryList.add(photoGalleryItem);
        
		photoGalleryItem = new Items();
		photoGalleryItem.setId(7);
		photoGalleryItem.setTitle("Chennai Rhinos Team Launch");
		photoGalleryItem.setUrl("http://ccl.in/images/gallery/chennai-rhinos-team-launch-thumb/chennai-rhinos-team-launch (17).jpg");
		photoGalleryList.add(photoGalleryItem);

		photoGalleryItem = new Items();
		photoGalleryItem.setId(8);
		photoGalleryItem.setTitle("Bengaluru CCL Team Launch");
		photoGalleryItem.setUrl("http://ccl.in/images/gallery/bengaluru-thumb/bengaluru-ccl-1 (69).jpg");
		photoGalleryList.add(photoGalleryItem);
		photoGalleryItem = new Items();
		photoGalleryItem.setId(9);
		photoGalleryItem.setTitle("Telugu Warriors vs Mumbai Heroes match photos");
		photoGalleryItem.setUrl("http://ccl.in/images/gallery/Bengal Tigers CCL Team Launch thumb/Bengal Tigers CCL Team Launch (1).jpg");
		photoGalleryList.add(photoGalleryItem);
		return photoGalleryList;

	}
}
