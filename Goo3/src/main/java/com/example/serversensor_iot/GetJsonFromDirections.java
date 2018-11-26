package com.example.serversensor_iot;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetJsonFromDirections extends AsyncTask<String, Void, String> {
    private String str, receive_Message;
    
    // Directions API에 보낼 url 요소 중 고정 요소
    String api_Key = "AIzaSyBNLuJvvEaKPepCbLuXU4cX9wWyTGju4lM";
    String url_Body = "https://maps.googleapis.com/maps/api/directions/json";
    String url_Origin = "?origin=";
    String url_Destination = "&destination=";
    String url_Place_Id = "place_id:";
    String url_Key = "&key=";
    String url_Mode = "&mode=transit";
    String url_Lang = "&language=ko";

    // Places API에 보낼 url 요소 중 사용자 입력 요소
    String origin_Id;                                       // 출발지의 place_id
    String destination_Id;                                  // 도착지의 place_id
    
    @Override
    protected String doInBackground(String... params) {
        URL url;
        try {
            // execute() 메소드 안에 순서대로 인자를 대입해서 사용
            origin_Id = params[0];
            destination_Id = params[1];

            // url 생성
            url = new URL(url_Body + url_Origin + url_Place_Id + origin_Id + url_Destination + url_Place_Id + destination_Id + url_Mode + url_Lang + url_Key + api_Key);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            connection.setRequestProperty("x-waple-authorization", api_Key);

            if (connection.getResponseCode() == connection.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(connection.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receive_Message = buffer.toString();
                Log.i("receive_Message : ", receive_Message);

                reader.close();
            } else {
                Log.i("통신 결과", connection.getResponseCode() + "에러");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return receive_Message;
    }
}