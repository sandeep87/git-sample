package in.ccl.adapters;

import in.ccl.model.Items;
import in.ccl.ui.R;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NewsAdapter extends BaseAdapter {

	private ArrayList <Items> newsArrayList;

	// private ArrayList <Items> nationalNewsArrayList;
	private Context mContext;

	private LayoutInflater layoutInflater;

	public NewsAdapter (Context context, ArrayList <Items> newsArrayList) {
		mContext = context;
		this.newsArrayList = newsArrayList;
		// this.nationalNewsArrayList = nationalNewsArrayList;
		layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount () {

		return newsArrayList.size();
	}

	@Override
	public Items getItem (int position) {

		return newsArrayList.get(position);
	}

	@Override
	public long getItemId (int position) {

		return 0;
	}

	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.news_row, null);
			viewHolder.txtNewsItem = (TextView) convertView.findViewById(R.id.txt_news_item);
			// viewHolder.txtNationalNewsItem = (TextView) convertView.findViewById(R.id.txt_national_news_item);

			convertView.setTag(viewHolder);
		}
		else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.txtNewsItem.setText(newsArrayList.get(position).getTitle().trim());
		// viewHolder.txtNationalNewsItem.setText(nationalNewsArrayList.get(position).getTitle());

		return convertView;
	}

	public static class ViewHolder {

		public TextView txtNewsItem;
		// public TextView txtNationalNewsItem;

	}
}
