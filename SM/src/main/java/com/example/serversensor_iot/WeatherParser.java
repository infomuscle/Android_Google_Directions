package com.example.serversensor_iot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherParser {

    private String parsed_Weather = null;
    private String parsed_Temperature = null;
    private String parsed_Humidity = null;

    public String[] weatherParser(String jsonString) {

        String[] weather_Result_Array = new String[3];

        try {
            JSONArray json_Array = new JSONObject(jsonString).getJSONArray("weather");

            JSONObject jObject = json_Array.getJSONObject(0);

            parsed_Weather = jObject.optString("main");

            JSONObject aa = new JSONObject(jsonString).getJSONObject("main");
            parsed_Temperature = aa.optString("temp");
            parsed_Humidity = aa.optString("humidity");

            weather_Result_Array[0] = parsed_Weather;
            weather_Result_Array[1] = parsed_Temperature;
            weather_Result_Array[2] = parsed_Humidity;

            System.out.println( "okokokok" + parsed_Weather + parsed_Temperature + parsed_Humidity);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weather_Result_Array;
    }
}
