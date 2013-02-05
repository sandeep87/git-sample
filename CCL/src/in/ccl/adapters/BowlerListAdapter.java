package in.ccl.adapters;

import in.ccl.score.Innings;
import in.ccl.ui.R;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BowlerListAdapter extends BaseAdapter {

	private ArrayList <Innings> bowlerList;

	private LayoutInflater inflater;

	public BowlerListAdapter (Context context, ArrayList <Innings> inninsList) {

		bowlerList = inninsList;

		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount () {
		return bowlerList.get(0).getBowler_info().size();
	}

	@Override
	public Object getItem (int position) {
		return bowlerList.get(position);
	}

	@Override
	public long getItemId (int position) {
		return 0;
	}

	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		ViewHolder mViewHolder;
		if (convertView == null) {
			convertView                  = inflater.inflate(R.layout.bowler_list_child, null);
			mViewHolder                  = new ViewHolder();
			mViewHolder.txtBowlerName    = (TextView) convertView.findViewById(R.id.bowler_name);
			mViewHolder.txtBowlerRuns    = (TextView) convertView.findViewById(R.id.bowler_runs);
			mViewHolder.txtBowlerExtras  = (TextView) convertView.findViewById(R.id.bowler_extras);
			mViewHolder.txtBowlerOvers   = (TextView) convertView.findViewById(R.id.bowler_overs);

			convertView.setTag(mViewHolder);
		}
		else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		try {

			mViewHolder.txtBowlerName.setText(bowlerList.get(0).getBowler_info().get(position).getBowlerName());
			mViewHolder.txtBowlerRuns.setText(String.valueOf(bowlerList.get(0).getBowler_info().get(position).getBowlerRuns()));
			// int extras = bowlerList.get(position).getByes() + bowlerList.get(position).getLegbyes() + bowlerList.get(position).getWides() + bowlerList.get(position).getNoballs();
			// mViewHolder.txtBowlerExtras.setText(String.valueOf(extras));
			mViewHolder.txtBowlerOvers.setText(String.valueOf(bowlerList.get(0).getBowler_info().get(position).getBowlerOvers()));

		}
		catch (Exception e) {
		//	e.printStackTrace();
		}

		return convertView;
	}

	public class ViewHolder {

		public TextView txtBowlerName;

		public TextView txtBowlerRuns;

		public TextView txtBowlerOvers;

		public TextView txtBowlerExtras;

	}
}
