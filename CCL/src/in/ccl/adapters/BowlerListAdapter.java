package in.ccl.adapters;

import in.ccl.score.Innings;
import in.ccl.ui.R;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableLayout;
import android.widget.TextView;

public class BowlerListAdapter extends BaseAdapter {

	private Innings bowlerList;

	private LayoutInflater inflater;

	public BowlerListAdapter (Context context, Innings inninsList) {

		bowlerList = inninsList;

		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount () {
		return bowlerList.getBowler_info().size();
	}

	@Override
	public Object getItem (int position) {
		return bowlerList;
	}

	@Override
	public long getItemId (int position) {
		return 0;
	}

	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		ViewHolder mViewHolder;
		if (convertView == null) {
			TableLayout tableView = (TableLayout) convertView;

			tableView = (TableLayout) inflater.inflate(R.layout.innings_list_child, null);
			View view = inflater.inflate(R.layout.bowler_list_item_child, null);
			mViewHolder = new ViewHolder();
			mViewHolder.txtBowlerName = (TextView) view.findViewById(R.id.bowler_name);
			mViewHolder.txtBowlerRuns = (TextView) view.findViewById(R.id.bowler_runs);
			mViewHolder.txtBowlerExtras = (TextView) view.findViewById(R.id.bowler_wickets);
			mViewHolder.txtBowlerOvers = (TextView) view.findViewById(R.id.bowler_overs);
			mViewHolder.txtBowlerMaidens = (TextView) view.findViewById(R.id.bowler_mdns);
			view.setId(position);
			tableView.addView(view);
			convertView = tableView;
			convertView.setTag(mViewHolder);
		}
		else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		try {
       if(! TextUtils.isEmpty(bowlerList.getBowler_info().get(position).getBowlerName())){
      	 
   			mViewHolder.txtBowlerName.setText(bowlerList.getBowler_info().get(position).getBowlerName());

       }
			mViewHolder.txtBowlerRuns.setText(String.valueOf(bowlerList.getBowler_info().get(position).getBowlerRuns()));
			// int extras = bowlerList.get(position).getByes() + bowlerList.get(position).getLegbyes() + bowlerList.get(position).getWides() + bowlerList.get(position).getNoballs();
			mViewHolder.txtBowlerExtras.setText(String.valueOf(bowlerList.getBowler_info().get(position).getBowlerWickets()));
			mViewHolder.txtBowlerOvers.setText(String.valueOf(bowlerList.getBowler_info().get(position).getBowlerOvers()));
			mViewHolder.txtBowlerMaidens.setText(String.valueOf(bowlerList.getBowler_info().get(position).getBowlerMaidens()));
		}
		catch (Exception e) {
			// e.printStackTrace();
		}
		return convertView;
	}

	public class ViewHolder {

		public TextView txtBowlerName;

		public TextView txtBowlerRuns;

		public TextView txtBowlerOvers;

		public TextView txtBowlerExtras;

		public TextView txtBowlerMaidens;

	}
}
