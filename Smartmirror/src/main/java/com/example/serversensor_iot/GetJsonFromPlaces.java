package com.example.serversensor_iot;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetJsonFromPlaces extends AsyncTask<String, Void, String> {
    private String str, receive_Message;

    // Places API에 보낼 url 요소 중 고정 요소
    String api_Key = "AIzaSyBNLuJvvEaKPepCbLuXU4cX9wWyTGju4lM";
    String url_Body = "https://maps.googleapis.com/maps/api/geocode/json";
    String url_Address = "?address=";
    String url_Key = "&key=";
    String url_Lang = "&language=ko";

    // Places API에 보낼 url 요소 중 사용자 입력 요소
    String address_Input;                                   // 사용자에게 입력받는 String 원본
    String address;                                         // API 형식에 맞게 변환된 String(공백을 "+"로 변환)

    @Override
    protected String doInBackground(String... params) {
        URL url;
        try {
            // execute() 메소드 안에 인자를 대입해서 사용
            address_Input = params[0];
            address = address_Input.replace(" ", "+");      // 공백을 "+"로 변환

            // url 생성
            url = new URL(url_Body + url_Address + address + url_Lang + url_Key + api_Key);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            conn.setRequestProperty("x-waple-authorization", api_Key);

            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receive_Message = buffer.toString();
                Log.i("receive_Message : ", receive_Message);

                reader.close();
            } else {
                Log.i("통신 결과", conn.getResponseCode() + "에러");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return receive_Message;
    }
}