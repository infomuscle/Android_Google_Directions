package com.example.serversensor_iot;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;

//iot_ServerSocket : 온도, 습도 서버를 담당 클래스 변수
//fineDustServerSocketgGetSenser : 미세먼지 서버를 담당하는 클래스 변수
//acceptdcServerSocket : 모터(엑츄에어터)를 제어하기 위한 클래스 변수

public class ServerSocketAcceptIOT extends Service {
    private ServerSocket iot_ServerSocket;
    private ServerSocket fineDustServerSocketgGetSenser;
    private ServerSocket acceptdcServerSocket;
    private static DbHelp dbhelper;

//    // Binder given to clients
//    private final IBinder mBinder = new LocalBinder();
//    // Random number generator
//    private final Random mGenerator = new Random();

    public static DbHelp getDbhelper() {
        return dbhelper;
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
//    public class LocalBinder extends Binder {
//        ServerSocketAcceptIOT getService() {
//
//            return ServerSocketAcceptIOT.this;
//        }
//    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
/////////////////////////////////////////////////////////////////////////////////////yyj start
    private BroadcastReceiver mReceiver;

    @Override
        public void onCreate() {
            super.onCreate();
            System.out.println("결과 확인 스레드 : 시작");
            dbhelper = new DbHelp(getBaseContext(), "PROJECTIOT_DB.db", null, 1);

            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);

            System.out.println(getBaseContext().toString());

            filter.addAction(Intent.ACTION_SCREEN_OFF);
            mReceiver = new ServerBroadCastReceiver();
            registerReceiver(mReceiver, filter);
            Log.i("yyj", "receiver on/off 등록됨");

        try {
            iot_ServerSocket = new ServerSocket(9002);
            fineDustServerSocketgGetSenser = new ServerSocket(9009);
            acceptdcServerSocket = new ServerSocket(9005);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

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

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if( intent == null)
        {
            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            mReceiver = new ServerBroadCastReceiver();
            registerReceiver(mReceiver, filter);
            Log.i("yyj", "receiver on/off 등록됨");
        }
        Log.i("yyj", "service start 상태");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        Log.i("yyj", "receiver on/off 해제됨");
    }

//    @Override
//    public void onReceive(Context context, Intent intent) {
//        String action = intent.getAction();
//        if (action.equals("android.intent.action.BOOT_COMPLETED")) {
//            new Thread() {
//                @Override
//                public void run() {
//                    System.out.println("결과 확인 : 스레드 진입");
//                    try {
//                        iot_ServerSocket = new ServerSocket(9002);
//                    } catch (IOException e) {
//                        System.out.println("결과 확인 : 스레드 오류" + e.getMessage());
//                    }
//
//                    while(true) {
//                        try {
//                            new ServerSocketGetSenser(iot_ServerSocket.accept());
//                        } catch (Exception e) {
//                            System.out.println("결과 확인 : 스레드 오류" + e.getMessage());
//                        }
//                    }
//                }
//            }.start();
//        }
//
//    }


    //    @Override
//    public void run() {
//        // TODO 자동 생성된 메소드 스텁
//        try {
//            iot_ServerSocket = new ServerSocket(9002);
//            fineDustServerSocketgGetSenser = new ServerSocket(9003);
//            acceptdcServerSocket = new ServerSocket(9005);
////            acceptdcServerSocket = new AcceptDCMotorServerSocket();
////            acceptdcServerSocket.start();
//
//            new Thread() {
//                @Override
//                public void run() {
//                    while(true) {
//                        try {
//                            new FineDustServerSocketgGetSenser(fineDustServerSocketgGetSenser.accept());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }.start();
//
//            new Thread() {
//                @Override
//                public void run() {
//                    while(true) {
//                        try {
//                            new ServerSocketGetSenser(iot_ServerSocket.accept());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }.start();
//
//            new Thread() {
//                @Override
//                public void run() {
//                    while(true) {
//                        try {
//                            new AcceptDCMotorServerSocket(acceptdcServerSocket.accept());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }.start();
//
////            while (true) {
////                    new FineDustServerSocketgGetSenser(fineDustServerSocketgGetSenser.accept());
////                    new ServerSocketGetSenser(iot_ServerSocket.accept());
////                    new AcceptDCMotorServerSocket(acceptdcServerSocket.accept());
////            }
//
//        } catch (IOException e) {
//            // TODO 자동 생성된 catch 블록
//            e.printStackTrace();
//        }
//    }
}
