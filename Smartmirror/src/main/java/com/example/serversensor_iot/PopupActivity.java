package com.example.serversensor_iot;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

//btn_Ok : 확인를 하면 값을 저장
//btn_Close : 취소를 하면 값을 저장하지 않음.

//editText_Temperature_Hot: 사용자가 입력안 온도를 가지고 위한 변수
//editText_Temperature_Cool : 사용자가 입력안 온도를 가지고 위한 변수

public class PopupActivity  extends Activity {
    private Button btn_Ok;
    private Button btn_Close;

    private EditText editText_Temperature_Hot;
    private EditText editText_Temperature_Cool;

    private RadioGroup radioBtn_Group;
    private RadioButton radioBtn;

    private JsonIO jsonIO;
    //기본값 설정
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);
        jsonIO = new JsonIO();
        jsonIO.newJsonSenser();

        String[] _getJsonVal = new String[4];

        btn_Ok = (Button) findViewById(R.id.btn_Ok);
        btn_Close = (Button) findViewById(R.id.btn_Close);
        editText_Temperature_Hot = (EditText)findViewById(R.id.editText_Temperature_Hot);
        editText_Temperature_Cool = (EditText)findViewById(R.id.editText_Temperature_Cool);
        radioBtn_Group = (RadioGroup)findViewById(R.id.radioBtn_Group);

        try{
            _getJsonVal = jsonIO.readerJsonSenser();

//        editText_Temperature_Hot.setText( Double.toString(((MainActivity) MainActivity.mContext).getTemperatureHotCoolVal()[0]));
//        editText_Temperature_Cool.setText( Double.toString(((MainActivity) MainActivity.mContext).getTemperatureHotCoolVal()[1]));

            editText_Temperature_Hot.setText(_getJsonVal[0]);
            editText_Temperature_Cool.setText(_getJsonVal[1]);
            if(Integer.parseInt(_getJsonVal[2]) > 0)
            {
                radioBtn = (RadioButton) findViewById(Integer.parseInt(_getJsonVal[2]));
                if(radioBtn != null) {
                    System.out.println("결과 확인 Radio : " + Integer.parseInt(_getJsonVal[2]));
                    System.out.println("결과 확인 Radio : " + radioBtn.getText());
                    radioBtn.setChecked(true);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        btn_Ok.setOnClickListener(new View.OnClickListener() {
            Toast _toast;
            @Override
            public void onClick(View v) {
                radioBtn = (RadioButton)findViewById(radioBtn_Group.getCheckedRadioButtonId());
                try {
                    if (editText_Temperature_Hot.getText().toString().equals("")) {
                        editText_Temperature_Hot.requestFocus();

                        if(_toast != null)
                            _toast.cancel();

                        _toast =  _toast.makeText(getApplicationContext(),"값을 입력하세요.", Toast.LENGTH_SHORT);
                        _toast.show();

                    }else if(editText_Temperature_Cool.getText().toString().equals("")){
                        editText_Temperature_Cool.requestFocus();

                        if(_toast != null)
                            _toast.cancel();

                        _toast =  _toast.makeText(getApplicationContext(),"값을 입력하세요.", Toast.LENGTH_SHORT);
                        _toast.show();
                    }
                    else {
                        if (radioBtn != null && (!editText_Temperature_Hot.getText().toString().equals("") && !editText_Temperature_Cool.getText().toString().equals(""))) {
                            jsonIO.writeJsonSenser(Double.parseDouble(editText_Temperature_Hot.getText().toString()), Double.parseDouble(editText_Temperature_Cool.getText().toString()), radioBtn_Group.getCheckedRadioButtonId(), radioBtn.getText().toString());
                            System.out.println("결과 확인 Radio : " + radioBtn_Group.getCheckedRadioButtonId());
                            System.out.println("결과 확인 Radio : " + radioBtn.getText());
                        } else if (radioBtn == null && (!editText_Temperature_Hot.getText().toString().equals("") && !editText_Temperature_Cool.getText().toString().equals(""))) {
                            jsonIO.writeJsonSenser(Double.parseDouble(editText_Temperature_Hot.getText().toString()), Double.parseDouble(editText_Temperature_Cool.getText().toString()), 0, "");
                        }

                        finish();
                    }
                }
                catch (Exception e)
                {
                    if(_toast != null)
                        _toast.cancel();

                    _toast = _toast.makeText(getApplicationContext(),"올바른 값을 입력하세요.", Toast.LENGTH_SHORT);
                    _toast.show();

                    Log.i("결과 확인 Popup 오류 : ", e.toString());
                }

            }
        });

        btn_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
