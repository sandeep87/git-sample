package in.ccl.adapters;

import in.ccl.model.DayMatches;
import in.ccl.ui.R;

import java.util.ArrayList;

import android.content.Context;
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

	private ArrayList <DayMatches> scheduleList;

	public ScheduleAdapter (Context context, ArrayList <DayMatches> totalNumberOfMatches) {
		mContext = context;
		this.scheduleList = totalNumberOfMatches;
		layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount () {
		return scheduleList.size();
	}

	public DayMatches getItem (int position) {
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
			viewHolder.firstMatchHostingTeam = (TextView) convertView.findViewById(R.id.txt_fst_team);
			viewHolder.firstMacthOpponentTeam = (TextView) convertView.findViewById(R.id.txt_second_team);
			viewHolder.firstMatchTime = (TextView) convertView.findViewById(R.id.txt_time);
			viewHolder.secondMatchHostingTeam = (TextView) convertView.findViewById(R.id.txt_scnd_team);
			viewHolder.secondMatchOpponentTeam = (TextView) convertView.findViewById(R.id.txt_opponent_team);
			viewHolder.secondMatchTime = (TextView) convertView.findViewById(R.id.txt_scnd_time);
			viewHolder.txtfstvs = (TextView) convertView.findViewById(R.id.txt_vs);
			viewHolder.txtScndVs = (TextView) convertView.findViewById(R.id.txt_scnd_vs);
			viewHolder.scndTeamLayout = (RelativeLayout) convertView.findViewById(R.id.scnd_team_layout);
			viewHolder.matchSeperator = (View) convertView.findViewById(R.id.seperator);

			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.scndLayout.setVisibility(View.VISIBLE);
		viewHolder.txtfstvs.setVisibility(View.VISIBLE);
		viewHolder.txtScndVs.setVisibility(View.VISIBLE);
		viewHolder.firstMacthOpponentTeam.setVisibility(View.VISIBLE);
		viewHolder.secondMatchOpponentTeam.setVisibility(View.VISIBLE);
		viewHolder.matchSeperator.setVisibility(View.VISIBLE);
		viewHolder.txtfstvs.setVisibility(View.VISIBLE);
		viewHolder.firstMacthOpponentTeam.setVisibility(View.VISIBLE);
		viewHolder.txtDate.setText(scheduleList.get(position).getDate());
		viewHolder.txtDay.setText(scheduleList.get(position).getDay());
		viewHolder.txtPlace.setText(scheduleList.get(position).getPalce());
		if (!TextUtils.isEmpty(scheduleList.get(position).getDayMatches().get(0).getHostingTeam())) {
			viewHolder.firstMatchHostingTeam.setText(scheduleList.get(position).getDayMatches().get(0).getHostingTeam());
		}

		if (!TextUtils.isEmpty(scheduleList.get(position).getDayMatches().get(0).getOpponentTeam())) {
			viewHolder.firstMacthOpponentTeam.setText(scheduleList.get(position).getDayMatches().get(0).getOpponentTeam());
		}

		if (!TextUtils.isEmpty(scheduleList.get(position).getDayMatches().get(0).getTime())) {
			viewHolder.firstMatchTime.setText(scheduleList.get(position).getDayMatches().get(0).getTime());
		}

		if (scheduleList.size() - 1 != position) {
			if (!TextUtils.isEmpty(scheduleList.get(position).getDayMatches().get(1).getHostingTeam())) {
				viewHolder.secondMatchHostingTeam.setText(scheduleList.get(position).getDayMatches().get(1).getHostingTeam());
			}

			if (!TextUtils.isEmpty(scheduleList.get(position).getDayMatches().get(1).getOpponentTeam())) {
				viewHolder.secondMatchOpponentTeam.setText(scheduleList.get(position).getDayMatches().get(1).getOpponentTeam());
			}

			if (!TextUtils.isEmpty(scheduleList.get(position).getDayMatches().get(1).getTime())) {
				viewHolder.secondMatchTime.setText(scheduleList.get(position).getDayMatches().get(1).getTime());
			}
		}

		if (position == scheduleList.size() - 1) {
			viewHolder.scndLayout.setVisibility(View.GONE);
			viewHolder.txtfstvs.setVisibility(View.GONE);
			viewHolder.firstMacthOpponentTeam.setVisibility(View.GONE);
			viewHolder.matchSeperator.setVisibility(View.GONE);
		}

		if (position == scheduleList.size() - 2) {
			viewHolder.txtfstvs.setVisibility(View.GONE);
			viewHolder.txtScndVs.setVisibility(View.GONE);
			viewHolder.firstMacthOpponentTeam.setVisibility(View.GONE);
			viewHolder.secondMatchOpponentTeam.setVisibility(View.GONE);
		}

		return convertView;
	}

	public static class ViewHolder {

		public TextView txtDate;

		public TextView txtDay;

		public TextView txtPlace;

		public TextView firstMatchHostingTeam;

		public TextView txtfstvs;

		public TextView firstMacthOpponentTeam;

		public TextView firstMatchTime;

		public TextView secondMatchHostingTeam;

		public TextView txtScndVs;

		public TextView secondMatchOpponentTeam;

		public TextView secondMatchTime;

		public View matchSeperator;

		public RelativeLayout scndTeamLayout;

		public RelativeLayout layoutTeams;

		public RelativeLayout scndLayout;
	}

}
