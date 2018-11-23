package com.multicampus.iot.smartmirror;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.text.MessageFormat;

public class MainActivity extends AppCompatActivity {
    // UI 변수
    EditText edittext_Origin, edittext_Destination;
    Button button_Get_Direction;
    TextView textview_Overview;
    ListView listview_Stepview;

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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI 컴포넌트 지정
        edittext_Origin = (EditText) findViewById(R.id.edittext_Origin);
        edittext_Destination = (EditText) findViewById(R.id.edittext_Destination);
        button_Get_Direction = (Button) findViewById(R.id.button_Get_Direction);
        textview_Overview = (TextView) findViewById(R.id.textview_Overview);
        listview_Stepview = (ListView) findViewById(R.id.listview_Stepview);

        // 버튼 터치 시 이벤트(길찾기 수행)
        button_Get_Direction.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent agr1) {
                // 각 변수에 EditText에서 받아온 출발지명, 목적지명 저장
                origin = edittext_Origin.getText().toString();
                destination = edittext_Destination.getText().toString();

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
                textview_Overview.setText(overview1 + overview2);

                // 리스트뷰를 표시함
                setListView();

                return false;
            }
        });
    }

    // 리스트뷰를 표시함
    public void setListView(){
        step_Length = new JsonParser().stepLengthChecker(directions_Json_Text);   // 목적지까지 경로의 스텝 개수를 불러옴
        String[] steps = new String[step_Length];                               // 각 스텝의 정보를 String 형태로 저장
        
        // i번째 스텝의 정보를 배열의 i번째에 대입함
        for (int i = 0; i < step_Length; i++) {
            steps[i] = new JsonParser().stepPrinter(directions_Json_Text, i);
        }

        // 각 스텝의 정보를 표시
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, steps);
        listview_Stepview.setAdapter(adapter);
    }
}