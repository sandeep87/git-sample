package in.ccl.ui;

import in.ccl.adapters.FullPagerAdapter;
import in.ccl.helper.Category;
import in.ccl.helper.PageChangeListener;
import in.ccl.model.Items;
import in.ccl.util.Constants;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

public class PhotoActivity extends Activity {

	private ArrayList <Items> photoGalleryList;
	private int imagePosition;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_view);

		// get intent data
		if (getIntent().hasExtra(Constants.EXTRA_PHOTO_KEY)) {
			photoGalleryList = getIntent().getParcelableArrayListExtra(Constants.EXTRA_PHOTO_KEY);
		}
		if(getIntent().hasExtra(Constants.EXTRA_PHOTO_POSITION_ID)){
			imagePosition = getIntent().getIntExtra(Constants.EXTRA_PHOTO_POSITION_ID, 0);
		}
		ViewPager fullPhotoViewPager = (ViewPager) findViewById(R.id.photo_full_view_pager);
		fullPhotoViewPager.setAdapter(new FullPagerAdapter(this, photoGalleryList, Category.FULL_SCREEN,imagePosition));
		fullPhotoViewPager.setCurrentItem(getIntent().getIntExtra(Constants.EXTRA_PHOTO_POSITION_ID, 0));
		fullPhotoViewPager.setOnPageChangeListener(new PageChangeListener(null, fullPhotoViewPager,photoGalleryList.size()));
       
	}
	
	
}
