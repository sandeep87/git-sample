package in.ccl.adapters;

import in.ccl.helper.Util;
import in.ccl.imageloader.DisplayImage;
import in.ccl.model.Items;
import in.ccl.photo.PhotoView;
import in.ccl.ui.R;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {

	private Context mcontext;

	private ArrayList <Items> gridItemsList;

	private LayoutInflater inflater;

	private String isFrom;

	// A Drawable for a grid cell that's empty
	 private Drawable mEmptyDrawable;

	public GridAdapter (Context context, ArrayList <Items> listOfItems, String from) {
		mcontext = context;
		isFrom = from;
		gridItemsList = listOfItems;
		inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mEmptyDrawable = context.getResources().getDrawable(R.drawable.imagenotqueued);

	}

	@Override
	public int getCount () {

		return gridItemsList.size();
	}

	@Override
	public Object getItem (int position) {

		return gridItemsList.get(position);
	}

	@Override
	public long getItemId (int arg0) {

		return 0;
	}

	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		ViewHolder mViewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.gallery_item, null);
			mViewHolder = new ViewHolder();
			//mViewHolder.image = (ImageView) convertView.findViewById(R.id.image);
			mViewHolder.image = (PhotoView) convertView.findViewById(R.id.image);

			mViewHolder.playImage = (ImageView) convertView.findViewById(R.id.img_play_icon);
			mViewHolder.title = (TextView) convertView.findViewById(R.id.title);
			mViewHolder.errorTxt = (TextView) convertView.findViewById(R.id.error_title);
			mViewHolder.imageLoader = (ImageView) convertView.findViewById(R.id.loading);

			mViewHolder.image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			mViewHolder.image.setPadding(5, 5, 5, 5);
			convertView.setTag(mViewHolder);
		}
		else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}

//		Util.setTextFont((Activity) mcontext, mViewHolder.title);
		mViewHolder.image.setTag(gridItemsList.get(position).getPhotoOrVideoUrl());
		//DisplayImage displayImage = null;
		if (isFrom.equals("video")) {
			mViewHolder.image.setImageURL(gridItemsList.get(position).getThumbUrl(), true, this.mEmptyDrawable,mViewHolder.errorTxt);

		//	displayImage = new DisplayImage(gridItemsList.get(position).getThumbUrl(), mViewHolder.image, (Activity) mcontext, null);
		}
		else {
			mViewHolder.image.setImageURL(gridItemsList.get(position).getPhotoOrVideoUrl(), true, this.mEmptyDrawable,mViewHolder.errorTxt);

		//	displayImage = new DisplayImage(gridItemsList.get(position).getPhotoOrVideoUrl(), mViewHolder.image, (Activity) mcontext, null);
		}
		// displayImage.setErrorTitle(mViewHolder.errorTxt);

		if (isFrom.equals("video")) {
			mViewHolder.title.setVisibility(View.INVISIBLE);
			mViewHolder.playImage.setVisibility(View.VISIBLE);
		//	displayImage.setPlayIcon(mViewHolder.playImage);
		}
		else if (isFrom.equals("photo_gallery")) {
			mViewHolder.title.setText(gridItemsList.get(position).getTitle());
			mViewHolder.title.setVisibility(View.VISIBLE);
			mViewHolder.playImage.setVisibility(View.INVISIBLE);
		//	displayImage.setPlayIcon(null);
		}
		else if (isFrom.equals("video_gallery")) {
			mViewHolder.title.setText(gridItemsList.get(position).getTitle());
			mViewHolder.title.setVisibility(View.VISIBLE);
			mViewHolder.playImage.setVisibility(View.VISIBLE);
		//	displayImage.setPlayIcon(mViewHolder.playImage);
		}
		else if (isFrom.equals("team_member_image")) {
			Util.setTextFont((Activity) mcontext, mViewHolder.title);
			mViewHolder.title.setText(gridItemsList.get(position).getTitle() + "\n" + gridItemsList.get(position).getPersonRoles());
			mViewHolder.title.setVisibility(View.VISIBLE);
			mViewHolder.playImage.setVisibility(View.INVISIBLE);
		//	displayImage.setPlayIcon(null);
		}
		else {
			mViewHolder.title.setVisibility(View.INVISIBLE);
			mViewHolder.playImage.setVisibility(View.INVISIBLE);
		//	displayImage.setPlayIcon(null);
		}
	//	displayImage.show();
		return convertView;
	}

	public class ViewHolder {

		//public ImageView image;
		
		public PhotoView image;


		public ImageView playImage;

		public TextView title;

		public TextView errorTxt;

		public ImageView imageLoader;
	}

	public void updateList (ArrayList <Items> items) {
		for (Items items2 : items) {
			gridItemsList.add(items2);
		}
		notifyDataSetChanged();
	}
}