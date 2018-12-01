package com.example.serversensor_iot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

public class ServerBroadCastReceiver extends BroadcastReceiver {

//    private ServerSocketAcceptIOT mService; // 서비스 객체
//    private boolean isService = false; // 서비스 중인 확인용
    private static DbHelp dbhelper;
    public static DbHelp getDbhelper() {
        return dbhelper;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("결과 확인 BroadcastReceiver : onReceive");
        dbhelper = new DbHelp(context, "PROJECTIOT_DB.db", null, 1);

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
            Toast.makeText(context, "BOOT_COMPLETED 부팅 완료", Toast.LENGTH_SHORT).show();
            System.out.println("결과 확인 Boot_Completed : 시작");
            Intent i = new Intent(context, ServerSocketAcceptIOT.class);
            ContextCompat.startForegroundService(context,i);
            context.startService(i);
        }
    }
}
