package com.paradigmcreatives.dialog;



import android.app.Dialog;
import android.content.Context;

import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paradigmcreatives.activity.MapViewActivity;
import com.paradigmcreatives.activity.R;

public class CustomizeDialog extends Dialog implements OnClickListener{
	private Context mContext;
	private int mAlertType;
	private boolean flag;
	private String myTitle;
	private String message;
	
	private int alertTimeTemp;
	

	public CustomizeDialog(Context context, int alerttype, String title, String message) {
		super(context);
		mContext = context;
		mAlertType = alerttype;

		if (title != null) {
			setMyTitle(title);
		}
		if (message != null) {
			setMessage(message);
		}
		// TODO Auto-generated constructor stub
		
		// Alert type 1 = alert with any button.
	   // Alert type 2 = alert with list view and radio button.
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		switch (mAlertType) {
		case 1:
			flag = false;
			break;
		case 2:
			flag = true;
			break;
		}
		
		if (flag) {
			setContentView(R.layout.custom_dialog_with_list);
			// i am doing the operation for the custom dialog with the list view 
			RelativeLayout layout = (RelativeLayout)findViewById(R.id.custom_dialog_list);
			final TextView heading =(TextView)layout.findViewById(R.id.cdl_title);
			ListView listView = (ListView)layout.findViewById(R.id.cdl_listview);
			final Button button = (Button)layout.findViewById(R.id.cdl_positive);
			button.setTag("1");
			button.setOnClickListener(this);
			heading.setText(getMyTitle());
			final String[] items = new String[60];
			int temp = 1;
			for (int i = 0; i < 60; i++) {
				items[i] = Integer.toString(temp) +" " +" Mins";
				temp = temp +1;
			}
			
			listView.setAdapter(new ArrayAdapter<String>(mContext,R.layout.list_with_radio_button, items));
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position,
						long arg3) {
					alertTimeTemp = position+1;
					heading.setText(getMyTitle()+alertTimeTemp+" Mins");
					//MapViewActivity.alertTime = alertTimeTemp;
				}
			});
			
		}else{
			setContentView(R.layout.custom_dialog);
			// these is for the custom view with only alert and a button
			
			RelativeLayout layout = (RelativeLayout)findViewById(R.id.custom_dialog);
			
			final TextView heading =(TextView)layout.findViewById(R.id.cd_title);
			//final Button button = (Button)layout.findViewById(R.id.cd_positive);
			//button.setTag("2");
			heading.setText(getMyTitle().toString());
			TextView message1 = (TextView)layout.findViewById(R.id.cd_message);
			message1.setText(getMessage().toString());
			//button.setOnClickListener(this);
		}
		
	}

	@Override
	public void onClick(View v) {
		int key = Integer.parseInt(v.getTag().toString());
		switch (key) {
		case 1:
			this.dismiss();
			break;
		case 2:
			this.dismiss();
			break;
		default:
			break;
		}
	}

	
	/**
	 * @return the title
	 */
	public String getMyTitle() {
		return myTitle;
	}

	/**
	 * @param title the title to set
	 */
	public void setMyTitle(String title) {
		this.myTitle = title;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
