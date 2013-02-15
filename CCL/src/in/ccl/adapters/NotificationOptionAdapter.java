package in.ccl.adapters;

import in.ccl.model.ListItem;
import in.ccl.model.Options;
import in.ccl.ui.R;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NotificationOptionAdapter extends BaseAdapter {

	private Context mContext;

	private LayoutInflater mInflater;

	public ArrayList<Options> optionsArrayList;

	private int battingOptionsCount;

	private int bowlingOptionsCount;
	
	public ArrayList<ListItem> listItem;

	public NotificationOptionAdapter(Context context,
			ArrayList<Options> optionsArrayList) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		ListItem item = null;
		Options options = null;
		this.optionsArrayList = new ArrayList<Options>();
		listItem = new ArrayList<ListItem>();
		listItem.add(item);
		this.optionsArrayList.add(options);
		for (int i = 0; i < optionsArrayList.size(); i++) {
			if (optionsArrayList.get(i).getOptionValue()
					.equalsIgnoreCase("BATTING")) {
				this.optionsArrayList.add(optionsArrayList.get(i));
				item = new ListItem();
				item.setPosition(i);
				item.setChecked(optionsArrayList.get(i).isChecked());
				listItem.add(item);
				battingOptionsCount++;
			}
		}
		this.optionsArrayList.add(options);
		item = null;
		listItem.add(item);
		for (int i = 0; i < optionsArrayList.size(); i++) {
			if (optionsArrayList.get(i).getOptionValue()
					.equalsIgnoreCase("BOWLING")) {
				this.optionsArrayList.add(optionsArrayList.get(i));
				item = new ListItem();
				item.setPosition(i);
				item.setChecked(optionsArrayList.get(i).isChecked());
				listItem.add(item);
				bowlingOptionsCount++;
			}
		}
	}

	@Override
	public int getCount() {
		return optionsArrayList.size();
	}

	@Override
	public Options getItem(int position) {
		return optionsArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.options_row, null);
			viewHolder.optionContent = (LinearLayout) convertView
					.findViewById(R.id.option_content);
			viewHolder.optionName = (TextView) convertView
					.findViewById(R.id.txt_option_name);
			viewHolder.imgCheckBox = (CheckBox) convertView
					.findViewById(R.id.img_check_box);
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.txt_option_title);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (position == 0) {
			viewHolder.title.setText("BATTING");
			viewHolder.title.setVisibility(View.VISIBLE);
			viewHolder.optionContent.setVisibility(View.GONE);
		} else if (position == battingOptionsCount + 1) {
			viewHolder.title.setText("BOWLING");
			viewHolder.title.setVisibility(View.VISIBLE);
			viewHolder.optionContent.setVisibility(View.GONE);
		} else {
			viewHolder.title.setVisibility(View.GONE);
			viewHolder.optionContent.setVisibility(View.VISIBLE);
			if (optionsArrayList.get(position).getActionValue() != null
					&& !TextUtils.isEmpty(optionsArrayList.get(position)
							.getActionValue())) {
				viewHolder.optionName.setText(optionsArrayList.get(
						position).getActionValue());
			}
			viewHolder.imgCheckBox.setChecked(optionsArrayList.get(
					position).isChecked());
			viewHolder.imgCheckBox.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					optionsArrayList.get(position).setChecked(
							viewHolder.imgCheckBox.isChecked());
				}
			});

		}
		return convertView;
	}

	public static class ViewHolder {
		private LinearLayout optionContent;
		private TextView title;
		private TextView optionName;
		private CheckBox imgCheckBox;
	}
}
