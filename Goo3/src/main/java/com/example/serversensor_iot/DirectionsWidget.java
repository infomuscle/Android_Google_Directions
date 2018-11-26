package com.example.serversensor_iot;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.example.serversensor_iot.GetJsonFromDirections;
import com.example.serversensor_iot.GetJsonFromPlaces;
import com.example.serversensor_iot.JsonParser;

import java.text.MessageFormat;

public class DirectionsWidget extends AppWidgetProvider {
    // 장소 변수
    String origin;                                  // 출발지명
    String destination;                             // 도착지명
    String origin_Id;                               // 출발지 place_id
    String destination_Id;                          // 도착지 place_id

    // JSON 데이터 저장 변수
    String directions_Json_Text;                    // Directions API에서 불러온 JSON 데이터를 String 형태로 저장
    String places_Json_Text_Origin;                 // Places API에서 불러온 JSON 데이터를 String 형태로 저장
    String places_Json_Text_Destination;            // Places API에서 불러온 JSON 데이터를 String 형태로 저장

    // 출력 정보 관련 변수
    String overview1;                       // 전체 경로 요약 정보 중 MainActivity 처리 부분 저장
    String overview2;                       // 전체 경로 요약 정보 중 JsonParser 처리 부분 저장
    String message;                         // MessageFormat 기본값

    protected void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                   int appWidgetId) {
//        CharSequence origin = "역삼 멀티캠퍼스";
//        CharSequence destination = "경복궁";
        origin = "역삼 멀티캠퍼스";
        destination = "잠실 롯데월드";

        // Directions API와 Places API에서 JSON 데이터를 불러옴
        try {
            // EditText에서 받은 출발지명, 목적지명을 place_id로 반환받아 저장
            places_Json_Text_Origin = new GetJsonFromPlaces().execute(origin).get();
            places_Json_Text_Destination = new GetJsonFromPlaces().execute(destination).get();
            origin_Id = new JsonParser().getPlaceId(places_Json_Text_Origin);
            destination_Id = new JsonParser().getPlaceId(places_Json_Text_Destination);

            // 반환받은 place_id 값들의 경로 JSON 데이터를 String 형태로 받아옴
            directions_Json_Text = new GetJsonFromDirections().execute(origin_Id, destination_Id).get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 전체 경로 요약 정보를 표시함
        message = "{0}부터 {1}까지\n";
        overview1 = MessageFormat.format(message, origin, destination);             // EditText에서 받은 지명 포매팅
        overview2 = new JsonParser().totalPrinter(directions_Json_Text);            // 전체 경로 요약 정보를 불러옴

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.directions_widget);
        views.setTextViewText(R.id.widget_Directions_Overview1, overview1);
        views.setTextViewText(R.id.widget_Directions_Overview2, overview2);

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