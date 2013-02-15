package in.ccl.adapters;

import in.ccl.model.Players;
import in.ccl.ui.R;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NotificationAdapter extends BaseAdapter {

	private Context mContext;

	private ArrayList <Players> playersList;

	private int clickedPosition;

	private LayoutInflater mInflater;

	private ViewHolder viewHolder;

	public NotificationAdapter (Context context, ArrayList <Players> notificationPlayersArrayList) {
		mContext = context;
		playersList = notificationPlayersArrayList;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount () {
		return playersList.size();
	}

	@Override
	public Players getItem (int position) {

		return playersList.get(position);
	}

	@Override
	public long getItemId (int position) {
		return 0;
	}

	@Override
	public View getView (int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.players_row, null);
			viewHolder.playerName = (TextView) convertView.findViewById(R.id.txt_player_name);
			viewHolder.arrowIndicator = (ImageView) convertView.findViewById(R.id.img_arrow);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(playersList.get(position).getPlayerName() != null){
			if(viewHolder.playerName != null){
				viewHolder.playerName.setText(playersList.get(position).getPlayerName());
				viewHolder.playerName.setTextColor(Color.GRAY);
			}else{
				System.out.println("nagesh not null");
			}
		
		}
		if (clickedPosition == position) {
			viewHolder.playerName.setTextColor(Color.BLACK);
			viewHolder.arrowIndicator.setVisibility(View.VISIBLE);
		}
		else {
			viewHolder.playerName.setTextColor(Color.GRAY);
			viewHolder.arrowIndicator.setVisibility(View.INVISIBLE);
		}
		return convertView;
	}

	static class ViewHolder {

		private TextView playerName;

		private ImageView arrowIndicator;

	}

	public void updateList (int clickedPosition) {
		this.clickedPosition = clickedPosition;
		notifyDataSetChanged();
	}

}
