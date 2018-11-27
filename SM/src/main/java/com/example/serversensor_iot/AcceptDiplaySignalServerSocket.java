package com.example.serversensor_iot;

import android.os.Handler;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class AcceptDiplaySignalServerSocket extends Thread {
    private Socket clientSocket_Display_Signal;
    private BufferedReader in;
    private String received_Message;

    public AcceptDiplaySignalServerSocket(Socket _clientSocket_Display_Signal) {
        this.clientSocket_Display_Signal = _clientSocket_Display_Signal;
        start();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub


        try {
            System.out.println( "[ Client connection : " + clientSocket_Display_Signal.getInetAddress().getHostAddress() + " : "	+
                    clientSocket_Display_Signal.getPort() +  " ]" );
            System.out.println( "Waiting for message..." );

            in = new BufferedReader( new InputStreamReader( clientSocket_Display_Signal.getInputStream() ) );
            int _delay_Count = 0;

            while ( ( received_Message = in.readLine() ) != null ) {
                System.out.println( "Receive message : " + received_Message );
                if (received_Message.equals("RECOG")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ((MainActivity)MainActivity.mContext).runOnUiThread(new Runnable(){
                                @Override
                                public void run() {
                                    if (((MainActivity)MainActivity.mContext).getWebViewVIDEO().getVisibility() == View.INVISIBLE)
                                    {
                                        ((MainActivity)MainActivity.mContext).getWebViewVIDEO().setVisibility(View.VISIBLE);
                                        ((MainActivity)MainActivity.mContext).getButtonDOOROPEN().setVisibility(View.VISIBLE);
                                        ((MainActivity)MainActivity.mContext).getButtonREPORT().setVisibility(View.VISIBLE);
                                        final Handler _handler = new Handler();
                                        _handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                ((MainActivity)MainActivity.mContext).getWebViewVIDEO().setVisibility(View.INVISIBLE);
                                                ((MainActivity)MainActivity.mContext).getButtonDOOROPEN().setVisibility(View.INVISIBLE);
                                                ((MainActivity)MainActivity.mContext).getButtonREPORT().setVisibility(View.INVISIBLE);
                                            }
                                        }, 10000 );
                                    }
                                    else if (((MainActivity)MainActivity.mContext).getWebViewVIDEO().getVisibility() == View.VISIBLE)
                                    {

                                    }
                                }
                            });
                        }
                    }).start();
                }
                else if (received_Message.equals("JOGCN")) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ((MainActivity)MainActivity.mContext).runOnUiThread(new Runnable(){
                                @Override
                                public void run() {
                                    ((MainActivity)MainActivity.mContext).getWebViewVIDEO().setVisibility(View.VISIBLE);
                                    ((MainActivity)MainActivity.mContext).getButtonDOOROPEN().setVisibility(View.VISIBLE);
                                    ((MainActivity)MainActivity.mContext).getButtonREPORT().setVisibility(View.VISIBLE);
                                    final Handler _handler = new Handler();
                                    _handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            ((MainActivity)MainActivity.mContext).getWebViewVIDEO().setVisibility(View.INVISIBLE);
                                            ((MainActivity)MainActivity.mContext).getButtonDOOROPEN().setVisibility(View.INVISIBLE);
                                            ((MainActivity)MainActivity.mContext).getButtonREPORT().setVisibility(View.INVISIBLE);
                                        }
                                    }, 10000 );
                                }
                            });
                        }
                    }).start();
                }
            }

            System.out.println( "[ disconnection client : " + clientSocket_Display_Signal.getInetAddress().getHostAddress() + " : "	+
                    clientSocket_Display_Signal.getPort() +  " ]" );
            in.close();
            clientSocket_Display_Signal.close();
        } catch ( IOException e ) {}
    }
}
