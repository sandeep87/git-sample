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

public class DirectorAdapter extends BaseAdapter {

	private Context mContext;

	private String[] names;

	private int[] images;

	private LayoutInflater mInflater;

	private TextView directorNames;

	private TextView directorTitle;

	private ImageView directorImages;

	public DirectorAdapter (Context context, String[] names, int[] images) {
		mContext = context;
		this.names = names;
		this.images = images;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount () {
		return names.length;
	}

	@Override
	public Object getItem (int position) {
		return position;
	}

	@Override
	public long getItemId (int position) {
		return position;
	}

	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		convertView = (View) mInflater.inflate(R.layout.owners_lounge_directors_row, null);

		directorImages = (ImageView) convertView.findViewById(R.id.director_image);
		directorNames = (TextView) convertView.findViewById(R.id.director_name);
		directorTitle = (TextView) convertView.findViewById(R.id.director_title);

		Util.setTextFont((Activity) mContext, directorNames);
		Util.setTextFont((Activity) mContext, directorTitle);
		directorImages.setImageResource(images[position]);

		directorNames.setText(names[position]);
		if (position == 0) {
			directorTitle.setText(mContext.getResources().getString(R.string.founder_director));
		}
		else {
			directorTitle.setText(mContext.getResources().getString(R.string.director));
		}

		return convertView;

	}

}
