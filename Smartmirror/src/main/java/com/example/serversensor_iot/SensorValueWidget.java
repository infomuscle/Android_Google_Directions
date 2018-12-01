package com.example.serversensor_iot;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Implementation of App Widget functionality.
 */
public class SensorValueWidget extends AppWidgetProvider {
    static AlarmManager alarmManager;
    static DbHelp dbHelp;
    static PendingIntent pIntent;
    RemoteViews views;

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        System.out.println("결과 확인 위젯 센서 : " + action);

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

//            PackageManager pm=context.getPackageManager();
//            List<ResolveInfo> matches=pm.queryBroadcastReceivers(intent, 0);
//            for (ResolveInfo resolveInfo : matches) {
//                Intent explicit=new Intent("android.appwidget.action.UPDATE_SENSOR");
//                ComponentName cn= new ComponentName(resolveInfo.activityInfo.applicationInfo.packageName, resolveInfo.activityInfo.name);
//                explicit.setComponent(cn);
//                context.sendBroadcast(explicit);

             }

            System.out.println("결과 확인 위젯 센서 : APPWIDGET_UPDATE");


//        if(action.equals("android.appwidget.action.UPDATE_SENSOR"))
//        {
////            long firstTime = System.currentTimeMillis() + 1000;
////            pIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
////            alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
////            alarmManager.set(AlarmManager.RTC, firstTime, pIntent);
//            System.out.println("결과 확인 위젯 센서 : UPDATE_SENSOR");
//        }


        super.onReceive(context, intent);

    }

    private void removeAlarmManager()
    {

    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        try {
            CharSequence widgetText = context.getString(R.string.appwidget_text);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.sensor_value_widget);

            views.setTextViewText(R.id.textView_FindDustappwidget_text, "미세먼지 : " + dbHelp.returnDust() );
            System.out.println("결과 확인 위젯 센서 : 갱신 "+ dbHelp.returnDust());
            views.setTextViewText(R.id.textView_Temperatureappwidget_text, "온        도 : " + dbHelp.returnTemperature() + "도");
            System.out.println("결과 확인 위젯 센서 : 갱신 "+ dbHelp.returnTemperature());
            views.setTextViewText(R.id.textView_Humidityappwidget_text, "습        도 : " + dbHelp.returnHumidity() + "%");
            System.out.println("결과 확인 위젯 센서 : 갱신 "+ dbHelp.returnHumidity());

            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            i.setComponent(new ComponentName(context, PopupActivity.class));
            PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
            views.setOnClickPendingIntent(R.id.layout_Popup_Main_Senser, pi);

            // Instruct the widget manager to update the widget
            System.out.println("결과 확인 위젯 센서 : updateAppWidget");
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        catch (Exception e)
        {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsStrting = sw.toString();
            System.out.println("결과 확인 위젯 센서 : 오류 " + exceptionAsStrting);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        try {
            if(dbHelp == null)
                dbHelp = new DbHelp(context, "PROJECTIOT_DB.db", null, 1);
//
            System.out.println("결과 확인 위젯 센서 : onUpdate");
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId);
            }
        }
        catch (Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsStrting = sw.toString();
            System.out.println("결과 확인 위젯 센서 : 오류" + exceptionAsStrting);
        }
    }
//
//    // 갱신을 위한 내용
//    class MyTime extends TimerTask{
//        private RemoteViews remoteViews;
//        private AppWidgetManager appwidgetManager;
//        private ComponentName thisWidget;
//        private DbHelp dbHelp;
//        Context context;
//        public MyTime(Context context, AppWidgetManager appWidgetManager)
//        {
//            this.context = context;
//            this.appwidgetManager = appWidgetManager;
//            remoteViews = new RemoteViews(context.getPackageName(), R.layout.sensor_value_widget);
//            thisWidget = new ComponentName(context, SensorValueWidget.class);
//        }
//
//        @Override
//        public void run() {
//            try {
//
//                remoteViews.setTextViewText(R.id.textView_FindDustappwidget_text, "미세먼지 : " + dbHelp.returnDust() );
//                System.out.println("결과 확인 위젯 센서 : 갱신 "+ dbHelp.returnDust());
//                remoteViews.setTextViewText(R.id.textView_Temperatureappwidget_text, "온        도 : " + dbHelp.returnTemperature() + "도");
//                System.out.println("결과 확인 위젯 센서 : 갱신 "+ dbHelp.returnTemperature());
//                remoteViews.setTextViewText(R.id.textView_Humidityappwidget_text, "습        도 : " + dbHelp.returnHumidity() + "%");
//                System.out.println("결과 확인 위젯 센서 : 갱신 "+ dbHelp.returnHumidity());
//
//                Intent i = new Intent(Intent.ACTION_MAIN);
//                i.addCategory(Intent.CATEGORY_LAUNCHER);
//                i.setComponent(new ComponentName(context, PopupActivity.class));
//                PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);
//                remoteViews.setOnClickPendingIntent(R.id.layout_Popup_Main_Senser, pi);
//
//                appwidgetManager.updateAppWidget(thisWidget,remoteViews);
//                System.out.println("결과 확인 위젯 센서 : 갱신");
//            }
//            catch (Exception e){
//                StringWriter sw = new StringWriter();
//                e.printStackTrace(new PrintWriter(sw));
//                String exceptionAsStrting = sw.toString();
//                System.out.println("결과 확인 위젯 : 오류" + exceptionAsStrting);
//            }
//        }
//    }

    @Override
    public void onEnabled(Context context) {
        if(alarmManager != null && pIntent != null)
        {
            pIntent.cancel();
            alarmManager.cancel(pIntent);
        }
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        if(alarmManager != null && pIntent != null)
        {
            pIntent.cancel();
            alarmManager.cancel(pIntent);
        }
        // Enter relevant functionality for when the last widget is disabled
    }

}

