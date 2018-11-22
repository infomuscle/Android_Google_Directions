package com.multicampus.iot.smartmirror;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;

public class JsonParser {

    // JSON 데이터 중 "steps" 키가 갖는 값의 길이 저장(목적지까지 경로의 스텝 개수)
    int step_Length = 0;

    // JSON 데이터에서 필요한 자료만을 찾아 가공된 정보(각 스텝) 저장
    String result = null;
    String message = null;

    String start_Address = null;            // 최초 출발지
    String end_Address = null;              // 최종 도착지
    String total_Distance = null;           // 총 거리
    String total_Duration = null;           // 총 소요 시간
    String total_Departure_Time = null;     // 출발 시간
    String total_Arrival_Time = null;       // 도착 시간

    String step_Travel_Mode = null;         // 이동 수단(도보 = WALKING, 대중교통 = TRANSIT)

    String step_Distance = null;            // 각 스텝별 거리
    String step_Duration = null;            // 각 스텝별 소요 시간
    String step_Html_Instruction = null;    // 각 스텝별 이동 방법
    String step_Departure_Stop = null;      // 각 스텝별 출발지
    String step_Departure_Time = null;      // 각 스텝별 출발 시간
    String step_Arrival_Stop = null;        // 각 스텝별 도착지
    String step_Arrival_Time = null;        // 각 스텝별 도착 시간
    String step_Line_Number = null;         // 버스 번호, 지하철 호선

    // 총 스텝의 개수를 반환하는 메소드
    public int stepLengthChecker(String jsonString){
        try {
            JSONObject routes = new JSONObject(jsonString).getJSONArray("routes").getJSONObject(0);
            JSONObject legs = routes.getJSONArray("legs").getJSONObject(0);
            JSONArray steps = legs.getJSONArray("steps");
            step_Length = steps.length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return step_Length;
    }

    // 총 정보를 요약 가공하여 반환하는 메소드(최초 출발지, 최종 도착지 등, 총 거리, 총 소요시간 등)
    public String totalPrinter(String jsonString){
        try{
            JSONObject routes = new JSONObject(jsonString).getJSONArray("routes").getJSONObject(0);
            JSONObject legs = routes.getJSONArray("legs").getJSONObject(0);

            start_Address = legs.optString("start_address");
            end_Address = legs.optString("end_address");
            total_Distance = legs.getJSONObject("distance").optString("text");
            total_Duration = legs.getJSONObject("duration").optString("text");
            total_Departure_Time = legs.getJSONObject("departure_time").optString("text");
            total_Arrival_Time = legs.getJSONObject("arrival_time").optString("text");

            message = "{0}부터 {1}까지 {2}, {3} 소요\n{4} 출발 시 {5} 도착 예정\n\n";
            result = MessageFormat.format(message, start_Address, end_Address, total_Distance, total_Duration, total_Departure_Time, total_Arrival_Time);
        } catch (JSONException e){
            e.printStackTrace();
        }
        return result;
    }

    // 각 스텝별 정보를 요약 가공하여 반환하는 메소드(스텝별 출발지, 스텝별 도착지 등)
    // MainActivity에서 반복문을 이용해 전체 스텝의 idx번째에 접근
    public String stepPrinter(String jsonString, int idx) {
        try {
            // 총 경로의 정보에 접근
            JSONObject routes = new JSONObject(jsonString).getJSONArray("routes").getJSONObject(0);
            JSONObject legs = routes.getJSONArray("legs").getJSONObject(0);

            start_Address = legs.optString("start_address");
            end_Address = legs.optString("end_address");
            total_Distance = legs.getJSONObject("distance").optString("text");
            total_Duration = legs.getJSONObject("duration").optString("text");
            total_Departure_Time = legs.getJSONObject("departure_time").optString("text");
            total_Arrival_Time = legs.getJSONObject("arrival_time").optString("text");

            // 스텝별 경로의 정보에 접근
            JSONArray steps = legs.getJSONArray("steps");
            JSONObject step = steps.getJSONObject(idx);

            step_Html_Instruction = step.optString("html_instructions");
            step_Duration = step.getJSONObject("duration").optString("text");
            step_Distance = step.getJSONObject("distance").optString("text");
            step_Travel_Mode = step.optString("travel_mode");

            // 대중교통(버스, 지하철)으로 이동할 때
            if (step_Travel_Mode.equals("TRANSIT")){
                // 대중교통 상세 정보에 접근
                JSONObject transit_details = step.getJSONObject("transit_details");
                step_Line_Number = transit_details.getJSONObject("line").optString("name");
                step_Departure_Stop = transit_details.getJSONObject("departure_stop").optString("name");
                step_Departure_Time = transit_details.getJSONObject("departure_time").optString("text");
                step_Arrival_Stop = transit_details.getJSONObject("arrival_stop").optString("name");
                step_Arrival_Time = transit_details.getJSONObject("arrival_time").optString("text");

                message = "- {6} {7} 탑승, {8} 소요({9})\n  ({10}에서 {11} 승차, {12}에 {13} 하차)";
                result = MessageFormat.format(message, start_Address, end_Address, total_Distance, total_Duration, total_Departure_Time, total_Arrival_Time, step_Html_Instruction, step_Line_Number, step_Duration, step_Distance, step_Departure_Stop, step_Departure_Time, step_Arrival_Stop, step_Arrival_Time);
            }

            // 도보로 이동할 때
            else{
                message = "- {6}, {7} 소요({8})";
                result = MessageFormat.format(message, start_Address, end_Address, total_Distance, total_Duration, total_Departure_Time, total_Arrival_Time, step_Html_Instruction, step_Duration, step_Distance);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
