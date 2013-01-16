package in.ccl.ui;

import in.ccl.adapters.ImagePagerAdapter;
import in.ccl.helper.Category;
import in.ccl.helper.PageChangeListener;
import in.ccl.helper.Util;
import in.ccl.model.Items;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
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

		ArrayList <Items> bannerItemsList = new ArrayList <Items>();
		Items bannerItem = new Items();
		bannerItem.setId(1);
		bannerItem.setUrl("http://ccl.in/images/banner/Bipasha-Basu-as-brand-ambassador-for-CCL-season-3.jpg");
		bannerItem.setTitle("");
		bannerItemsList.add(bannerItem);

		bannerItem = new Items();
		bannerItem.setId(1);
		bannerItem.setUrl("http://ccl.in/images/banner/banner-slide-01.jpg");
		bannerItem.setTitle("");
		bannerItemsList.add(bannerItem);

		bannerItem = new Items();
		bannerItem.setId(1);
		bannerItem.setUrl("http://ccl.in/images/banner/banner-slide-04.jpg");
		bannerItem.setTitle("");
		bannerItemsList.add(bannerItem);
		bannerViewPager.setAdapter(new ImagePagerAdapter(this, bannerItemsList, Category.BANNER));
		bannerViewPager.setOnPageChangeListener(new PageChangeListener(bannerPageIndicatorLayout, bannerViewPager));

		// for photos
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

		photoViewPager.setAdapter(new ImagePagerAdapter(this, photoGalleryList, Category.PHOTO));
		photoViewPager.setOnPageChangeListener(new PageChangeListener(photoPageIndicatorLayout, photoViewPager));
    // for loading videos in home screen
		ArrayList <Items> videoGalleryList = new ArrayList <Items>();
		// TODO loading dummy data for show casing the application, should load dynamically these data .
		Items videoGalleryItem = new Items();
		videoGalleryItem.setId(1);
		videoGalleryItem.setTitle("Karnataka Bulldozers vs Chennai Rhinos Final Match");
		videoGalleryItem.setUrl("http://ccl.in/images/videos/final-match-thumb/cr-kb-fi-20.jpg");
		videoGalleryList.add(videoGalleryItem);
		videoGalleryItem = new Items();
		videoGalleryItem.setId(2);
		videoGalleryItem.setTitle("Chennai Rhinos vs Mumbai Heroes");
		videoGalleryItem.setUrl("http://ccl.in/images/videos/cr-mh-thumb/cr-mh-18.jpg");
		videoGalleryList.add(videoGalleryItem);
		videoGalleryItem = new Items();
		videoGalleryItem.setId(3);
		videoGalleryItem.setTitle("Karnataka Bulldozers vs Telugu Warriors");
		videoGalleryItem.setUrl("http://ccl.in/images/videos/kb-tw-thumb/kb-tw-20.jpg");
		videoGalleryList.add(videoGalleryItem);
		videoGalleryItem = new Items();
		videoGalleryItem.setId(4);
		videoGalleryItem.setTitle("Telugu Warriors vs Mumbai Heroes");
		videoGalleryItem.setUrl("http://ccl.in/images/videos/mh-tw-thumb/tw-mh-20.jpg");
		videoGalleryList.add(videoGalleryItem);
		videoGalleryItem = new Items();
		videoGalleryItem.setId(5);
		videoGalleryItem.setTitle("Karnataka Bulldozers vs Chennai Rhinos");
		videoGalleryItem.setUrl("http://ccl.in/images/videos/cr-kb-thumb/kb-cr-17.jpg");
		videoGalleryList.add(videoGalleryItem);
		videoGalleryItem = new Items();
		videoGalleryItem.setId(6);
		videoGalleryItem.setTitle("Chennai Rhinos vs Telugu Warriors");
		videoGalleryItem.setUrl("http://ccl.in/images/videos/cr-tw-thumb/tw-cr-10.jpg");
		videoGalleryList.add(videoGalleryItem);

		videoGalleryItem = new Items();
		videoGalleryItem.setId(7);
		videoGalleryItem.setTitle("Celebrity Cricket League Curtain Raiser Match");
		videoGalleryItem.setUrl("http://ccl.in/images/videos/ccl-curtain-raiser-thumb/curtain-raiser-stars-awards.jpg");
		videoGalleryList.add(videoGalleryItem);

		videoGalleryItem = new Items();
		videoGalleryItem.setId(8);
		videoGalleryItem.setTitle("CCL 2 Final : Karnatka Bulldozers vs Chennai Rhinos match at Hyderabad");
		videoGalleryItem.setUrl("http://ccl.in/images/gallery/CCL 2 Final Karnatka Bulldozers Vs Chennai Rhinos Match Photos thumb/CCL 2 Final Karnatka Bulldozers Vs Chennai Rhinos Match Photos (2).jpg");
		videoGalleryList.add(videoGalleryItem);
		videoGalleryItem = new Items();
		videoGalleryItem.setId(9);
		videoGalleryItem.setTitle("CCL 2 : Bengal Tigers vs Mumbai Heroes match at Vizag");
		videoGalleryItem.setUrl("http://ccl.in/images/gallery/CCL 2 Bengal Tigers vs Mumbai Heroes Match Photos thumb/CCL 2 Bengal Tigers vs Mumbai Heroes Match Photos (24).jpg");
		videoGalleryList.add(videoGalleryItem);

		videoViewPager.setAdapter(new ImagePagerAdapter(this, videoGalleryList, Category.VIDEO));
		videoViewPager.setOnPageChangeListener(new PageChangeListener(videoPageIndicatorLayout, videoViewPager));
	}
}
