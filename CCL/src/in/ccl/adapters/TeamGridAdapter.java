package in.ccl.adapters;

import in.ccl.ui.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class TeamGridAdapter extends BaseAdapter {

	private Context mContext;

	private int[] teamLogo;

	public TeamGridAdapter (Context ctx, int[] teams) {
		mContext = ctx;
		teamLogo = teams;
	}

	@Override
	public int getCount () {
		// TODO Auto-generated method stub
		return teamLogo.length;
	}

	@Override
	public Object getItem (int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId (int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		ImageView i;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = (View) inflater.inflate(R.layout.team_logo_child, null);
		}
		i = (ImageView) convertView.findViewById(R.id.team_logo_img);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		i.setBackgroundResource(teamLogo[position]);
		return convertView;
	}

}
