package com.paradigmcreatives.widget;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetActivity extends Activity {
	SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		    final int N = appWidgetIds.length;
	    Log.i("ExampleWidget",  "Updating widgets " + Arrays.asList(appWidgetIds));
	    
		    for (int i = 0; i < N; i++) {
		      int appWidgetId = appWidgetIds[i];
		      
		      Intent intent = new Intent(context, WidgetExampleActivity.class);
		      PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		     
		      RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget1_info);
		      views.setOnClickPendingIntent(R.id.button, pendingIntent);
		      
		      views.setTextViewText(R.id.widget1label, df.format(new Date(appWidgetId)));
		     
		      appWidgetManager.updateAppWidget(appWidgetId, views);
		    }
		  }
}