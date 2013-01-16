package in.ccl.ui;

import java.util.ArrayList;

import in.ccl.adapters.FullPagerAdapter;
import in.ccl.adapters.ImagePagerAdapter;
import in.ccl.helper.Category;
import in.ccl.helper.PageChangeListener;
import in.ccl.model.Items;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class PhotoActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_view);

		// get intent data
		Intent i = getIntent();
		// Selected image id
		int position = i.getExtras().getInt("id");
		ViewPager fullPhotoViewPager = (ViewPager) findViewById(R.id.photo_full_view_pager);
		fullPhotoViewPager.setAdapter(new FullPagerAdapter(this,
				addPhotoDummyData(), Category.FULL_SCREEN));
		fullPhotoViewPager.setCurrentItem(position);
		fullPhotoViewPager.setOnPageChangeListener(new PageChangeListener(null,
				fullPhotoViewPager));

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
