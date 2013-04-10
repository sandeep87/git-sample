package com.paradigmcreatives.adapters;

import java.util.ArrayList;

import com.paradigmcreatives.activity.MapViewActivity;
import com.paradigmcreatives.activity.R;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TextView;

public class ExtendedBaseAdapter extends BaseAdapter{
	
	ArrayList<String> listData;
	private Context mContext;
	
	public ExtendedBaseAdapter(Context context,ArrayList<String> list){
		listData = list;
		mContext = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return listData.indexOf(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// i am creating a view simple with only one text view in it.as per requirement
		//TextView textView = new TextView(ExtendedBaseAdapter.this);
		ViewHolder holder = null;
		/*if (convertView == null) {
			//convertView = View.inflate(mContext, R.layout.listview_widgets, null);
			holder = new ViewHolder();
			convertView.setTag(holder);
			holder.text = (TextView)convertView.findViewById(R.id.listview_text);
			holder.checkButton = (RadioButton)convertView.findViewById(R.id.radio_check);
			if (position == 0) {
				holder.checkButton.setChecked(true);
			}
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		*/
		
		//holder.checkButton.setId(position);
		
		holder.checkButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					//MapViewActivity.checkNumber = buttonView.getId();
					buttonView.setChecked(true);
				}
			}
		});
		
		holder.text.setTextColor(Color.BLACK);
		holder.text.setText(listData.get(position));
		return convertView;
	}
	
	static class ViewHolder {
		TextView text;
		RadioButton checkButton;
		}

}
