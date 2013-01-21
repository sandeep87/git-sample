package in.ccl.adapters;

import in.ccl.logging.Logger;
import in.ccl.model.Innings;
import in.ccl.ui.R;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InningsAdapter extends BaseAdapter {

	private ArrayList <Innings> inningsList;

	private LayoutInflater inflater;

	public InningsAdapter (Context context, ArrayList <Innings> inninsList, String from) {

		this.inningsList = inninsList;

		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount () {
		// TODO Auto-generated method stub
		return inningsList.size();
	}

	@Override
	public Innings getItem (int position) {
		// TODO Auto-generated method stub
		return inningsList.get(position);
	}

	@Override
	public long getItemId (int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		ViewHolder mViewHolder;	
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_innings, null);
			mViewHolder = new ViewHolder();
			mViewHolder.txtPlayerName = (TextView) convertView.findViewById(R.id.txt_player_name);
			mViewHolder.txtPlayerRuns = (TextView) convertView.findViewById(R.id.txt_player_runs);
			mViewHolder.txtPlayerPlayedBalls = (TextView) convertView.findViewById(R.id.txt_player_playedballs);
			convertView.setTag(mViewHolder);
		}
		else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		try {
			mViewHolder.txtPlayerName.setText(inningsList.get(position).getPlayerNameOne());
			mViewHolder.txtPlayerRuns.setText(inningsList.get(position).getFirstPlayerScore());
			mViewHolder.txtPlayerPlayedBalls.setText(inningsList.get(position).getFirstPlayedBalls());
		}
		catch (Exception e) {
			Logger.info("INNINGS ADAPTER", e.toString());
		}

		return convertView;
	}

	public class ViewHolder {

		public TextView txtPlayerName;

		public TextView txtPlayerRuns;

		public TextView txtPlayerPlayedBalls;

	}
}
