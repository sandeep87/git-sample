package in.ccl.adapters;

import in.ccl.helper.Category;
import in.ccl.imageloader.DisplayImage;
import in.ccl.model.Items;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.AdapterView.OnItemClickListener;

public class FullPagerAdapter extends PagerAdapter {

	private LayoutInflater inflater;

	private ArrayList<Items> itemsList;

	private Activity activity;

	private Category mCategory;

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	public FullPagerAdapter(Activity ctx, ArrayList<Items> list,
			Category category) {
		activity = ctx;
		itemsList = list;
		mCategory = category;
		inflater = activity.getLayoutInflater();
	}

	@Override
	public int getCount() {
		return itemsList.size();
	}

	@Override
	public View instantiateItem(View view, int position) {
		View imageLayout = null;
		ImageView imageView = null;
		ProgressBar spinner = null;

		imageLayout = inflater.inflate(R.layout.item_pager_image, null);
		imageView = (ImageView) imageLayout.findViewById(R.id.image);
		spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
		imageView.setTag(itemsList.get(position).getUrl());
		DisplayImage displayImage = new DisplayImage(itemsList.get(position)
				.getUrl(), imageView, activity, spinner);
		displayImage.show();

		((ViewPager) view).addView(imageLayout, 0);
		return imageLayout;
	}
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view.equals(object);

	}

}
