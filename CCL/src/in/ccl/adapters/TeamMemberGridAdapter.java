package in.ccl.adapters;

import in.ccl.helper.Util;
import in.ccl.model.TeamMember;
import in.ccl.photo.PhotoView;
import in.ccl.ui.R;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TeamMemberGridAdapter extends BaseAdapter {

	private Context mContext;

	private ArrayList<TeamMember> gridItemsList;

	private LayoutInflater inflater;

	private String isFrom;
	private int reqImageHeight;
	// A Drawable for a grid cell that's empty
	private Drawable mEmptyDrawable;

	public TeamMemberGridAdapter(Context context,
			ArrayList<TeamMember> listOfItems, String from) {
		mContext = context;
		isFrom = from;
		gridItemsList = listOfItems;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mEmptyDrawable = context.getResources().getDrawable(
				R.drawable.imagenotqueued);
		int deviceDisplayDensity = ((Activity) mContext).getResources()
				.getDisplayMetrics().densityDpi;

		if (isFrom.equals("team_members")) {

			Display display = ((Activity) context).getWindowManager()
					.getDefaultDisplay();
			if (deviceDisplayDensity == DisplayMetrics.DENSITY_LOW) {

				int height = display.getHeight();
				reqImageHeight = (int) (((float) 16/ 100) * (height - 50));

			} else if (deviceDisplayDensity == DisplayMetrics.DENSITY_MEDIUM) {
				int height = display.getHeight();
				reqImageHeight = (int) (((float) 19 / 100) * (height - 50));

			} else if (deviceDisplayDensity == DisplayMetrics.DENSITY_HIGH) {
				int height = display.getHeight();
				reqImageHeight = (int) (((float) 19 / 100) * (height - 50));

			} else if (deviceDisplayDensity == DisplayMetrics.DENSITY_XHIGH) {
				int height = display.getHeight();
				reqImageHeight = (int) (((float) 19 / 100) * (height - 50));

			}
		} else {
			Display display = ((Activity) context).getWindowManager()
					.getDefaultDisplay();
			if (deviceDisplayDensity == DisplayMetrics.DENSITY_LOW) {

				int height = display.getHeight();
				reqImageHeight = (int) (((float) 17 / 100) * (height - 50));

			} else if (deviceDisplayDensity == DisplayMetrics.DENSITY_MEDIUM) {
				int height = display.getHeight();
				reqImageHeight = (int) (((float) 19 / 100) * (height - 50));

			} else if (deviceDisplayDensity == DisplayMetrics.DENSITY_HIGH) {
				int height = display.getHeight();
				reqImageHeight = (int) (((float) 19 / 100) * (height - 50));

			} else if (deviceDisplayDensity == DisplayMetrics.DENSITY_XHIGH) {
				int height = display.getHeight();
				reqImageHeight = (int) (((float) 19 / 100) * (height - 50));

			}
		}
	}

	@Override
	public int getCount() {

		return gridItemsList.size();
	}

	@Override
	public Object getItem(int position) {

		return gridItemsList.get(position);
	}

	@Override
	public long getItemId(int arg0) {

		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mViewHolder;
		if (convertView == null) {
			if (isFrom.equals("team_members")) {
				convertView = inflater.inflate(R.layout.team_item, null);
				mViewHolder = new ViewHolder();
				// mViewHolder.image = (ImageView)
				// convertView.findViewById(R.id.image);
				mViewHolder.imgTeamMember = (PhotoView) convertView
						.findViewById(R.id.img_team_member);
				mViewHolder.txtPersonName = (TextView) convertView
						.findViewById(R.id.txt_person_name);
				mViewHolder.txtRole = (TextView) convertView
						.findViewById(R.id.txt_role);
				mViewHolder.errorTxt = (TextView) convertView
						.findViewById(R.id.error_title);
				// Util.setTextFont((Activity) mContext, mViewHolder.txtRole);
				// Util.setTextFont((Activity) mContext,
				// mViewHolder.txtPersonName);

				mViewHolder.txtPersonName.setText(gridItemsList.get(position)
						.getPersonName());
				String role = gridItemsList.get(position).getRole().trim()
						.toLowerCase().replaceAll("\\s+", "");
				if (role.equals(mContext.getResources().getString(
						R.string.no_role))
						|| role.equals(mContext.getResources()
								.getString(R.string.ambassadors).toLowerCase())) {
					mViewHolder.txtRole.setText("");

				} else {
					mViewHolder.txtRole.setText(gridItemsList.get(position)
							.getRole().trim());

				}

				mViewHolder.imgTeamMember
						.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
				// mViewHolder.imgTeamMember.setPadding(5, 5, 5, 5);
				convertView.setTag(mViewHolder);
			} else {

				convertView = inflater.inflate(R.layout.ambassador_item, null);
				mViewHolder = new ViewHolder();
				// mViewHolder.image = (ImageView)
				// convertView.findViewById(R.id.image);
				mViewHolder.imgTeamMember = (PhotoView) convertView
						.findViewById(R.id.img_ambassador);
				mViewHolder.txtPersonName = (TextView) convertView
						.findViewById(R.id.txt_ambassador_name);
				mViewHolder.errorTxt = (TextView) convertView
						.findViewById(R.id.error_title);
				// Util.setTextFont((Activity) mContext, mViewHolder.txtRole);
				// Util.setTextFont((Activity) mContext,
				// mViewHolder.txtPersonName);

				mViewHolder.txtPersonName.setText(gridItemsList.get(position)
						.getPersonName());

				mViewHolder.imgTeamMember
						.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
				// mViewHolder.imgTeamMember.setPadding(5, 5, 5, 5);
				convertView.setTag(mViewHolder);

			}
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		mViewHolder.imgTeamMember
				.setLayoutParams(new RelativeLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, reqImageHeight));
		mViewHolder.imgTeamMember.setScaleType(ImageView.ScaleType.MATRIX);

		// Util.setTextFont((Activity) mcontext, mViewHolder.title);
		mViewHolder.imgTeamMember.setImageDrawable(mContext.getResources()
				.getDrawable(R.drawable.imagenotqueued));
		mViewHolder.imgTeamMember.setTag(gridItemsList.get(position)
				.getMemberThumbUrl());

		if (isFrom.equals("team_members")) {
			mViewHolder.imgTeamMember.setImageURL(gridItemsList.get(position)
					.getMemberThumbUrl(), true, this.mEmptyDrawable,
					mViewHolder.errorTxt, false);

		} else {
			mViewHolder.imgTeamMember.setImageURL(gridItemsList.get(position)
					.getMemberThumbUrl(), true, this.mEmptyDrawable,
					mViewHolder.errorTxt, false);

		}

		return convertView;
	}

	public class ViewHolder {

		// public ImageView image;

		public PhotoView imgTeamMember;

		public TextView txtPersonName;

		public TextView txtRole;

		public TextView errorTxt;

	}

	public void updateList(ArrayList<TeamMember> teamMembers) {
		for (TeamMember teamMembers2 : teamMembers) {
			gridItemsList.add(teamMembers2);
		}
		notifyDataSetChanged();
	}
}