package com.multicampus.iot.smartmirror;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetJsonFromPlaces extends AsyncTask<String, Void, String> {
    private String str, receiveMsg;
    String API_KEY = "AIzaSyBNLuJvvEaKPepCbLuXU4cX9wWyTGju4lM";
    String url_Body = "https://maps.googleapis.com/maps/api/geocode/json";
    String url_Address = "?address=";
    String url_Key = "&key=";
    String url_Lang = "&language=ko";
    String address_input;
    String address;

    @Override
    protected String doInBackground(String... params) {
        URL url = null;
        try {
            address_input = params[0];
            address = address_input.replace(" ", "+");

            url = new URL(url_Body + url_Address + address + url_Lang + url_Key + API_KEY);
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