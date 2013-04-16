package com.paradigmcreatives.widgetdemo;

import java.util.Random;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetDemoActivity extends AppWidgetProvider {
	private static final String LOG = "de.vogella.android.widget.example";

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		ComponentName thisWidget = new ComponentName(context,
				WidgetDemoActivity.class);
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		for (int widgetId : allWidgetIds) {
			// Create some random data
			int number = (new Random().nextInt(100));

			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.main);
			Log.w("WidgetExample", String.valueOf(number));
			// Set the text
			remoteViews.setTextViewText(R.id.text_widget,
					String.valueOf(number));

			// Register an onClickListener
			Intent intent = new Intent(context, WidgetDemoActivity.class);

			intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews
					.setOnClickPendingIntent(R.id.text_widget, pendingIntent);
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
	}
}