package com.example.serversensor_iot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class ServerBroadCastReceiver extends BroadcastReceiver {

//    private ServerSocketAcceptIOT mService; // 서비스 객체
//    private boolean isService = false; // 서비스 중인 확인용

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_SCREEN_ON))
        {
            Toast.makeText(context, "SCREEN_ON", Toast.LENGTH_SHORT).show();
        }
        else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
        {
            Toast.makeText(context, "SCREEN_OFF", Toast.LENGTH_SHORT).show();
        }
        else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        {
            Toast.makeText(context, "BOOT_COMPLETED", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(context, ServerSocketAcceptIOT.class);
            ContextCompat.startForegroundService(context,i);
            context.startService(i);
        }
    }
}
