package com.brena.ulsacommunity.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by DanielBrena on 03/10/15.
 */
public class MyPreference {
    public static final String PREFS_NAME = "ACCOUNT";
    public static void add(Context context,String name, String value){
        SharedPreferences setting = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();

        editor.putString(name,value);
        editor.commit();

    }

    public static String get(Context context,String name){
        SharedPreferences setting = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        String value = setting.getString(name, null);
        return value;
    }

    public static  void delete(Context context,String name){
        SharedPreferences setting = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.remove(name);
        editor.commit();
    }

    public static boolean exist(Context context, String name){
        SharedPreferences setting = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        if(setting.contains(name)){
            return true;
        }else{
            return false;
        }
    }
    public static void deleteAll(Context context){
        SharedPreferences setting = context.getSharedPreferences(PREFS_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = setting.edit();
        editor.clear();
        editor.commit();

    }
}
