package in.ccl.adapters;

import in.ccl.helper.Category;
import in.ccl.model.Items;
import in.ccl.photo.PhotoView;
import in.ccl.photo.ScaleImageView;
import in.ccl.ui.R;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FullPagerAdapter extends PagerAdapter {

	private LayoutInflater inflater;

	private ArrayList <Items> itemsList;

	private Activity activity;

	// private Category mCategory;

	@Override
	public void destroyItem (ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	public FullPagerAdapter (Activity ctx, ArrayList <Items> list, Category category) {
		activity = ctx;
		itemsList = list;
		// mCategory = category;
		inflater = activity.getLayoutInflater();
	}

	@Override
	public int getCount () {
		return itemsList.size();
	}

	@Override
	public View instantiateItem (View view, int position) {
		View imageLayout = null;
		ScaleImageView imageView = null;
		// ImageView spinner = null;

		imageLayout = inflater.inflate(R.layout.full_image, null);
		imageView = (ScaleImageView) imageLayout.findViewById(R.id.image);
		// spinner = (ImageView) imageLayout.findViewById(R.id.loading);
		imageView.setTag(itemsList.get(position).getPhotoOrVideoUrl());
		TextView errorTxt = (TextView) imageLayout.findViewById(R.id.error_title);
		// loadingImage = (ImageView) imageLayout.findViewById(R.id.loading);
    imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.blackbackground));
		imageView.setTag(itemsList.get(position).getPhotoOrVideoUrl());
		imageView.setImageURL(itemsList.get(position).getPhotoOrVideoUrl(), true, activity.getResources().getDrawable(R.drawable.blackbackground), errorTxt);

		((ViewPager) view).addView(imageLayout, 0);
		return imageLayout;
	}

	@Override
	public boolean isViewFromObject (View view, Object object) {
		return view.equals(object);

	}

}
