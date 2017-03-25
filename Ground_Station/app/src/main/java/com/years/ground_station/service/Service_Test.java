package com.years.ground_station.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.years.ground_station.adapter.EventUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Years on 2017/3/16.
 */

public class Service_Test extends Service {
    private int i;
    private byte[] bytes=new byte[64];
    public static final String ACTION="com.years.ground_station.service.Service_Test";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("===========ServiceTest OnBind=============");
        return null;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventUtil eventUtil){
    }

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
        System.out.println("===========ServiceTest OnCreat=============");
        //timer.schedule(task,1000,1);
        super.onCreate();
    }
    Timer timer=new Timer();
    TimerTask task=new TimerTask() {
        @Override
        public void run() {
            i++;
            bytes[63]=2;
            EventBus.getDefault().post(new EventUtil("Test",bytes));
            //System.out.println("======Timer========"+i);
        }
    };

    @Override
    public void onStart(Intent intent, int startId) {
        System.out.println("===========ServiceTest OnStart=============");
        super.onStart(intent, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("===========ServiceTest OnUnBind============");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        System.out.println("===========ServiceTest OnStop=============");
        super.onDestroy();
    }
}
