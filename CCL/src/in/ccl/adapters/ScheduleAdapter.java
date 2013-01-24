package in.ccl.adapters;

import in.ccl.helper.Util;
import in.ccl.model.ScheduleItem;
import in.ccl.ui.R;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ScheduleAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;

	private Context mContext;

	

	private ArrayList <ScheduleItem> scheduleList;

	/*
	 * public ScheduleAdapter(Context context, String[] date, String[] day, String[] place, String[] players, String[] time) { mContext = context; this.date = date; this.day = day; this.place = place; this.players = players; this.time = time; layoutInflater = (LayoutInflater) mContext
	 * .getSystemService(Context.LAYOUT_INFLATER_SERVICE); }
	 */
	public ScheduleAdapter (Context context, ArrayList <ScheduleItem> scheduleList) {
		mContext = context;
		this.scheduleList = scheduleList;
		layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount () {
		return scheduleList.size();
	}

	public ScheduleItem getItem (int position) {
		return scheduleList.get(position);
	}

	public long getItemId (int position) {
		return 0;
	}

	public View getView (int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.schedule_row, null);
			
			viewHolder.layoutTeams = (RelativeLayout) convertView.findViewById(R.id.layout_teams);
			viewHolder.scndLayout = (RelativeLayout) convertView.findViewById(R.id.scnd_layout);

			viewHolder.txtDate = (TextView) convertView.findViewById(R.id.txt_date);
			viewHolder.txtDay = (TextView) convertView.findViewById(R.id.txt_day);
			viewHolder.txtPlace = (TextView) convertView.findViewById(R.id.txt_place);
			viewHolder.txtfstTeam = (TextView) convertView.findViewById(R.id.txt_fst_team);
			viewHolder.txtSecondTeam = (TextView) convertView.findViewById(R.id.txt_second_team);
			viewHolder.txtTime = (TextView) convertView.findViewById(R.id.txt_time);
			//viewHolder.imgFstteamInfo = (ImageButton) convertView.findViewById(R.id.img_fstteam_info);
			viewHolder.txtScndTeam = (TextView) convertView.findViewById(R.id.txt_scnd_team);
			viewHolder.txtOpponentTeam = (TextView) convertView.findViewById(R.id.txt_opponent_team);
			viewHolder.txtScndTime = (TextView) convertView.findViewById(R.id.txt_scnd_time);
			//viewHolder.imgScndBtnInfo = (ImageButton) convertView.findViewById(R.id.img_scnd_btn_info);
			//viewHolder.imgScndBtnInfo = (ImageButton) convertView.findViewById(R.id.img_scnd_btn_info);
			viewHolder.txtfstvs = (TextView) convertView.findViewById(R.id.txt_vs);
			viewHolder.txtScndVs = (TextView) convertView.findViewById(R.id.txt_scnd_vs);

			viewHolder.scndTeamLayout = (RelativeLayout) convertView.findViewById(R.id.scnd_team_layout);
			// viewHolder.viewSeparator = (View) convertView.findViewById(R.id.view_separator);

			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Util.setTextFont((Activity) mContext, viewHolder.txtDate);
		Util.setTextFont((Activity) mContext, viewHolder.txtPlace);

		Util.setTextFont((Activity) mContext, viewHolder.txtDay);
		Util.setTextFont((Activity) mContext, viewHolder.txtfstTeam);
		Util.setTextFont((Activity) mContext, viewHolder.txtSecondTeam);
		Util.setTextFont((Activity) mContext, viewHolder.txtTime);
		Util.setTextFont((Activity) mContext, viewHolder.txtScndTeam);
		Util.setTextFont((Activity) mContext, viewHolder.txtOpponentTeam);
		Util.setTextFont((Activity) mContext, viewHolder.txtScndTime);
		Util.setTextFont((Activity) mContext, viewHolder.txtfstvs);
		Util.setTextFont((Activity) mContext, viewHolder.txtScndVs);
		viewHolder.txtDate.setText(scheduleList.get(position).getDate());

		viewHolder.txtDay.setText(scheduleList.get(position).getDay());
		viewHolder.txtPlace.setText(scheduleList.get(position).getPalce());

		viewHolder.txtfstTeam.setText(scheduleList.get(position).getFirstMatchTeamOne());
		
		if (!TextUtils.isEmpty(scheduleList.get(position).getFirstMatchTeamTwo())) {
			viewHolder.txtSecondTeam.setText(scheduleList.get(position).getFirstMatchTeamTwo());
		}
		else {
			viewHolder.txtSecondTeam.setText("");
			viewHolder.txtfstvs.setText("");
		}
		viewHolder.txtTime.setText(scheduleList.get(position).getFirstMatchTime());

		if (!TextUtils.isEmpty(scheduleList.get(position).getSecondMatchTeamOne())) {
			viewHolder.txtScndTeam.setText(scheduleList.get(position).getSecondMatchTeamOne());
		}
		else {
			viewHolder.txtScndTeam.setText("");
			viewHolder.txtScndTime.setText("");
			viewHolder.layoutTeams.setBackgroundResource(R.drawable.schedule_final_txt_bg);
			viewHolder.scndLayout.setVisibility(View.GONE);
		
		//	viewHolder.imgScndBtnInfo.setVisibility(View.INVISIBLE);

			// viewHolder.scndTeamLayout.setVisibility(View.INVISIBLE);
			// viewHolder.viewSeparator.setVisibility(View.INVISIBLE);
		}
		if (!TextUtils.isEmpty(scheduleList.get(position).getSecondMatchTeamTwo())) {
			viewHolder.txtOpponentTeam.setText(scheduleList.get(position).getSecondMatchTeamTwo());

		}
		else {
			viewHolder.txtOpponentTeam.setText("");
			viewHolder.txtScndVs.setText("");
		}
		if (!TextUtils.isEmpty(scheduleList.get(position).getSecondMatchTime())) {
			viewHolder.txtScndTime.setText(scheduleList.get(position).getSecondMatchTime());
		}

		return convertView;
	}

	public static class ViewHolder {

		public TextView txtDate;

		public TextView txtDay;

		public TextView txtPlace;

		public TextView txtfstTeam;

		public TextView txtfstvs;

		public TextView txtSecondTeam;

		public TextView txtTime;

		//public ImageButton imgFstteamInfo;

		public TextView txtScndTeam;

		public TextView txtScndVs;

		public TextView txtOpponentTeam;

		public TextView txtScndTime;

	//	public ImageButton imgScndBtnInfo;

		public RelativeLayout scndTeamLayout;

		public RelativeLayout layoutTeams;
		public RelativeLayout scndLayout;

		
		// public View viewSeparator;
	}

}
