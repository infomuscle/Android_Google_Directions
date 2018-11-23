package com.multicampus.iot.smartmirror;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetJsonFromDirections extends AsyncTask<String, Void, String> {
    private String str, receiveMsg;
    String API_KEY = "AIzaSyBNLuJvvEaKPepCbLuXU4cX9wWyTGju4lM";
    String url_Body = "https://maps.googleapis.com/maps/api/directions/json";
    String url_Origin = "?origin=";
    String url_Destin = "&destination=";
    String url_Place_ID = "place_id:";
    String url_Key = "&key=";
    String url_Mode = "&mode=transit";
    String url_Lang = "&language=ko";
//    String origin_ID = "ChIJFXO4zdSifDURFGFcAKIgK1s";     // 성균관대
//    String destin_ID = "ChIJsaECEwqlfDURVEJQ6Agz7qc";     // 잠실역
//    String destin_ID = "ChIJpeKhoOiffDUR58XwBLzu4qE";       // 서울대
//    String origin_ID = "ChIJF0OnPOOifDUR58l2u5fFViM";       // 충무로역
    String origin_ID;
    String destin_ID;
    @Override
    protected String doInBackground(String... params) {
        URL url = null;
        try {
            origin_ID = params[0];
            destin_ID = params[1];
            url = new URL(url_Body + url_Origin + url_Place_ID + origin_ID + url_Destin + url_Place_ID + destin_ID + url_Mode + url_Lang + url_Key + API_KEY);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            conn.setRequestProperty("x-waple-authorization", API_KEY);

            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
                Log.i("receiveMsg : ", receiveMsg);

                reader.close();
            } else {
                Log.i("통신 결과", conn.getResponseCode() + "에러");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return receiveMsg;
    }
}