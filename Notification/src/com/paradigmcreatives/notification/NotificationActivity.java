package com.paradigmcreatives.notification;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class NotificationActivity extends Activity {
    private PendingIntent contentIntent;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    public void createNotification(View view) {
    	Notification noti = new Notification(android.R.drawable.btn_star_big_on, "Notifications", System.currentTimeMillis() );
    	noti.flags = Notification.FLAG_AUTO_CANCEL;


    	Intent notificationIntent = new Intent(this, NotificationReceiver.class);
    	notificationIntent.setAction(Intent.ACTION_MAIN);
    	notificationIntent = notificationIntent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
    	contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

    	noti.setLatestEventInfo(this, "This will take u into the next location", "Notification Receiver", contentIntent); }
}