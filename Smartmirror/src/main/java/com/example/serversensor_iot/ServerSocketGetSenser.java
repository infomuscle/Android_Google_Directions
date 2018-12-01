package com.example.serversensor_iot;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.Socket;

//connection : 연결된 소켓 정보 저장
//count : 연결된 갯수
//receive_In : 메세지 수신 받는 변수
//dbhelper : DB 이용하기 위한 변수
//textView_HUMID : TextView_HUMID를 이용하기 위한 변수
//textView_TEMPE : TextView_TEMPE 이용하기 위한 변수
//TEMP_HUMID_Receive_Message : 전송 받은 데이터를 임시 저장
//TEMP_HUMID_Receive_Message_Split : TEMP_HUMID_Receive_Message문자열을 split(";")를 하여 배열로 저장
//type_Data : 전송 받은 타입을 저장
//value_Data : 전송 받은 값을 저장
//date_Data : 전송 받은 Date를 저장


public class ServerSocketGetSenser extends Thread {

    //Socket 변수
    private Socket connection = null;

    private DbHelp dbhelper;

    private BufferedReader receive_In = null;
    private String temperature_Humidity_Receive_Message = null;
    private String[] temperature_Humidity_Receive_Message_Split;
    private String type_Data = null, value_Data = null, date_Data = null;

    //생성자
    public ServerSocketGetSenser(Socket accept) {
        connection = accept;
        System.out.println("결과 확인 GetSener : 연결 완료" );
        start();
    }

    //start하면 실행 되는 메서드
    @Override
    public void run() {
          dbhelper = ServerBroadCastReceiver.getDbhelper();
//
//          dbhelper = ((MainActivity) MainActivity.mContext).getDbhelper();
//        textView_Humidity = ((MainActivity) MainActivity.mContext).getTextView_Humidity();
//        textView_Temperature = ((MainActivity) MainActivity.mContext).getTextView_Temperature();

        try {
            receive_In = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while (connection.isConnected()) {
                System.out.println("결과 확인 GetSenser : 실행중 " );
                try{
                    if((temperature_Humidity_Receive_Message = receive_In.readLine()) != null) {
                        System.out.println("결과 확인 GetSenser Msg 확인 : " + temperature_Humidity_Receive_Message);
                        temperature_Humidity_Receive_Message_Split = temperature_Humidity_Receive_Message.split(";");
                        System.out.println("결과 확인 GetSenser Msg_Split 확인 : " + temperature_Humidity_Receive_Message_Split[0] + " " +temperature_Humidity_Receive_Message_Split[1] + " " +temperature_Humidity_Receive_Message_Split[2]);

                        type_Data = temperature_Humidity_Receive_Message_Split[0];
                        value_Data = temperature_Humidity_Receive_Message_Split[1];
                        date_Data = temperature_Humidity_Receive_Message_Split[2];

                        if(type_Data.equals("END") && value_Data.equals("END"))
                        {
                            System.out.println("결과 확인 GetSenser 1 : " + connection.isClosed());
                            System.out.println("결과 확인 GetSenser 확인 : 종료 들어옴");
                            receive_In.close();
                            connection.close();
                            System.out.println("결과 확인 GetSenser 2 : " + connection.isClosed());
                        }
                        else {
                            dbinsert(type_Data, value_Data, date_Data);
                        }

                    }
                    else if((temperature_Humidity_Receive_Message = receive_In.readLine()) == null)
                    {
                        receive_In.close();
                        connection.close();
                    }
                }catch (IOException e)
                {   System.out.println("결과 확인 GetSenser 오류 확인 1 : " + e.toString());
                    Log.e("결과 확인",  "  GetSenser 오류 while in 1 :  " + e.toString());
                    System.out.println("결과 확인 GetSenser 11: " + connection.isClosed());
                    receive_In.close();
                    connection.close();
                    System.out.println("결과 확인 GetSenser 22: " + connection.isClosed());
                    break;
                }
                catch (Exception e)
                {
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String exceptionAsStrting = sw.toString();

                    System.out.println("결과 확인 GetSenser 오류 확인 : " + exceptionAsStrting);
                    Log.e("결과 확인",  " GetSenser 오류 while in  :  " + exceptionAsStrting);
                }

            }
        } catch (Exception e) {
            // TODO: handle exception
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsStrting = sw.toString();
            System.out.println("결과 확인 GetSenser 오류 확인 : " + exceptionAsStrting);
            Log.e("결과 확인", " GetSenser 오류 : "   + exceptionAsStrting);
        }
        finally {
            try {
                System.out.println("결과 확인 GetSenser 111: " + connection.isClosed());
                if(connection.isConnected()) {
                    receive_In.close();
                    connection.close();
                }
                System.out.println("결과 확인 GetSenser Msg 확인 : 종료");
                System.out.println("결과 확인 GetSenser 222: " + connection.isClosed());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println("결과 확인 GetSenser Msg 확인 : 종료");
    }

    //DB에 데이터를 저장하는 메서드
    private void dbinsert(String type_data, String value_data, String date_data) {
        try {
            if (type_data != null && date_data != null && value_data != null) {
                //DB연결
                if (type_data.equals("TEMP")) {
                    System.out.println("결과 확인 GetSenser TEMP : " + value_data);
                    //textView_Temperature.setText("온도 : " + value_data);

                } else if (type_data.equals("HUMI")) {
                    System.out.println("결과 확인 GetSenser HUMI : " + value_data);
                    //textView_Humidity.setText("습도 : " + value_data);
                }

                dbhelper.insert(type_data, date_data, value_data);

            }
        } catch (Exception e) {
            // TODO: handle exception
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsStrting = sw.toString();

            Log.e("결과 확인", " GetSenser 오류 dbinsert : " +  exceptionAsStrting);
        } finally {

        }
    }
}
