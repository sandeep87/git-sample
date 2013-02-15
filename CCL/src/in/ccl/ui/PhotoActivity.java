package in.ccl.ui;

import in.ccl.adapters.FullPagerAdapter;
import in.ccl.model.Items;
import in.ccl.util.Constants;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class PhotoActivity extends Activity {

	private ArrayList <Items> photoGalleryList;

	private int previousState;

	private int currentState;

	private static int currentPosition;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_view);

		// get intent data
		if (getIntent().hasExtra(Constants.EXTRA_PHOTO_KEY)) {
			photoGalleryList = getIntent().getParcelableArrayListExtra(Constants.EXTRA_PHOTO_KEY);
		}
		final ViewPager fullPhotoViewPager = (ViewPager) findViewById(R.id.photo_full_view_pager);

		fullPhotoViewPager.setAdapter(new FullPagerAdapter(this, photoGalleryList));
		currentPosition = getIntent().getIntExtra(Constants.EXTRA_PHOTO_POSITION_ID, 0);
		fullPhotoViewPager.setCurrentItem(currentPosition);
		fullPhotoViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected (int position) {
				currentPosition = position;
			}

			@Override
			public void onPageScrolled (int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged (int state) {
				int currentPage = fullPhotoViewPager.getCurrentItem();

				if (currentPage == photoGalleryList.size() - 1 || currentPage == 0) {
					previousState = currentState;
					currentState = state;
					if (previousState == 1 && currentState == 0) {
						fullPhotoViewPager.setCurrentItem(currentPage == 0 ? photoGalleryList.size() : 0);
					}
				}

			}
		});
	}

}
