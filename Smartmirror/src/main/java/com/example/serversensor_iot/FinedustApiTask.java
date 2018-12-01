package com.example.serversensor_iot;

import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FinedustApiTask extends AsyncTask<String, Void, String> {

    private String finedust_Xml_Data;

    @Override
    protected String doInBackground(String... strings) {
        String service_Key = "FyePdzsoSZF5QoHItsSJ%2BN0Fdohs3aIcw3CK6VhAUgEMGcpUEdIvH%2B1kt6LXIdq2d1Xaly3umuKKAGSn1%2FB1eg%3D%3D";
        String finedust_Url = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/"
                + "getCtprvnRltmMesureDnsty"
                + "?sidoName=서울"
                + "&pageNo=1"
                + "&numOfRows=30"
                + "&ServiceKey=" + service_Key
                + "&ver=1.3";
        URL url;
        String send_Finedust_data = "";
        try {
            url = new URL(finedust_Url);

            HttpURLConnection finedust_Connection = (HttpURLConnection) url.openConnection();

            if (finedust_Connection.getResponseCode() == finedust_Connection.HTTP_OK) {
                InputStreamReader input_Stream_Reader = new InputStreamReader(finedust_Connection.getInputStream(), "UTF-8");

                XmlPullParserFactory xml_Factory = XmlPullParserFactory.newInstance();
                XmlPullParser xml_Parser = xml_Factory.newPullParser();
                xml_Parser.setInput(input_Stream_Reader);

                String tag;

                xml_Parser.next();
                int eventType = xml_Parser.getEventType();
                boolean sido_Name_Check = false;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            tag = xml_Parser.getName();//태그 이름 얻어오기
                            if (tag.equals("stationName") && sido_Name_Check == false) {
                                xml_Parser.next();
                                if (xml_Parser.getText().equals("강남구")) {
                                    sido_Name_Check = true;
                                }
                                else if (sido_Name_Check == false)
                                    break;
                            }
                            else if (tag.equals("pm10Value") && sido_Name_Check == true) {
                                xml_Parser.next();
                                finedust_Xml_Data = xml_Parser.getText();
                                sido_Name_Check = false;
                                break;
                            }

                        case XmlPullParser.TEXT:
                            break;

                        case XmlPullParser.END_TAG:
                            break;
                    }

                    eventType = xml_Parser.next();
                }
            } else {
                Log.i("통신 결과", finedust_Connection.getResponseCode() + "에러");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        if (Integer.parseInt(finedust_Xml_Data) > 101) {
            send_Finedust_data = "매우 나쁨 (" + finedust_Xml_Data + " ㎍/㎥)";
        }
        else if (Integer.parseInt(finedust_Xml_Data) > 51) {
            send_Finedust_data = "나쁨 (" + finedust_Xml_Data + " ㎍/㎥)";
        }
        else if (Integer.parseInt(finedust_Xml_Data) > 16) {
            send_Finedust_data = "보통 (" + finedust_Xml_Data + " ㎍/㎥)";
        }
        else {
            send_Finedust_data = "좋음 (" + finedust_Xml_Data + " ㎍/㎥)";
        }

        return send_Finedust_data;

    }

    public String getFinedust_Xml_Data() {
        return finedust_Xml_Data;
    }
}
