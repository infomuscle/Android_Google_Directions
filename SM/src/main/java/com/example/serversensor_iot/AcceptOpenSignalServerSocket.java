package com.example.serversensor_iot;

import android.view.View;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class AcceptOpenSignalServerSocket extends Thread {
    private Socket clientSocket_Open_Signal;
    private PrintWriter out;

    public AcceptOpenSignalServerSocket(Socket _clientSocket_Open_Signal) {
        this.clientSocket_Open_Signal = _clientSocket_Open_Signal;
        start();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub


        try {
            System.out.println( "[ Client connection : " + clientSocket_Open_Signal.getInetAddress().getHostAddress() + " : "	+
                    clientSocket_Open_Signal.getPort() +  " ]" );
            System.out.println( "Waiting for message..." );

            out = new PrintWriter( new OutputStreamWriter( clientSocket_Open_Signal.getOutputStream() ), true );
            while ( true ) {
                ((MainActivity)MainActivity.mContext).getButtonDOOROPEN().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Thread() {
                            @Override
                            public void run() {
                                out.write("MTROP");
                                out.flush();
                            }
                        }.start();
                    }
                });


            }


//            System.out.println( "[ disconnection client : " + clientSocket_Open_Signal.getInetAddress().getHostAddress() + " : "	+
//                    clientSocket_Open_Signal.getPort() +  " ]" );
//            out.close();
//            clientSocket_Open_Signal.close();
        } catch ( IOException e ) {}
    }
}
