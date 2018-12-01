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

public class DirectionsWidget extends AppWidgetProvider {

    // UI 변수
    private Button button_Ok;                       // 팝업 내 확인 버튼
    private Button button_Close;                    // 팝업 내 취소 버튼
    private EditText edittext_Origin_Popup;         // 팝업 내 출발지 입력 구간
    private EditText edittext_Destination_Popup;    // 팝업 내 도착지 입력 구간

    private RemoteViews remoteViews;
    private AppWidgetManager awm;
    private ComponentName thisWidget;
    private Intent listviewIntent;
    private Context c;

    // 장소 변수 (입력)
    String origin;                                  // 출발지명
    String destination;                             // 도착지명
    String origin_Id;                               // 출발지 place_id
    String destination_Id;                          // 도착지 place_id

    // JSON 데이터 저장 변수
    String directions_Json_Text;                    // Directions API에서 불러온 JSON 데이터를 String 형태로 저장
    String places_Json_Text_Origin;                 // Places API에서 불러온 JSON 데이터를 String 형태로 저장
    String places_Json_Text_Destination;            // Places API에서 불러온 JSON 데이터를 String 형태로 저장

    // 정보 변수 (출력)
    String total_Duration;                          // 전체 경로 소요시간
    String total_Distance;                          // 전체 경로 거리
    String total_Distance_Format;                   // 전체 경로 거리 포매팅 변수
    String total_Departure_Time;                    // 전체 경로 출발 시간
    String total_Arrival_Time;                      // 전체 경로 도착 시간

    String step_Duration;                           // 스텝별 경로 소요시간
    String step_Distance;                           // 스텝별 경로 거리
    String step_Travel_Mode;                        // 스텝별 경로 도보 or 대중교통
    String step_Transit;                            // 스텝별 경로 이동수단
    String step_LineNumber;                         // 스텝별 경로 버스 및 지하철 노선
    String step_Departure_Stop;                     // 스텝별 경로 출발 정류장
    String step_Departure_Time;                     // 스텝별 경로 출발 시간
    String[] first_Line_Split;

    int step_Length;                        // JSON 데이터 중 "steps" 키가 갖는 값의 길이 저장(목적지까지 경로의 스텝 개수)
    String message;                         // MessageFormat 기본값
    String[] steps;                         // 스텝별 경로 정보 String 담아두는 배열
    String stepview_Text;                   // 최종적으로 출력할 스텝별 경로의 합
    int flag_First = 1;

    Calendar now_Calendar;                          // 버튼을 클릭하는 현재 시간
    Calendar arrival_Calendar;                      // 현재 시간 + 예상 소요시간
    SimpleDateFormat sdf;                           // 시간 표시 포맷
    String total_Duration_Value;                    // 소요시간의 초(Second) 값
    int total_Duration_Value_Int;                   // 소요시간의 초(Second) 값
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