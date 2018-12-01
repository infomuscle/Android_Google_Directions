package com.example.serversensor_iot;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

//connection : 연결된 소켓 정보 저장
//receive_In : 메세지 수신 받는 변수
//send_Out : 메세시 송신 하는 변수
//dbhelper : DB 이용하기 위한 변수
//senser_name : 연결된 액츄에이터의 이름을 저장하는 변수

public class AcceptDCMotorServerSocket extends Thread {
    //private ServerSocket dc_ServerSocket;
    private Socket connection = null;

    private BufferedReader receive_In = null;
    private PrintWriter send_Out = null;
    private DbHelp dbhelper;
    private String senser_name = "";
    private JsonIO jsonIO;

    //생성자 소켓 저장 연결된거 출력
    public AcceptDCMotorServerSocket(Socket accept) {
        connection = accept;
        System.out.println("결과 확인 DC모터 : 클라이언트 연결 성공");
        jsonIO = new JsonIO();
        start();
    }

    //start하면 돌아가는 실제 코드
    @Override
    public void run() {
        try {
            dbhelper = ServerBroadCastReceiver.getDbhelper();

            receive_In = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            send_Out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()), true);

            String getFindDustApi = "";
            FinedustApiTask finedustApiTask = new FinedustApiTask();

            if ((senser_name = receive_In.readLine()) != null) {
                while (true) {
                    System.out.println("결과 확인 DC모터 : 실행중");
                    try {
                        System.out.println("결과 확인 DC모터 : " + senser_name);
                        String _getJson[] = jsonIO.readerJsonSenser();

                        if (_getJson[3].equals("자동")) {
                            if (senser_name.equals("Window_Motor")) {
                                System.out.println("결과 확인 DC모터 HUMI : " + dbhelper.returnHumidity());
                                System.out.println("결과 확인 DC모터 TEMP : " + dbhelper.returnTemperature());
                                System.out.println("결과 확인 DC모터 TEMP_Hot_VAL : " + Double.parseDouble(_getJson[0]));
                                System.out.println("결과 확인 DC모터 TEMP_Cool_VAL : " + Double.parseDouble(_getJson[1]));
                                System.out.println("결과 확인 DC모터 : "  + finedustApiTask.getFinedust_Xml_Data());

                                if (Double.parseDouble(dbhelper.returnTemperature()) > Double.parseDouble(_getJson[0])) {
                                    send_Out.write("Open");
                                    send_Out.flush();
                                } else if (Double.parseDouble(dbhelper.returnTemperature()) < Double.parseDouble(_getJson[1])) {
                                    send_Out.write("Close");
                                    send_Out.flush();
                                }

                            } else if (senser_name.equals("HotCool_Motor")) {

                            } else if (senser_name.equals("Air_Cleaner_Motor")) {

                                if (Double.parseDouble(dbhelper.returnDust()) > 0) {
                                    send_Out.write("Start");
                                    send_Out.flush();
                                }
                            }
                        }
                        Thread.sleep(10000);
                    }
                    catch (Exception e) {
                        try {
                            System.out.println("결과 확인 DC모터 오류 : " + e.getMessage());
                            receive_In.close();
                            send_Out.close();
                            connection.close();
                            break;
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        System.out.println("결과 확인 DC모터 오류 : " + e.getMessage());
                    }
                }
            }
        }
        catch (IOException  e) {
            // TODO 자동 생성된 catch 블록
            System.out.println("결과 확인 DC모터 오류 : " + e.getMessage());
            e.printStackTrace();
            try {
                receive_In.close();
                send_Out.close();
                connection.close();

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        finally {
            try {
                send_Out.close();
                connection.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        System.out.println("결과 확인 DC모터 : 종료" );
        }
}

