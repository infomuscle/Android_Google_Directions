package com.example.serversensor_iot;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

//context : Context 넘어온 값 저장
// DB : DB 이름
// TABLE : TABLE 이름
// DB_VERSION : 미리 1로 초기화

public class DbHelp extends SQLiteOpenHelper {
    private Context context;

    private static final String DB = "PROJECTIOT_DB.db";
    private static final String TABLE = "PROJECTIOT_DB";
    private static final int DB_VERSION = 1;

    public DbHelp(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    public DbHelp(Context context) {
        super(context, DB, null, DB_VERSION);
    }

    //DB 만들기
    @Override
    public void onCreate(SQLiteDatabase db) {

        StringBuffer sb = new StringBuffer();
        sb.append(" CREATE TABLE PROJECTIOT_DB ( ");
        sb.append("_ID INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sb.append(" TYPE TEXT, ");
        sb.append(" VALUE TEXT, ");
        sb.append(" TIME DATE)");
        // SQLite Database로 쿼리 실행
        db.execSQL(sb.toString());
    }

    //모든 값 넘기는 매서드
    public ArrayList selectDB() {
        SQLiteDatabase _db = getReadableDatabase();
        String temp;
        ArrayList<String> returnval = new ArrayList<>();
        int i = 0;
        Cursor cursor = _db.rawQuery("SELECT * FROM PROJECTIOT_DB", null);
        while (cursor.moveToNext()) {
            temp = cursor.getString(0) + "  ";
            temp += "타입 : " + cursor.getString(1);
            temp += "  값 : " + cursor.getString(2);
            temp += "  날짜 : " + cursor.getString(3);

            returnval.add(temp);

            i++;
        }
//        db.close();

        return returnval;

    }

    //DB에 값넣기
    public void insert(String _type_data, String _date_data, String _value_data) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase _db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        try {
            _db.execSQL("INSERT INTO PROJECTIOT_DB( TYPE, TIME, VALUE ) VALUES ('" + _type_data + "', '" + _date_data + "', " + _value_data + ")");

//            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("결과 확인 DB오류 insert : " + e.toString());

//            db.close();
        }
    }

    //DB에서 제일 최근 온도 반환 메서드
    public String returnTemperature() {
        try {
            String _temp;
            SQLiteDatabase _db = getReadableDatabase();
            Cursor _cursor = _db.rawQuery("select * from PROJECTIOT_DB where TYPE = 'TEMP' order by _ID desc limit 1", null);

            _cursor.moveToNext();
            _temp = _cursor.getString(2);
//            db.close();
            return _temp;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("결과 확인 DB오류 returnTEMP : " + e.toString());
            return "DB오류";
        }
    }

    //DB에서 제일 최근 습도 반환 메서드
    public String returnHumidity() {
        try {
            String temp;
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from PROJECTIOT_DB where TYPE = 'HUMI' order by _ID desc limit 1", null);

            cursor.moveToNext();
            temp = cursor.getString(2);
//            db.close();
            return temp;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("결과 확인 DB오류 returnHUMI : " + e.toString());
            return "DB오류";
        }
    }

    public String returnDust() {
        try {
            String temp;
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from PROJECTIOT_DB where TYPE = 'DUST' order by _ID desc limit 1", null);

            cursor.moveToNext();
            temp = cursor.getString(2);
//            db.close();
            return temp;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("결과 확인 DB오류 DUST : " + e.toString());
            return "DB오류";
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
