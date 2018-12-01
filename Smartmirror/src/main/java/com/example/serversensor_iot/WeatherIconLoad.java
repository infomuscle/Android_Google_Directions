package com.example.serversensor_iot;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherIconLoad extends AsyncTask<String, Void, Bitmap> {

    private String weather_Url;

    public WeatherIconLoad() {}

    public WeatherIconLoad(String weather_Url) {
        this.weather_Url = weather_Url;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bitmap = null;
        try {
            URL url = new URL(this.weather_Url);

            HttpURLConnection http_connction = (HttpURLConnection) url.openConnection();
            http_connction.setDoInput(true);
            http_connction.connect();

            InputStream input_Stream = http_connction.getInputStream();
            bitmap = BitmapFactory.decodeStream(input_Stream);
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.d("weather", "image Error");
        }
        return bitmap;
    }
}
