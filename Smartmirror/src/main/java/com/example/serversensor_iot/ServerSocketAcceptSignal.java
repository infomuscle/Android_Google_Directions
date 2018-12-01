package com.example.serversensor_iot;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ServerSocketAcceptSignal extends Service {
    private static ServerSocketAcceptThread serverSocketAcceptThread;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("yyj", "ServerSocketService start");
        serverSocketAcceptThread = new ServerSocketAcceptThread(this);
        serverSocketAcceptThread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d("yyj", "ServerSocketService stop");
        serverSocketAcceptThread.closeServerSocket();
        super.onDestroy();
    }

    public static void callSendMsgForService(){
        serverSocketAcceptThread.callSendThreadOpen();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}