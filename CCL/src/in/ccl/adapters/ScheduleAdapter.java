package in.ccl.adapters;

import in.ccl.helper.Util;
import in.ccl.ui.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class ScheduleAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;

	private Context mContext;

	private String[] date;

	private String[] day;

	private String[] place;

	private String[] players;

	private String[] time;

	public ScheduleAdapter(Context context, String[] date, String[] day,
			String[] place, String[] players, String[] time) {
		mContext = context;
		this.date = date;
		this.day = day;
		this.place = place;
		this.players = players;
		this.time = time;
		layoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return place.length;
	}

	public Object getItem(int position) {
		return place[position];
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.schedule_row, null);
			viewHolder.txtDate = (TextView) convertView
					.findViewById(R.id.txt_date);
			viewHolder.txtDay = (TextView) convertView
					.findViewById(R.id.txt_day);
			viewHolder.txtPlace = (TextView) convertView
					.findViewById(R.id.txt_place);
			viewHolder.txtfstTeam = (TextView) convertView
					.findViewById(R.id.txt_fst_team);
			viewHolder.txtSecondTeam = (TextView) convertView
					.findViewById(R.id.txt_second_team);
			viewHolder.txtTime = (TextView) convertView
					.findViewById(R.id.txt_time);
			viewHolder.imgFstteamInfo = (ImageButton) convertView
					.findViewById(R.id.img_fstteam_info);
			viewHolder.txtScndTeam = (TextView) convertView
					.findViewById(R.id.txt_scnd_team);
			viewHolder.txtOpponentTeam = (TextView) convertView
					.findViewById(R.id.txt_opponent_team);
			viewHolder.txtScndTime = (TextView) convertView
					.findViewById(R.id.txt_scnd_time);
			viewHolder.imgScndBtnInfo = (ImageButton) convertView
					.findViewById(R.id.img_scnd_btn_info);
			viewHolder.imgScndBtnInfo = (ImageButton) convertView
					.findViewById(R.id.img_scnd_btn_info);
			viewHolder.txtfstvs = (TextView) convertView
					.findViewById(R.id.txt_vs);
			viewHolder.txtScndVs = (TextView) convertView
					.findViewById(R.id.txt_scnd_vs);
			convertView.setTag(viewHolder);
		} else {
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
		viewHolder.txtDate.setText(date[position]);

		viewHolder.txtDay.setText(day[position]);
		viewHolder.txtPlace.setText(place[position]);
		viewHolder.txtfstTeam.setText("BANGALORE");
		viewHolder.txtSecondTeam.setText("CHENNAI");
		viewHolder.txtTime.setText(time[position]);
		viewHolder.txtScndTeam.setText("SOUTH AFRICA");
		viewHolder.txtOpponentTeam.setText("AUSTRALIA");
		viewHolder.txtScndTime.setText(time[position]);

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

		public ImageButton imgFstteamInfo;

		public TextView txtScndTeam;

		public TextView txtScndVs;

		public TextView txtOpponentTeam;

		public TextView txtScndTime;

		public ImageButton imgScndBtnInfo;

	}

}
