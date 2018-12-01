package com.example.serversensor_iot;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class DirectionsWidget extends AppWidgetProvider {
    protected void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                   int appWidgetId) {
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.directions_widget);

        // 팝업
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.setComponent(new ComponentName(context, DirectionsPopup.class));
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
        views.setOnClickPendingIntent(R.id.layout_Directions_Widget, pi);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTime(context, appWidgetManager), 5000, 1000);


        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

class MyTime extends TimerTask {
    RemoteViews remoteviews;
    AppWidgetManager appwidgetmanager;
    ComponentName componentname;

    MyTime(Context context, AppWidgetManager appWidgetManager){
        appwidgetmanager = appWidgetManager;
        remoteviews = new RemoteViews(context.getPackageName(), R.layout.directions_widget);
        componentname = new ComponentName(context, DirectionsWidget.class);
    }

    @Override
    public void run() {
        remoteviews.setTextViewText(R.id.widget_Directions_Stepview_Others, (new Date(System.currentTimeMillis())).toLocaleString());
        appwidgetmanager.updateAppWidget(componentname, remoteviews);
    }


}
