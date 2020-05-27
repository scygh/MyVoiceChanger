package com.scy.myvoicechanger.utils.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * description ：
 * author : scy
 * email : 1797484636@qq.com
 * date : 2020/4/8 08:56
 */
public class MyOpenHelper extends SQLiteOpenHelper {

    private static MyOpenHelper helper;

    public static MyOpenHelper getHelper(Context context) {
        if (helper == null) {
            helper = new MyOpenHelper(context, "voice_db", null, 1);
        }
        return helper;
    }

    public static final String CREATE_VOICE = "create table Voice (" +
            "id integer primary key autoincrement," +
            "name text," +
            "author text" +
            ")";

    private MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_VOICE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
