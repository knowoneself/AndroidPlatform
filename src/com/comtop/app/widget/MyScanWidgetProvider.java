package com.comtop.app.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.comtop.app.R;
import com.google.zxing.CaptureActivity;

/**
 * 桌面widget provider�?
 * 
 * @author by xxx
 * 
 */
public class MyScanWidgetProvider extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.scan_short_layout);

		Intent intent = new Intent(context, CaptureActivity.class);

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.scanImageView, pendingIntent);
		appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
		super.onUpdate(context, appWidgetManager, appWidgetIds);

	}

}
