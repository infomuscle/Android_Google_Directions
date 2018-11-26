package com.example.serversensor_iot;

import android.app.Application;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

//jsonObject JSON문자열을 받거나 저장하기 위하여 사용하는 변수
//dirPath : 파일 경로를 알기 위한 변수

public class JsonIO {
    private JSONObject jsonObject;
    private  String dirPath = (((MainActivity) MainActivity.mContext).getFilesDir().getAbsolutePath());

    //SenserOption.json파일이 없으면 새로 생성 하는 메서드
    public void newJsonSenser(){
        jsonObject = new JSONObject();
        try {
            File files = new File(dirPath + "SenserOption.json");

            if(files.exists()==false) {
                jsonObject.put("temp_hot", "27.00");
                jsonObject.put("temp_cool", "60.00");
                jsonObject.put("Radio_Check", "0");
                jsonObject.put("Radio_Check_Name", "");

                Log.i("결과확인 Json newJsonSenser : ", dirPath);

                FileWriter file = new FileWriter(dirPath + "SenserOption.json");
                file.write(jsonObject.toString());
                Log.i("결과확인 Json newJsonSenser : ", jsonObject.toString());
                file.flush();
                file.close();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e){
                e.printStackTrace();
            }
        }

        //SenserOption.json을 읽어와 temp_hot,temp_cool값을 리턴해주는 메서드
        public String[] readerJsonSenser(){
            String[] _optionValue = new String[4];
            String _temp = "";

            File file = new File(dirPath + "SenserOption.json") ;
            FileReader fr = null ;
            BufferedReader bufrd = null ;

            try {
                fr =  new FileReader(file) ;
                bufrd = new BufferedReader(fr) ;

                // read 1 char from file.
                if((_temp = bufrd.readLine()) != null) {
                    Log.i("결과확인 1 : " , _temp);
                }

                jsonObject = new JSONObject(_temp);
                _optionValue[0] = String.format("%.2f",Double.parseDouble(jsonObject.getString("temp_hot")));
                _optionValue[1] = String.format("%.2f",Double.parseDouble(jsonObject.getString("temp_cool")));
                _optionValue[2] = jsonObject.getString("Radio_Check");
                _optionValue[3] = jsonObject.getString("Radio_Check_Name");
                Log.i("결과확인 Json readerJsonSenser temp : " , _optionValue[0]);
                Log.i("결과확인 Json readerJsonSenser humi : " , _optionValue[1]);
                Log.i("결과확인 Json readerJsonSenser Radio_Check : " , _optionValue[2]);
                Log.i("결과확인 Json readerJsonSenser Radio_Check_Name : " , _optionValue[3]);

                bufrd.close();
                fr.close();
        }
        catch (Exception e){
            Log.i("결과확인 오류 : " , e.getMessage());
            e.printStackTrace();
        }
        return _optionValue;
    }

    // temp_hot,temp_cool값을 변경해주는 메서드
    public void writeJsonSenser(double val_Temp_hot, double val_Temp_cool, int radio_Check, String radio_Check_Name){
        jsonObject = new JSONObject();
        try {
            jsonObject.put("temp_hot", val_Temp_hot);
            jsonObject.put("temp_cool", val_Temp_cool);
            jsonObject.put("Radio_Check", radio_Check);
            jsonObject.put("Radio_Check_Name", radio_Check_Name);

            Log.i("결과확인 Json writeJsonSenser : " , dirPath);

            FileWriter file = new FileWriter( dirPath + "SenserOption.json");
            file.write(jsonObject.toString());
            Log.i("결과확인 Json writeJsonSenser : " , jsonObject.toString());
            file.flush();
            file.close();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
