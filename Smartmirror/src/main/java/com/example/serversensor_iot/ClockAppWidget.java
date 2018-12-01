package com.example.serversensor_iot;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Implementation of App Widget functionality.
 */
public class ClockAppWidget extends AppWidgetProvider {
    static AlarmManager alarmManager;
    static PendingIntent pIntent;
    RemoteViews views;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        System.out.println("결과 확인 위젯 시계 : " + action);

        if(action.equals("android.appwidget.action.APPWIDGET_UPDATE"))
        {
            if(alarmManager != null && pIntent != null)
            {
                pIntent.cancel();
                alarmManager.cancel(pIntent);
            }
            long firstTime = System.currentTimeMillis() + 1000;
            pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC, firstTime, pIntent);

            System.out.println("결과 확인 위젯 시계 : APPWIDGET_UPDATE");
        }

        super.onReceive(context, intent);

    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfNow = new SimpleDateFormat("HH:mm");
        String strNow= sdfNow.format(date);

        System.out.println("결과 확인 위젯 시간 : 확인 " + strNow);
        String formatDate = sdfNow.format(date);

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.clock_app_widget);
        views.setTextViewText(R.id.appwidget_text, formatDate);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        if(alarmManager != null && pIntent != null)
        {
            pIntent.cancel();
            alarmManager.cancel(pIntent);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        if(alarmManager != null && pIntent != null)
        {
            pIntent.cancel();
            alarmManager.cancel(pIntent);
        }
    }

}

