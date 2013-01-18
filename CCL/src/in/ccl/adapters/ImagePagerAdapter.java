package in.ccl.adapters;

import in.ccl.helper.Category;
import in.ccl.helper.ServerResponse;
import in.ccl.imageloader.DisplayImage;
import in.ccl.model.Items;
import in.ccl.net.CCLService;
import in.ccl.net.DownLoadAsynTask;
import in.ccl.ui.PhotoGalleryActivity;
import in.ccl.ui.R;
import in.ccl.ui.VideoGalleryActivity;
import in.ccl.util.Constants;

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
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;

public class ImagePagerAdapter extends PagerAdapter implements ServerResponse {

	private LayoutInflater inflater;

	private ArrayList <Items> itemsList;

	private Activity activity;

	private Category mCategory;

	private static final int VIEW_PAGER_PAGE_COUNT = 3;

	private enum RequestType {
		NO_REQUEST, PHOTOGALLERY_REQUEST, VIDEOGALLERY_REQUEST;
	}

	RequestType mRequestType = RequestType.NO_REQUEST;

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
				mRequestType = RequestType.NO_REQUEST;
				imageLayout = inflater.inflate(R.layout.item_pager_image, null);
				imageView = (ImageView) imageLayout.findViewById(R.id.image);
				imageView.setScaleType(ImageView.ScaleType.FIT_XY);
				spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
				System.out.println("BANNER " + itemsList.get(position).getUrl());
				imageView.setTag(itemsList.get(position).getUrl());
				DisplayImage displayImage = new DisplayImage(itemsList.get(position).getUrl(), imageView, activity, spinner);
				displayImage.show();
				break;
			case PHOTO:
				mRequestType = RequestType.PHOTOGALLERY_REQUEST;
				imageLayout = load("photo_gallery", position);
				break;
			case VIDEO:
				mRequestType = RequestType.VIDEOGALLERY_REQUEST;
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
		((ViewPager) view).addView(imageLayout, new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		return imageLayout;
	}

	private View load (final String from, int position) {
		View view = inflater.inflate(R.layout.grid_view, null);
		GridView gridView = (GridView) view.findViewById(R.id.grid_view);
		final ArrayList <Items> items = new ArrayList <Items>();
		items.add(itemsList.get(VIEW_PAGER_PAGE_COUNT * position));
		items.add(itemsList.get((VIEW_PAGER_PAGE_COUNT * position) + 1));
		items.add(itemsList.get((VIEW_PAGER_PAGE_COUNT * position) + 2));
		GridAdapter adapter = new GridAdapter(activity, items, from);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick (AdapterView <?> arg0, View arg1, int position, long arg3) {
				DownLoadAsynTask asyncTask = null;
				if (from.equals("video_gallery")) {
					asyncTask = new DownLoadAsynTask(activity, ImagePagerAdapter.this, false);
					asyncTask.execute(activity.getResources().getString(R.string.video_album_url));
				}
				else if (from.equals("photo_gallery")) {
					asyncTask = new DownLoadAsynTask(activity, ImagePagerAdapter.this, false);
					asyncTask.execute(activity.getResources().getString(R.string.photo_album_url));
				}
			}
		});
		return view;
	}

	@Override
	public boolean isViewFromObject (View view, Object object) {
		return view.equals(object);
	}

	@Override
	public void setData (String result) {
		switch (mRequestType) {
			case PHOTOGALLERY_REQUEST:
				Intent photoGalleryIntent = new Intent(activity, PhotoGalleryActivity.class);
				photoGalleryIntent.putParcelableArrayListExtra(Constants.EXTRA_PHOTO_KEY, CCLService.getPhotoAlbums(result));
				activity.startActivity(photoGalleryIntent);
				break;
			case VIDEOGALLERY_REQUEST:
				Intent videoGalleryIntent = new Intent(activity, VideoGalleryActivity.class);
				videoGalleryIntent.putParcelableArrayListExtra(Constants.EXTRA_VIDEO_KEY, CCLService.getVideoAlbums(result));
				activity.startActivity(videoGalleryIntent);
				break;

			default:
				break;
		}
	}
}
