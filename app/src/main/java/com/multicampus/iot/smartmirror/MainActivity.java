package com.multicampus.iot.smartmirror;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    EditText edit_origin, edit_destin;
    Button btn;
    String origin = "서울역";
    String destin = "서울대학교";
    String origin_ID = null;
    String destin_ID = null;

    String directionsJsonText = null;      // Directions API에서 불러온 JSON 데이터를 String 형태로 저장
    String placesJsonTextOrigin = null;           // Places API에서 불러온 JSON 데이터를 String 형태로 저장
    String placesJsonTextDestin = null;           // Places API에서 불러온 JSON 데이터를 String 형태로 저장

    int step_Length;               // JSON 데이터 중 "steps" 키가 갖는 값의 길이 저장(목적지까지 경로의 스텝 개수)
    String overview1;
    String overview2;
    String message;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edit_origin = (EditText) findViewById(R.id.editOrigin);
        edit_destin = (EditText) findViewById(R.id.editDestin);
        btn = (Button) findViewById(R.id.btn1);

        btn.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent agr1) {
                origin = edit_origin.getText().toString();
                destin = edit_destin.getText().toString();

                try {
                    // 에디트텍스트의 문자열을 place_ID로 변환함
                    placesJsonTextOrigin = new GetJsonFromPlaces().execute(origin).get();
                    placesJsonTextDestin = new GetJsonFromPlaces().execute(destin).get();
                    origin_ID = new JsonParser().getPlaceID(placesJsonTextOrigin);
                    destin_ID = new JsonParser().getPlaceID(placesJsonTextDestin);

                    // url에서 JSON 데이터를 String 형태로 받아옴
                    directionsJsonText = new GetJsonFromDirections().execute(origin_ID, destin_ID).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 전체 경로 요약 정보를 표시
                message = "{0}부터 {1}까지\n";
                overview1 = MessageFormat.format(message, origin, destin);
                overview2 = new JsonParser().totalPrinter(directionsJsonText);           // 전체 경로 요약 정보를 불러옴
                TextView tv = (TextView) findViewById(R.id.textView1);
                tv.setText(overview1 + overview2);

                setListView();

                return false;
            }
        });
    }

    // 리스트뷰를 표시함
    public void setListView(){
        step_Length = new JsonParser().stepLengthChecker(directionsJsonText);   // 목적지까지 경로의 스텝 개수를 불러옴
        String[] steps = new String[step_Length];                               // 각 스텝의 정보를 String 형태로 저장
        // i번째 스텝의 정보를 배열의 i번째에 대입함
        for (int i = 0; i < step_Length; i++) {
            steps[i] = new JsonParser().stepPrinter(directionsJsonText, i);
        }

        // 각 스텝의 정보를 표시
        ListView list = (ListView) findViewById(R.id.listView1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, steps);
        list.setAdapter(adapter);
    }
}