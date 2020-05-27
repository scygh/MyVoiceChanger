package com.scy.myvoicechanger.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * description ：
 * author : scy
 * email : 1797484636@qq.com
 * date : 2020/4/8 08:52
 */
public class SpUtils {

    public static void Collect(Context context,String name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("isCollect",Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(name,true).commit();
    }

    public static void notCollect(Context context,String name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("isCollect",Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(name,false).commit();
    }

    public static boolean isCollect(Context context,String name) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("isCollect",Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(name,false);
    }


}
