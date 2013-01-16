package in.ccl.adapters;

import in.ccl.helper.Util;
import in.ccl.ui.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdaptor extends BaseAdapter {

	int[] logos;

	int[] images;

	private String[] names;

	private String[] teams;
	private String[] franchaseNames;

	private Context mContext;

	private LayoutInflater inflator;

	private ImageView logo;

	private ImageView image;

	private TextView owner_name;

	private TextView txt_franchise_owner;
	private TextView txt_team_name;

	public CustomAdaptor(Context context, int[] logos, int[] images,
			String[] names, String[] teams, String[] franchaseNames) {
		mContext = context;
		this.logos = logos;
		this.images = images;
		this.names = names;
		this.teams = teams;
		this.franchaseNames = franchaseNames;
		inflator = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return logos.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			System.out.println("null");
			convertView = (View) inflator.inflate(R.layout.lounge_row, null);

		}
		logo = (ImageView) convertView.findViewById(R.id.logo);
		txt_team_name = (TextView) convertView.findViewById(R.id.txt_team_name);
		owner_name = (TextView) convertView.findViewById(R.id.txt_team_owner_name);
		txt_franchise_owner = (TextView) convertView.findViewById(R.id.txt_franchise_owner);
		Util.setTextFont((Activity)mContext, owner_name);
		Util.setTextFont((Activity)mContext, txt_team_name);
		Util.setTextFont((Activity)mContext, txt_franchise_owner);

		image = (ImageView) convertView.findViewById(R.id.img_team_owner);
		logo.setImageResource(logos[position]);
		txt_team_name.setText(teams[position]);
		image.setImageResource(images[position]);
		owner_name.setText(names[position]);
		txt_franchise_owner.setText(franchaseNames[position]);

		return convertView;
	}
}
