package com.example.serversensor_iot;

import android.app.Service;
import android.util.Log;

import java.io.IOException;
import java.net.ServerSocket;

class ServerSocketAcceptThread extends Thread{
        private ServerSocket diplay_Signal_ServerSocket;
        private ServerSocket open_Signal_ServerSocket;

        private AcceptOpenSignalServerSocket sendThread;
        private AcceptDiplaySignalServerSocket receiveThread;

        private Service context;

        public ServerSocketAcceptThread(Service context){
            this.context = context;
        }

        @Override
        public void run() {
            try {
                diplay_Signal_ServerSocket = new ServerSocket(5000);
                open_Signal_ServerSocket = new ServerSocket(5001);

                while (true) {
                    System.out.println("Pending connection...");
                    Log.d("yyj", "ServerSocketService accept");
                    receiveThread = new AcceptDiplaySignalServerSocket(diplay_Signal_ServerSocket.accept(), context);
                    sendThread = new AcceptOpenSignalServerSocket(open_Signal_ServerSocket.accept());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void callSendThreadOpen(){
            sendThread.sendOpenDoorMsg();
        }


        public void closeServerSocket(){
            try {
                diplay_Signal_ServerSocket.close();
                open_Signal_ServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }