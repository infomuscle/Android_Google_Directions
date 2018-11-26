package com.example.serversensor_iot;

import java.io.IOException;
import java.net.ServerSocket;

//iot_ServerSocket : 온도, 습도 서버를 담당 클래스 변수
//fineDustServerSocketgGetSenser : 미세먼지 서버를 담당하는 클래스 변수
//acceptdcServerSocket : 모터(엑츄에어터)를 제어하기 위한 클래스 변수

public class ServerSocketAcceptIOT extends Thread {
    private ServerSocket iot_ServerSocket;
    private ServerSocket fineDustServerSocketgGetSenser;
    private ServerSocket acceptdcServerSocket;

    @Override
    public void run() {
        // TODO 자동 생성된 메소드 스텁
        try {
            iot_ServerSocket = new ServerSocket(9002);
            fineDustServerSocketgGetSenser = new ServerSocket(9003);
            acceptdcServerSocket = new ServerSocket(9005);
//            acceptdcServerSocket = new AcceptDCMotorServerSocket();
//            acceptdcServerSocket.start();

            new Thread() {
                @Override
                public void run() {
                    while(true) {
                        try {
                            new FineDustServerSocketgGetSenser(fineDustServerSocketgGetSenser.accept());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

            new Thread() {
                @Override
                public void run() {
                    while(true) {
                        try {
                            new ServerSocketGetSenser(iot_ServerSocket.accept());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

            new Thread() {
                @Override
                public void run() {
                    while(true) {
                        try {
                            new AcceptDCMotorServerSocket(acceptdcServerSocket.accept());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();

//            while (true) {
//                    new FineDustServerSocketgGetSenser(fineDustServerSocketgGetSenser.accept());
//                    new ServerSocketGetSenser(iot_ServerSocket.accept());
//                    new AcceptDCMotorServerSocket(acceptdcServerSocket.accept());
//            }

        } catch (IOException e) {
            // TODO 자동 생성된 catch 블록
            e.printStackTrace();
        }
    }
}
