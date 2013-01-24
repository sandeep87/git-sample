package in.ccl.ui;

import in.ccl.adapters.ImagePagerAdapter;
import in.ccl.helper.Category;
import in.ccl.helper.PageChangeListener;
import in.ccl.helper.Util;
import in.ccl.model.Items;
import in.ccl.util.Constants;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeActivity extends TopActivity {

	private LinearLayout bannerPageIndicatorLayout;

	private LinearLayout photoPageIndicatorLayout;

	private LinearLayout videoPageIndicatorLayout;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// adding menu items to header of the screen, which is in MainActivity.
		addContent(R.layout.home);
		// for banner items, banner items always shows latest three items only.
		ViewPager bannerViewPager = (ViewPager) findViewById(R.id.banner_view_pager);
		ViewPager photoViewPager = (ViewPager) findViewById(R.id.photo_view_pager);
		ViewPager videoViewPager = (ViewPager) findViewById(R.id.video_view_pager);
		TextView txtBannerTitle = (TextView) findViewById(R.id.txt_banner_title);
		TextView txtPhotoTitle = (TextView) findViewById(R.id.txt_gallery_title);
		TextView txtVideoTitle = (TextView) findViewById(R.id.txt_video_title);
		// setting font style of the textviews
		Util.setTextFont(this, txtBannerTitle);
		Util.setTextFont(this, txtPhotoTitle);
		Util.setTextFont(this, txtVideoTitle);

		bannerPageIndicatorLayout = (LinearLayout) findViewById(R.id.banner_page_indicator_layout);
		photoPageIndicatorLayout = (LinearLayout) findViewById(R.id.photo_page_indicator_layout);
		videoPageIndicatorLayout = (LinearLayout) findViewById(R.id.video_page_indicator_layout);
		ArrayList <Items> bannerItemsList = null;
		ArrayList <Items> photoGalleryList = null;
		ArrayList <Items> videoGalleryList = null;

		if (getIntent().hasExtra(Constants.EXTRA_BANNER_KEY)) {
			bannerItemsList = getIntent().getParcelableArrayListExtra(Constants.EXTRA_BANNER_KEY);
			photoGalleryList = getIntent().getParcelableArrayListExtra(Constants.EXTRA_PHOTO_KEY);
			videoGalleryList = getIntent().getParcelableArrayListExtra(Constants.EXTRA_VIDEO_KEY);
		}
		if (bannerItemsList != null) {
			bannerViewPager.setAdapter(new ImagePagerAdapter(this, bannerItemsList, Category.BANNER));
			bannerViewPager.setOnClickListener(null);
			bannerViewPager.setOnPageChangeListener(new PageChangeListener(bannerPageIndicatorLayout, bannerViewPager));
		}
		// for photos
		if (photoGalleryList != null) {
			photoViewPager.setAdapter(new ImagePagerAdapter(this, photoGalleryList, Category.PHOTO));
			photoViewPager.setOnClickListener(null);
			photoViewPager.setOnPageChangeListener(new PageChangeListener(photoPageIndicatorLayout, photoViewPager));
		}
		// for loading videos in home screen
		if (videoGalleryList != null) {
			videoViewPager.setAdapter(new ImagePagerAdapter(this, videoGalleryList, Category.VIDEO));
			videoViewPager.setOnClickListener(null);
			videoViewPager.setOnPageChangeListener(new PageChangeListener(videoPageIndicatorLayout, videoViewPager));
		}
	}
}
