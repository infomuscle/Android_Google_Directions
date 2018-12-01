package com.example.serversensor_iot;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class AcceptDiplaySignalServerSocket extends Thread {
    private Socket clientSocket_Display_Signal;
    private BufferedReader in;
    private String received_Message;

    private Service forIntentCtx;

    public AcceptDiplaySignalServerSocket(Socket _clientSocket_Display_Signal, Context context) {
        this.clientSocket_Display_Signal = _clientSocket_Display_Signal;
        this.forIntentCtx = (Service)context;
        start();
    }

    @Override
    public void run() {
        try {
            System.out.println( "[ Client connection : " + clientSocket_Display_Signal.getInetAddress().getHostAddress() + " : "	+
                    clientSocket_Display_Signal.getPort() +  " ]" );
            System.out.println( "Waiting for message..." );

            in = new BufferedReader( new InputStreamReader( clientSocket_Display_Signal.getInputStream() ) );

            while ( ( received_Message = in.readLine() ) != null ) {
                System.out.println( "Receive message : " + received_Message );

                if(received_Message.equals("SHOWVIDEO")){
                    Intent i = new Intent(Intent.ACTION_MAIN);
                    i.addCategory(Intent.CATEGORY_LAUNCHER);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setComponent(new ComponentName(forIntentCtx, PopupVideoActivity.class));
                    forIntentCtx.startActivity(i);
                }
            }

            System.out.println( "[ disconnection client : " + clientSocket_Display_Signal.getInetAddress().getHostAddress() + " : "	+
                    clientSocket_Display_Signal.getPort() +  " ]" );
            in.close();
            clientSocket_Display_Signal.close();
        } catch ( IOException e ) {}
    }
}
