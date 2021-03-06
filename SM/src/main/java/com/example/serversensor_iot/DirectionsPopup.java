package com.example.serversensor_iot;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;

public class DirectionsPopup  extends Activity {
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

    //기본값 설정
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.directions_popup);

        // UI 컴포넌트 변수 설정
        button_Ok = (Button) findViewById(R.id.button_Ok);
        button_Close = (Button) findViewById(R.id.button_Close);
        edittext_Origin_Popup = (EditText)findViewById(R.id.edittext_Origin_Popup);
        edittext_Destination_Popup = (EditText)findViewById(R.id.edittext_Destination_Popup);

        // 리모트뷰 사용하기 위한 변수
        c = getApplicationContext();
        awm = AppWidgetManager.getInstance(this);

        button_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 각 변수에 EditText에서 받아온 출발지명, 목적지명 저장
                origin = edittext_Origin_Popup.getText().toString();
                destination = edittext_Destination_Popup.getText().toString();

                getDirection();
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

    public void getDirection() {
        try {


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

            // 전체 경로 요약 정보를 추출함
            total_Duration = new JsonParser().getTotalDuration(directions_Json_Text);
            total_Duration_Value = new JsonParser().getTotalDurationValue(directions_Json_Text);
            total_Duration_Value_Int = Integer.parseInt(total_Duration_Value);

            total_Distance = new JsonParser().getTotalDistance(directions_Json_Text);
            message = " 소요 ({0})";
            total_Distance_Format = MessageFormat.format(message, total_Distance);

            now_Calendar = Calendar.getInstance();
            arrival_Calendar = Calendar.getInstance();
            arrival_Calendar.add(Calendar.SECOND, total_Duration_Value_Int);
            sdf = new SimpleDateFormat("a hh:mm");
            total_Departure_Time = sdf.format(now_Calendar.getTime());
            total_Arrival_Time = sdf.format(arrival_Calendar.getTime());

            // 전체 경로 요약 정보를 표시함
            overviewPrinter(c, awm, origin, destination, total_Duration, total_Distance_Format, total_Departure_Time, total_Arrival_Time);

            // 스텝별 경로를 추출함
            step_Length = new JsonParser().stepLengthChecker(directions_Json_Text);   // 목적지까지 경로의 스텝 개수를 불러옴
            steps = new String[step_Length];                               // 각 스텝의 정보를 String 형태로 저장
            stepview_Text = "";
            // i번째 스텝의 정보를 배열의 i번째에 대입함
            for (int i = 0; i < step_Length; i++) {
                steps[i] = new JsonParser().stepPrinter(directions_Json_Text, i);
                step_Travel_Mode = new JsonParser().getStepTravelMode(directions_Json_Text, i);
                if (step_Travel_Mode.equals("WALKING") == false && flag_First == 1) {
                    step_Transit = new JsonParser().getStepTransit(directions_Json_Text, i);
                    step_LineNumber = new JsonParser().getStepLineNumber(directions_Json_Text, i);
                    if (step_Transit.equals("버스")) {
                        step_LineNumber += "번";
                    }
                    step_Departure_Stop = new JsonParser().getStepDepartureStop(directions_Json_Text, i);
                    if (step_Transit.equals("지하철")) {
                        step_Departure_Stop += "역";
                    }
                    step_Departure_Time = new JsonParser().getStepDepartureTime(directions_Json_Text, i);
                    first_Line_Split = steps[i].split("\n");
                    stepview_Text += steps[i] + "\n";
                    flag_First = 0;
                } else {
                    if (i == step_Length-1 && step_Travel_Mode.equals("WALKING")){
                        step_Distance = new JsonParser().getStepDistance(directions_Json_Text, i);
                        step_Duration = new JsonParser().getStepDuration(directions_Json_Text, i);
                        stepview_Text += destination + "까지 도보\n   >> " + step_Duration + " 소요 (" + step_Distance + ")";
                    } else {
                        stepview_Text += steps[i] + "\n";
                    }
                }
            }

            // 스텝별 경로를 표시함
            stepviewPrinter(c, awm, step_Transit, step_LineNumber, step_Departure_Stop, step_Departure_Time, stepview_Text);

            finish();
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "올바른 장소명을 입력하세요", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    // TextView에 전체 경로를 표시하는 메소드 (현재는 스텝별 경로도 TextView로 표시)
    public void overviewPrinter(Context ctxt, AppWidgetManager appWidgetManager, String orgn, String dstn, String drtn, String dstnce, String dptr, String arvl)
    {
        this.awm = appWidgetManager;
        thisWidget = new ComponentName(ctxt, DirectionsWidget.class);
        remoteViews = new RemoteViews(ctxt.getPackageName(), R.layout.directions_widget);

        // LinearLayout id:widget_Overview1
        remoteViews.setTextViewText(R.id.widget_Particle1, "출발 ");
        remoteViews.setTextViewText(R.id.widget_Origin, orgn);

        // LinearLayout id:widget_Overview2
        remoteViews.setTextViewText(R.id.widget_Particle2, "도착 ");
        remoteViews.setTextViewText(R.id.widget_Destination, dstn);
        remoteViews.setViewVisibility(R.id.widget_Overview2,View.VISIBLE);

        // LinearLayout id:widget_Overview3
        remoteViews.setTextViewText(R.id.widget_Total_Duration, drtn);
        remoteViews.setTextViewText(R.id.widget_Total_Distance, dstnce);
        remoteViews.setViewVisibility(R.id.widget_Overview3,View.VISIBLE);

        // LinearLayout id:widget_Overview4
        remoteViews.setTextViewText(R.id.widget_Total_Departure_Time, dptr);
        remoteViews.setTextViewText(R.id.widget_Particle3, " - ");
        remoteViews.setTextViewText(R.id.widget_Total_Arrival_Time, arvl);
        remoteViews.setViewVisibility(R.id.widget_Overview4,View.VISIBLE);

        appWidgetManager.updateAppWidget(thisWidget, remoteViews);
    }

    public void stepviewPrinter(Context ctxt, AppWidgetManager appWidgetManager, String trnst, String lnnm, String dptrstp, String dptrtm, String othr){
        this.awm = appWidgetManager;
        thisWidget = new ComponentName(ctxt, DirectionsWidget.class);
        remoteViews = new RemoteViews(ctxt.getPackageName(), R.layout.directions_widget);

        // LinearLayout id:widget_Overview_First1
        remoteViews.setTextViewText(R.id.widget_Directions_Stepview_Transit, trnst);
        remoteViews.setTextViewText(R.id.widget_Particle5, " ");
        remoteViews.setTextViewText(R.id.widget_Directions_Stepview_LineNumber, lnnm);
        remoteViews.setTextViewText(R.id.widget_Particle6, " ");
        remoteViews.setTextViewText(R.id.widget_Directions_Stepview_Departure_Time, dptrtm);
        remoteViews.setViewVisibility(R.id.widget_Overview_First1,View.VISIBLE);

        // LinearLayout id:widget_Overview_First2
        remoteViews.setTextViewText(R.id.widget_Directions_Stepview_Departure_Stop, dptrstp);
        remoteViews.setTextViewText(R.id.widget_Particle7, " 승차");
        remoteViews.setViewVisibility(R.id.widget_Overview_First2,View.VISIBLE);

        // Stepview
        remoteViews.setTextViewText(R.id.widget_Directions_Stepview_Others, othr);

        appWidgetManager.updateAppWidget(thisWidget, remoteViews);

    }
}
