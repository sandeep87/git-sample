package in.ccl.adapters;

import in.ccl.helper.Util;
import in.ccl.imageloader.DisplayImage;
import in.ccl.model.Items;
import in.ccl.ui.R;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {

	private Context mcontext;

	private ArrayList <Items> gridItemsList;

	private LayoutInflater inflater;

	private String isFrom;

	public GridAdapter (Context context, ArrayList <Items> listOfItems, String from) {
		mcontext = context;
		isFrom = from;
		gridItemsList = listOfItems;
		inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount () {
		// TODO Auto-generated method stub
		return gridItemsList.size();
	}

	@Override
	public Object getItem (int position) {
		// TODO Auto-generated method stub
		return gridItemsList.get(position);
	}

	@Override
	public long getItemId (int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView (int position, View convertView, ViewGroup arg2) {
		ViewHolder mViewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.gallery_item, null);
			mViewHolder = new ViewHolder();
			mViewHolder.image = (ImageView) convertView.findViewById(R.id.image);
			mViewHolder.playImage = (ImageView) convertView.findViewById(R.id.img_play_icon);
			mViewHolder.title = (TextView) convertView.findViewById(R.id.title);
			mViewHolder.errorTxt = (TextView) convertView.findViewById(R.id.error_title);

			mViewHolder.spinner = (ProgressBar) convertView.findViewById(R.id.loading);
			mViewHolder.image.setLayoutParams(new RelativeLayout.LayoutParams(143, 141));
			mViewHolder.image.setScaleType(ImageView.ScaleType.FIT_XY);
			mViewHolder.image.setPadding(5, 5, 5, 5);
			convertView.setTag(mViewHolder);
		}
		else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		Util.setTextFont((Activity) mcontext, mViewHolder.title);
		mViewHolder.image.setTag(gridItemsList.get(position).getUrl());
		
		DisplayImage displayImage = new DisplayImage(gridItemsList.get(position).getUrl(), mViewHolder.image, (Activity) mcontext, mViewHolder.spinner);
	  displayImage.setErrorTitle(mViewHolder.errorTxt);
		if (isFrom.equals("video")) {
			mViewHolder.title.setVisibility(View.INVISIBLE);
			displayImage.setPlayIcon(mViewHolder.playImage);
		}
		else if (isFrom.equals("photo_gallery")) {
			mViewHolder.title.setText(gridItemsList.get(position).getTitle());
			mViewHolder.title.setVisibility(View.VISIBLE);
			mViewHolder.playImage.setVisibility(View.INVISIBLE);
			displayImage.setPlayIcon(null);
		}
		else if (isFrom.equals("video_gallery")) {
			mViewHolder.title.setText(gridItemsList.get(position).getTitle());
			mViewHolder.title.setVisibility(View.VISIBLE);
			displayImage.setPlayIcon(mViewHolder.playImage);
		}
		else {
			mViewHolder.title.setVisibility(View.INVISIBLE);
			mViewHolder.playImage.setVisibility(View.INVISIBLE);
			displayImage.setPlayIcon(null);
		}
		displayImage.show();

		return convertView;
	}

	public class ViewHolder {

		public ImageView image;

		public ImageView playImage;

		public TextView title;

		public TextView errorTxt;

		public ProgressBar spinner;
	}
}