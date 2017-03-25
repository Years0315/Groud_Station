package com.years.ground_station.adapter;

/**
 * Created by Administrator on 2016/7/25 0025.
 */
public class EventUtil {
    private String msgs;
    private Object objects;
    public EventUtil(String msg, Object object){
        msgs=msg;
        objects=object;
    }

    public String getMsg(){
        return msgs;
    }
    public Object getParam(){
       // String type=objects.getClass().getSimpleName();
        /*if ("String".equals(type))
            return (String)objects;
        else  if ("Integer".equals(type))
            return (int)objects;
        else  if ("Byte".equals(type))
            return (byte)objects;
        else  if ("Float".equals(type))
            return (float)objects;
        else if ("Short".equals(type))
            return (short)objects;
        else if ("Byte[]".equals(type))
            return objects;*/
        return objects;
    }
}
