package com.multicampus.iot.smartmirror;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    EditText edit_origin, edit_destin;

    String directionsJsonText = null;      // Directions API에서 불러온 JSON 데이터를 String 형태로 저장
    String placesJsonTextOrigin = null;           // Places API에서 불러온 JSON 데이터를 String 형태로 저장
    String placesJsonTextDestin = null;           // Places API에서 불러온 JSON 데이터를 String 형태로 저장
    int step_Length;               // JSON 데이터 중 "steps" 키가 갖는 값의 길이 저장(목적지까지 경로의 스텝 개수)
    String overview;

    //        edit_origin = (EditText) findViewById(R.id.editOrigin);
    //        edit_destin = (EditText) findViewById(R.id.editDestin);
    //        String origin = null;
    //        String destin = null;

    String test_origin = "잠실역";
    String test_destin = "성균관대학교";

    String origin_ID = null;
    String destin_ID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
//            origin = edit_origin.getText().toString();
//            destin = edit_origin.getText().toString();
            placesJsonTextOrigin = new GetJsonFromPlaces().execute(test_origin).get();
            placesJsonTextDestin = new GetJsonFromPlaces().execute(test_destin).get();
            origin_ID = new JsonParser().getPlaceID(placesJsonTextOrigin);
            destin_ID = new JsonParser().getPlaceID(placesJsonTextDestin);

            directionsJsonText = new GetJsonFromDirections().execute(origin_ID, destin_ID).get();       // url에서 JSON 데이터를 String 형태로 받아옴

            overview = new JsonParser().totalPrinter(directionsJsonText);           // 전체 경로 요약 정보를 불러옴
            step_Length = new JsonParser().stepLengthChecker(directionsJsonText);   // 목적지까지 경로의 스텝 개수를 불러옴
            String[] steps = new String[step_Length];                               // 각 스텝의 정보를 String 형태로 저장

//             i번째 스텝의 정보를 배열의 i번째에 대입함
            for (int i=0;i<step_Length;i++){
                steps[i] = new JsonParser().stepPrinter(directionsJsonText, i);
            }

            // 전체 경로 요약 정보를 표시
            TextView tv = (TextView) findViewById(R.id.textView1);
            tv.setText(overview);

            // 각 스텝의 정보를 표시
            ListView list = (ListView) findViewById(R.id.listView1);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, steps);
            list.setAdapter(adapter);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}