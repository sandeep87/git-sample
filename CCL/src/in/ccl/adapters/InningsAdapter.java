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
			convertView = inflater.inflate(R.layout.row_innings, null);
			mViewHolder = new ViewHolder();
			mViewHolder.txtPlayerName        = (TextView) convertView.findViewById(R.id.txt_player_name);
			mViewHolder.txtPlayerRuns        = (TextView) convertView.findViewById(R.id.txt_player_runs);
			mViewHolder.txtPlayerPlayedBalls = (TextView) convertView.findViewById(R.id.txt_player_playedballs);
			convertView.setTag(mViewHolder);
		}
		else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		try {

			mViewHolder.txtPlayerName.setText(inningsList.getBatting_info().get(position).getName());
			String score = String.valueOf(inningsList.getBatting_info().get(position).getScore());
			mViewHolder.txtPlayerRuns.setText(score);
			mViewHolder.txtPlayerPlayedBalls.setText(" (" + String.valueOf(inningsList.getBatting_info().get(position).getBalls()).trim() + ")");

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

	}
}
