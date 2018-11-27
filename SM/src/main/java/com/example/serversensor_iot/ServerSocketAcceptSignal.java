package com.example.serversensor_iot;

import android.database.Cursor;
import android.net.Uri;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerSocketAcceptSignal extends Thread{
    private ServerSocket diplay_Signal_ServerSocket;
    private ServerSocket open_Signal_ServerSocket;
    @Override
    public void run() {
        try {

            diplay_Signal_ServerSocket = new ServerSocket( 5000 );
            open_Signal_ServerSocket = new ServerSocket( 5001 );

            while ( true ) {
                System.out.println( "Pending connection..." );
                new AcceptDiplaySignalServerSocket( diplay_Signal_ServerSocket.accept());
                new AcceptOpenSignalServerSocket( open_Signal_ServerSocket.accept());
            }
        } catch ( IOException e ) {}
    }
}