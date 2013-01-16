package in.ccl.adapters;

import in.ccl.helper.Category;
import in.ccl.imageloader.DisplayImage;
import in.ccl.model.Items;
import in.ccl.ui.HomeActivity;
import in.ccl.ui.PhotoGalleryActivity;
import in.ccl.ui.R;
import in.ccl.ui.VideoGalleryActivity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class ImagePagerAdapter extends PagerAdapter {

	private LayoutInflater inflater;

	private ArrayList <Items> itemsList;

	private Activity activity;

	private Category mCategory;

	private static final int VIEW_PAGER_PAGE_COUNT = 3;

	@Override
	public void destroyItem (ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	public ImagePagerAdapter (Activity ctx, ArrayList <Items> list, Category category) {
		activity = ctx;
		itemsList = list;
		mCategory = category;
		inflater = activity.getLayoutInflater();
	}

	@Override
	public int getCount () {
		return VIEW_PAGER_PAGE_COUNT;
	}

	@Override
	public View instantiateItem (View view, int position) {
		View imageLayout = null;
		ImageView imageView = null;
		ProgressBar spinner = null;

		switch (mCategory) {
		
			case BANNER:
				imageLayout = inflater.inflate(R.layout.item_pager_image, null);
				imageView = (ImageView) imageLayout.findViewById(R.id.image);
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
				imageView.setTag(itemsList.get(position).getUrl());
				DisplayImage displayImage = new DisplayImage(itemsList.get(position).getUrl(), imageView, activity, spinner);
				displayImage.show();
				break;
			case PHOTO:
				imageLayout = load("photo_gallery", position);
				break;
			case VIDEO:
				imageLayout = load("video_gallery", position);
				break;
			case FULL_SCREEN:
				imageLayout = inflater.inflate(R.layout.item_pager_image, null);
				imageView = (ImageView) imageLayout.findViewById(R.id.image);
				spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
				imageView.setTag(itemsList.get(position).getUrl());
				displayImage = new DisplayImage(itemsList.get(position).getUrl(), imageView, activity, spinner);
				displayImage.show();
				break;
			default:
				break;
		}
		((ViewPager) view).addView(imageLayout, 0);
		return imageLayout;
	}

	private View load (final String from, int position) {
		GridView gridView = (GridView) inflater.inflate(R.layout.grid_view, null);
		ArrayList <Items> items = new ArrayList <Items>();
		items.add(itemsList.get(VIEW_PAGER_PAGE_COUNT * position));
		items.add(itemsList.get((VIEW_PAGER_PAGE_COUNT * position) + 1));
		items.add(itemsList.get((VIEW_PAGER_PAGE_COUNT * position) + 2));
		GridAdapter adapter = new GridAdapter(activity, items, from);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick (AdapterView <?> arg0, View arg1, int arg2, long arg3) {
				if (from.equals("video_gallery")) {
					Intent photoGalleryIntent = new Intent(activity, VideoGalleryActivity.class);
					activity.startActivity(photoGalleryIntent);
				}
				else if (from.equals("photo_gallery")) {
					Intent photoGalleryIntent = new Intent(activity, PhotoGalleryActivity.class);
					activity.startActivity(photoGalleryIntent);
				}
			}
		});
		return gridView;
	}

	@Override
	public boolean isViewFromObject (View view, Object object) {
		return view.equals(object);

	}

}
