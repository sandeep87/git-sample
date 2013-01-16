package in.ccl.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class NotificationActivity extends TopActivity {

	ListView list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContent(R.layout.notifications_view);
		list = (ListView) findViewById(R.id.notification_players_list);
		NotificationAdapter adapter = new NotificationAdapter();
		list.setAdapter(adapter);

	}

	public class NotificationAdapter extends BaseAdapter {

		private Integer image[] = { R.drawable.ic_launcher,
				R.drawable.ic_launcher, R.drawable.ic_launcher };
		private String name[] = { "Mahesh", "Dileep", "Anil" };
		private String Position[] = { "Captain", "Batsman", "Bowler" };

		@Override
		public int getCount() {
			return image.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return image[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				LayoutInflater inflator = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
				convertView = inflator.inflate(R.layout.notifications_item,
						null);
				holder = new ViewHolder();
				holder.mImage = (ImageView) convertView
						.findViewById(R.id.image);
				holder.mName = (TextView) convertView.findViewById(R.id.name);
				holder.mPosition = (TextView) convertView
						.findViewById(R.id.position);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.mImage.setImageResource(image[position]);
			holder.mName.setText(name[position]);
			holder.mPosition.setText(Position[position]);
			return convertView;
		}

	}

	private class ViewHolder {
		ImageView mImage;
		TextView mName, mPosition;
	}

}