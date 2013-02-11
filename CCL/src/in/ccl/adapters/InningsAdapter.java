package in.ccl.adapters;

import in.ccl.score.Batting;
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

public class InningsAdapter extends BaseAdapter {

	private Innings inningsList;

	private LayoutInflater inflater;

	public InningsAdapter (Context context, Innings inninsList) {

		this.inningsList = inninsList;

		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount () {

		return inningsList.getBatting_info().size();

	}

	@Override
	public Innings getItem (int position) {

		return inningsList;
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
			tableView =(TableLayout) inflater.inflate(R.layout.innings_list_child, null);
			View view = inflater.inflate(R.layout.batting_list_item_child, null);
			mViewHolder = new ViewHolder();
			mViewHolder.txtPlayerName = (TextView) view.findViewById(R.id.txt_player_name);
			//mViewHolder.txtPlayerRuns = (TextView) view.findViewById(R.id.txt_player_runs);
			mViewHolder.txtPlayerPlayedBalls = (TextView) view.findViewById(R.id.txt_player_playedballs);
			mViewHolder.txtPlayerStatus = (TextView) view.findViewById(R.id.txt_player_status);
			view.setId(position);
			tableView.addView(view);
			convertView = tableView;
			convertView.setTag(mViewHolder);
		}
		else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		try {
       if(!TextUtils.isEmpty(inningsList.getBatting_info().get(position).getName())){
      	 mViewHolder.txtPlayerName.setText(inningsList.getBatting_info().get(position).getName());
       }
			
			String score = String.valueOf(inningsList.getBatting_info().get(position).getScore());
		//	mViewHolder.txtPlayerRuns.setText(score);
			 if(score != null){
					mViewHolder.txtPlayerPlayedBalls.setText(score + " (" + String.valueOf(inningsList.getBatting_info().get(position).getBalls()).trim() + ")");

			 }
			String status = "";

			Batting playerStatus = inningsList.getBatting_info().get(position);
			if (playerStatus.getCaught() != null && !TextUtils.isEmpty(playerStatus.getCaught())) {
				status = "(c) " + playerStatus.getCaught();
			}
			if (playerStatus.getBowled() != null && !TextUtils.isEmpty(playerStatus.getBowled())) {
				status = "(b) " + playerStatus.getBowled();
			}
			if (playerStatus.getHitwicket() != null && !TextUtils.isEmpty(playerStatus.getHitwicket())) {
				status = "(hit wicket) ";
			}
			if (playerStatus.getRetiredhurt() != null && !TextUtils.isEmpty(playerStatus.getRetiredhurt())) {
				status = "(retired hurt) ";
			}
			if (playerStatus.getRunout() != null && !TextUtils.isEmpty(playerStatus.getRunout())) {
				status = "(run out) " + playerStatus.getRunout();
			}
			if (playerStatus.getNotout() != null && !TextUtils.isEmpty(playerStatus.getNotout())) {
				status = "(not out) ";
			}
			if (playerStatus.getStumped() != null && !TextUtils.isEmpty(playerStatus.getStumped())) {
				status = "(st) " + playerStatus.getStumped();
			}
			if (playerStatus.getCandb() != null && !TextUtils.isEmpty(playerStatus.getCandb())) {
				status = "(c & b) " + playerStatus.getCandb();
			}
			if (playerStatus.getLbw() != null && !TextUtils.isEmpty(playerStatus.getLbw())) {
				status = "(lbw)";
			}
			if (playerStatus.getHandledtheball() != null && !TextUtils.isEmpty(playerStatus.getHandledtheball())) {
				status = "(handled the ball)";
			}
			if (playerStatus.getDidnotbat() != null && !TextUtils.isEmpty(playerStatus.getDidnotbat())) {
				status = "(dnb)";
			}
			if (!TextUtils.isEmpty(status)) {
				mViewHolder.txtPlayerStatus.setVisibility(View.VISIBLE);

				mViewHolder.txtPlayerStatus.setText(status);
			}
			else {
				mViewHolder.txtPlayerStatus.setVisibility(View.INVISIBLE);
			}

		}
		catch (Exception e) {
			// e.printStackTrace();
		}
		return convertView;
	}

	public class ViewHolder {

		public TextView txtPlayerName;

		public TextView txtPlayerRuns;

		public TextView txtPlayerPlayedBalls;

		public TextView txtPlayerStatus;

	}

}
