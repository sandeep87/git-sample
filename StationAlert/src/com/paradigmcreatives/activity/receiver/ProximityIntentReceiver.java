package com.paradigmcreatives.activity.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import com.paradigmcreatives.activity.AlarmActivity;

public class ProximityIntentReceiver extends BroadcastReceiver {
	@Override
	public void onReceive (Context context, Intent intent) {

		System.out.println("onRecieve of ProximityIntentReceiver");

		String key = LocationManager.KEY_PROXIMITY_ENTERING;
		Boolean entering = intent.getBooleanExtra(key, false);
		// what should be shown once the USER enter the location

		Intent intent2 = new Intent(context, AlarmActivity.class);
		intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(intent2);

	}
}