package com.example.serversensor_iot;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherTask extends AsyncTask<String, Void, String> {

    private String receive_String_Message, weather_Json_Data;

    @Override
    protected String doInBackground(String... params) {
        URL weather_Url = null;
        try {
            weather_Url = new URL("https://api.openweathermap.org/data/2.5/weather?q=Seoul,kr&APPID=73f62b5baaef604dc4de34ba0e092af1&units=metric");

            HttpURLConnection weather_Connection = (HttpURLConnection) weather_Url.openConnection();

            if (weather_Connection.getResponseCode() == weather_Connection.HTTP_OK) {
                InputStreamReader input_Stream_Reader = new InputStreamReader(weather_Connection.getInputStream(), "UTF-8");
                BufferedReader buffered_Reader = new BufferedReader(input_Stream_Reader);
                StringBuffer string_Buffer = new StringBuffer();
                while ((receive_String_Message = buffered_Reader.readLine()) != null) {
                    string_Buffer.append(receive_String_Message);
                }
                weather_Json_Data = string_Buffer.toString();
                Log.i("receiveMsg : ", weather_Json_Data);

                buffered_Reader.close();
            } else {
                Log.i("통신 결과", weather_Connection.getResponseCode() + "에러");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return weather_Json_Data;
    }
}
