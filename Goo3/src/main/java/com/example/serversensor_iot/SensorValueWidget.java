package com.example.serversensor_iot;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Implementation of App Widget functionality.
 */
public class SensorValueWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.sensor_value_widget);
        views.setTextViewText(R.id.textView_FindDustappwidget_text, "미세먼지 : ");
        views.setTextViewText(R.id.textView_Temperatureappwidget_text, "온       도 : ");
        views.setTextViewText(R.id.textView_Humidityappwidget_text, "습       도 : ");
        // Instruct the widget manager to update the widget

        Intent i = new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        i.setComponent(new ComponentName(context, PopupActivity.class));
        PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
        views.setOnClickPendingIntent(R.id.layout_Popup_Main_Senser, pi);

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

    // 갱신을 위한 내용
    class MyTime extends TimerTask{
        private RemoteViews remoteViews;
        private AppWidgetManager appwidgetManager;
        private ComponentName thisWidget;

        public MyTime(Context context, AppWidgetManager appWidgetManager)
        {
            this.appwidgetManager = appWidgetManager;
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.sensor_value_widget);
            thisWidget = new ComponentName(context, SensorValueWidget.class);
        }

        @Override
        public void run() {
            System.out.println("결과 확인 위젯 : 갱신");
            remoteViews.setTextViewText(R.id.textView_FindDustappwidget_text, "미세먼지 : " );
            remoteViews.setTextViewText(R.id.textView_Temperatureappwidget_text     , "온        도 : " + ((MainActivity) MainActivity.mContext).getTemperature());
            remoteViews.setTextViewText(R.id.textView_Humidityappwidget_text     , "습        도 : " + ((MainActivity) MainActivity.mContext).getHumidity());
            appwidgetManager.updateAppWidget(thisWidget, remoteViews);
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

