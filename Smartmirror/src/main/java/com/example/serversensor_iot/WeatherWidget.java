package com.example.serversensor_iot;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.RemoteViews;

import java.util.concurrent.ExecutionException;

/**
 * Implementation of App Widget functionality.
 */
public class WeatherWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        String weather_Receive_String = "";
        String[] weather_Parsed_String_Array = new String[4];

        String finedust_Recieve_String = "";
        Bitmap weather_icon = null;


        try {
            weather_Receive_String = new WeatherTask().execute().get();
            finedust_Recieve_String = new FinedustApiTask().execute().get();

            weather_Parsed_String_Array = new WeatherParser().weatherParser(weather_Receive_String);
            weather_icon = new WeatherIconLoad(weather_Parsed_String_Array[1]).execute().get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e ) {
            e.printStackTrace();
        }

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
        views.setTextViewText(R.id.textview_Extra_Finedust, " 외부 미세먼지 : \n" + finedust_Recieve_String);
        views.setTextViewText(R.id.textview_Weather, weather_Parsed_String_Array[0]);
        views.setImageViewBitmap(R.id.imageview_Weather, weather_icon);
        views.setTextViewText(R.id.textview_Temperature, weather_Parsed_String_Array[2] + "°C");
        views.setTextViewText(R.id.textview_Humidity, " 습       도 : " + weather_Parsed_String_Array[3] + "%");
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
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}