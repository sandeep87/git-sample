package in.ccl.ui;

import in.ccl.adapters.ScheduleAdapter;
import in.ccl.helper.Util;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class ScheduleActivity extends TopActivity {

	private ListView listSchedule;

	private ScheduleAdapter scheduleAdapter;

	private String date[] = { "07 FEB", "08 FEB", "09 FEB", "10 FEB" };

	private String day[] = { "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY" };

	private String place[] = { "HYDERABAD", "VIZAG", "CHENNAI", "DELHI" };

	private String players[] = { "TELUGU WARRIORS vs KARNATAKA", "KERALA vs MUMBAI HEROS", "TELUGU WARRIORS vs KARNATAKA", "KERALA vs MUMBAI HEROS" };

	private String time[] = { "10am - 2pm", "3pm - 7pm", "10am - 2pm", "3pm - 7pm" };

	@Override
	public void onCreate (Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContent(R.layout.shedule_layout);
		TextView scheduleTitle =  (TextView)findViewById(R.id.schedule_title);
		Util.setTextFont(this, scheduleTitle);
		listSchedule = (ListView) findViewById(R.id.list_schedule);
		scheduleAdapter = new ScheduleAdapter(ScheduleActivity.this, date, day, place, players, time);
		listSchedule.setAdapter(scheduleAdapter);
		listSchedule.setFadingEdgeLength(1);
		listSchedule.setCacheColorHint(Color.TRANSPARENT);

	}

}
