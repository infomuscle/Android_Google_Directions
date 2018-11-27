package com.example.serversensor_iot;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.MessageFormat;

public class DirectionsPopup  extends Activity {
    // UI 변수
    private Button button_Ok;
    private Button button_Close;
    private EditText edittext_Origin_Popup;
    private EditText edittext_Destination_Popup;

    private RemoteViews remoteViews;
    private AppWidgetManager awm;
    private ComponentName thisWidget;
    private Intent listviewIntent;

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
    int step_Length;                        // JSON 데이터 중 "steps" 키가 갖는 값의 길이 저장(목적지까지 경로의 스텝 개수)
    String overview1;                       // 전체 경로 요약 정보 중 MainActivity 처리 부분 저장
    String overview2;                       // 전체 경로 요약 정보 중 JsonParser 처리 부분 저장
    String message;                         // MessageFormat 기본값
    String[] steps;
    String stepview_text;

    Context c;

    //기본값 설정
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.directions_popup);

        button_Ok = (Button) findViewById(R.id.button_Ok);
        button_Close = (Button) findViewById(R.id.button_Close);
        edittext_Origin_Popup = (EditText)findViewById(R.id.edittext_Origin_Popup);
        edittext_Destination_Popup = (EditText)findViewById(R.id.edittext_Destination_Popup);

        c = getApplicationContext();
        awm = AppWidgetManager.getInstance(this);

        button_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    // 각 변수에 EditText에서 받아온 출발지명, 목적지명 저장
                    origin = edittext_Origin_Popup.getText().toString();
                    destination = edittext_Destination_Popup.getText().toString();

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
//                    message = "{0}부터 {1}까지";
//                    overview1 = MessageFormat.format(message, origin, destination);             // EditText에서 받은 지명 포매팅
                    overview2 = new JsonParser().totalPrinter(directions_Json_Text);            // 전체 경로 요약 정보를 불러옴

                    // 스텝별 경로를 표시함
                    step_Length = new JsonParser().stepLengthChecker(directions_Json_Text);   // 목적지까지 경로의 스텝 개수를 불러옴
                    steps = new String[step_Length];                               // 각 스텝의 정보를 String 형태로 저장
                    stepview_text = "";
                    // i번째 스텝의 정보를 배열의 i번째에 대입함
                    for (int i = 0; i < step_Length; i++) {
                        steps[i] = new JsonParser().stepPrinter(directions_Json_Text, i);
                        stepview_text += steps[i] + "\n";
                    }

//                    overviewPrinter(c, awm, overview1, overview2, stepview_text);
                    overviewPrinter(c, awm, origin, destination, overview2, stepview_text);

//                stepviewPrinter(c, awm, steps);

                    finish();
                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "올바른 장소명을 입력하세요", Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        });

        button_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

    public void overviewPrinter(Context context, AppWidgetManager appWidgetManager, String ov1, String ov2, String time, String sv)
    {
        this.awm = appWidgetManager;
        thisWidget = new ComponentName(context, DirectionsWidget.class);
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.directions_widget);
        remoteViews.setTextViewText(R.id.widget_Origin, ov1);
        remoteViews.setTextViewText(R.id.widget_Particle1, "에서 ");
        remoteViews.setTextViewText(R.id.widget_Destination, ov2);
        remoteViews.setTextViewText(R.id.widget_Particle2, "까지");
        remoteViews.setTextViewText(R.id.widget_Time, time);
//        remoteViews.setTextViewText(R.id.widget_Directions_Overview1, ov1);
//        remoteViews.setTextViewText(R.id.widget_Directions_Overview2, ov2);
        remoteViews.setTextViewText(R.id.widget_Directions_Stepview, sv);
        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
    }
}
