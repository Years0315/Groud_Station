package com.years.ground_station.widget.sp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/10/24 0024.
 */
public class SharedPreferencesSave {

    private static final String FILE_NAME = "share_date";

    public static void setParams(Context context,String name,Object object){

        String type=object.getClass().getSimpleName();
        SharedPreferences sp=context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        if ("String".equals(type))
            editor.putString(name, (String) object);
        else if ("Integer".equals(type))
            editor.putInt(name, (Integer) object);
        else if ("Boolean".equals(type))
            editor.putBoolean(name, (Boolean) object);
        else if ("Float".equals(type))
            editor.putFloat(name, (Float) object);
        else if ("Long".equals(type))
            editor.putLong(name, (Long) object);
        editor.commit();
    }

    public static Object getParams(Context context,String key,Object object){
        String type=object.getClass().getSimpleName();
        SharedPreferences sp=context.getSharedPreferences(FILE_NAME,Context.MODE_PRIVATE);
        if ("String".equals(type))
            return sp.getString(key, (String) object);
        else  if ("Integer".equals(type))
            return sp.getInt(key, (Integer) object);
        else  if ("Boolean".equals(type))
            return sp.getBoolean(key, (Boolean) object);
        else  if ("Float".equals(type))
            return sp.getFloat(key, (Float) object);
        else if ("Long".equals(type))
            return sp.getLong(key, (Long) object);
        return null;
    }
}
