package com.paradigmcreatives.adapters;

import java.util.Map;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.paradigmcreatives.activity.R;
import com.paradigmcreatives.drivingDirection.WeakestHashMap;

public class TrackListAdapter extends BaseAdapter{
	private Map<Integer, Map<String, String>> mDirivingData;
	private Context mContext;
	private Drawable trackLeftTop;
	private Drawable trackRightTop;
	private Drawable trackLeftbottpm;
	private Drawable trackRightBottom;
	private Drawable trackmiddle;
	
	public TrackListAdapter(Context context,Map<Integer, Map<String, String>> dirivingData){
		mContext = context;
		mDirivingData = dirivingData;
		//System.out.println(" SIZE ------------ : "+mDirivingData.size());
		 /*trackLeftTop = mContext.getResources().getDrawable(R.drawable.track_left_top);
		 trackRightTop = mContext.getResources().getDrawable(R.drawable.track_right_top);
		 trackLeftbottpm = mContext.getResources().getDrawable(R.drawable.track_left_bottom);
		 trackRightBottom= mContext.getResources().getDrawable(R.drawable.track_right_buttom);
		 trackmiddle = mContext.getResources().getDrawable(R.drawable.track_middle);*/
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDirivingData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDirivingData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 /*Set s=mDirivingData.entrySet();
		 Iterator it=s.iterator();
		 while(it.hasNext())
	        {
	            // key=value separator this by Map.Entry to get key and value
	            Map.Entry m =(Map.Entry)it.next();
	            // getKey is used to get key of Map
	            String key=(String)m.getKey();
	            // getValue is used to get value of key in Map
	            String value=(String)m.getValue();
	            System.out.println("Key :"+key+"  Value :"+value);
	            	data.add("Source");
			data.add("Destination");
			data.add("Your are at");
			data.add("Remaining Distance :");
	        }*/
		
		/*ViewHolder holder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.track_list_view_data, null);
			holder = new ViewHolder();
			convertView.setTag(holder);
			holder.name = (TextView)convertView.findViewById(R.id.track_view_name);
			holder.data = (TextView)convertView.findViewById(R.id.track_view_data);
		} else{
			holder = (ViewHolder) convertView.getTag();
		}*/
		
		WeakestHashMap wMap = (WeakestHashMap) mDirivingData.get(position);
		//System.out.println(" my key :"+ wMap.key);
		
		/*if (position == 0) {
			holder.name.setBackgroundDrawable(trackLeftTop);
			holder.data.setBackgroundDrawable(trackRightTop);
		}else if(position == 6){
			holder.name.setBackgroundDrawable(trackLeftbottpm);
			holder.data.setBackgroundDrawable(trackRightBottom);
		}*/
		
		//holder.name.setText(wMap.key);
		/*if (wMap.key == "Remaining Distance") {
			int i = Integer.parseInt(wMap.name);
			i = i/1000;
			System.out.println(" Remaining :"+i);
			holder.data.setText(i+" Kms");
		}*/
		//holder.data.setText(wMap.name);
		
			/*Set s=mDirivingData.entrySet();
			Iterator it=s.iterator();
			while(it.hasNext())
	        {
	            // key=value separator this by Map.Entry to get key and value
	            Map.Entry m =(Map.Entry)it.next();
	            // getKey is used to get key of Map
	            String key=(String)m.getKey();
	            // getValue is used to get value of key in Map
	            String value=(String)m.getValue();
	            
	            holder.name.setText(key);
	            holder.data.setText(value);*/
	            
	            //System.out.println("Key :"+key+"  Value :"+value);
	       // }
		
		return convertView;
	
	}
	class ViewHolder {
		TextView name;
		TextView data;
		}
	
}
