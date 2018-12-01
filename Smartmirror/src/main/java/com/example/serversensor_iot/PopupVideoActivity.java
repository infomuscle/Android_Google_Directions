package com.example.serversensor_iot;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class PopupVideoActivity  extends Activity {
    private WebView webView_VIDEO;
    private Button button_DOOROPEN;
    private Button button_REPORT;
    public static Context mContext;

    //기본값 설정
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_videopopup);

        mContext = this;

        button_DOOROPEN = (Button) findViewById(R.id.button_DOOROPEN);
        button_REPORT = (Button) findViewById(R.id.button_REPORT);

        webView_VIDEO = (WebView) findViewById(R.id.webView);
        webView_VIDEO.setWebViewClient(new WebViewClient());
        webView_VIDEO.setBackgroundColor(255);
        webView_VIDEO.getSettings().setLoadWithOverviewMode(true);
        webView_VIDEO.getSettings().setUseWideViewPort(true);

        WebSettings webSettings = webView_VIDEO.getSettings();
        webSettings.setJavaScriptEnabled(true);

//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction("CAMERA.TEST");

        webView_VIDEO.loadData("<html><head><style type='text/css'>body{margin:auto auto;text-align:center;} img{width:100%25;} div{overflow: hidden;} </style></head><body><div><img src='http://192.168.0.22:8080/stream/video.mjpeg'/></div></body></html>", "text/html", "UTF-8");

        button_DOOROPEN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        ServerSocketAcceptSignal.callSendMsgForService();
                    }
                }.start();
            }

        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
