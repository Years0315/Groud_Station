package com.years.ground_station.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.years.ground_station.adapter.EventUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;


/**
 * Created by Administrator on 2016/9/27 0027.
 */
public class Service_usb extends Service {
    private int ProductID=0;
    private int Permission=0;
    private  boolean STATE=true;
    private final int VendorID=1155;
    private final int ProductID_Remote=40976;       //数传PID
    private final int ProductID_Airplane=40964;     //飞控PID
    /**===========USB HID===============**/
    private UsbManager usbManager;                      //usb管理类
    private UsbDevice usbDevice;                        //usb设备
    private UsbInterface usbInterface;                  //usb接口
    private UsbEndpoint usbEndpoint_IN,usbEndpoint_OUT; //节点
    private UsbDeviceConnection usbDeviceConnection;    //连接
    private PendingIntent intent;                       //意图
    private static final String ACTION_USB_PERMISSION =
            "com..years.USB_PERMISSION";//定义常量
    IntentFilter intentFilter;
    private  byte[] buffer= new byte[64];             //接收缓冲
    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);

        System.out.println("=======Service_usb OnCreate=========="+STATE);
        intent=PendingIntent.getBroadcast(this,0,new Intent(ACTION_USB_PERMISSION),0);
        intentFilter=new IntentFilter(ACTION_USB_PERMISSION);


    }
    private void addData(byte[] bytes){
        for (int i=0;i<bytes.length;i++){
            data_to_send[i]=bytes[i];
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventUtil eventUtil){
        if (eventUtil.getMsg().equals("Send_Buffer")){
            byte[] bytes= (byte[]) eventUtil.getParam();
            addData(bytes);
            usb_sendData(bytes.length+1);
        }
    }

    public void scanUSBDevice(){
        usbManager= (UsbManager) getSystemService(Context.USB_SERVICE);
        if (usbManager==null){
            Toast.makeText(this, "手机不支持OTG", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String,UsbDevice> deviceList=usbManager.getDeviceList();
        if (!(deviceList.isEmpty())) //获取的device列表
        {
            for (UsbDevice device: deviceList.values())
            {
                usbDevice=device;                       //获取到设备  一般只有一个
                System.out.println("========DeviceName======"+usbDevice.getDeviceName());
                if(usbDevice.getProductId()==ProductID_Remote&&usbDevice.getVendorId()==VendorID)           //判断usb设备PID VID 此处用来和匿名数传和飞控对比
                    ProductID=1;                   //与数传pid vid吻合  判断usb设备为数传
                else if(usbDevice.getProductId()==ProductID_Airplane&&usbDevice.getVendorId()==VendorID)
                    ProductID=2;                   //与飞控pid vid吻合   判断usb设备为飞控
                else ProductID=3;                  //均不匹配  判断usb设备为第三方设备

                if (usbManager.hasPermission(usbDevice))//请求权限
                {
                    System.out.println("======拥有权限=======");
                    Permission=1;
                    int i=usbDevice.getInterfaceCount();//获取接口数量
                    System.out.println("======="+i);
                    //特殊判断   匿名新版数传增加了虚拟串口  多了两个接口  一般都是一个
                    if (i<2){
                        usbInterface=usbDevice.getInterface(0);
                        System.out.println("=======UsbInterfce0======="+usbInterface.getName());
                    }
                    else{
                        usbInterface=usbDevice.getInterface(2);
                        System.out.println("=======UsbInterfce2======="+usbInterface.getName());
                    }
                    //定义两个节点 1发送 0 接收
                    usbEndpoint_IN=usbInterface.getEndpoint(0);
                    usbEndpoint_OUT=usbInterface.getEndpoint(1);
                    //连接的类 得到实例
                    usbDeviceConnection=usbManager.openDevice(usbDevice);
                    usbDeviceConnection.claimInterface(usbInterface,true);
                    usb_receiveData();
                    System.out.println("=======进入接收======"+STATE);
                }
                else{
                    Permission=2;  //第一次插入  会请求临时权限
                    //请求临时权限
                    usbManager.requestPermission(usbDevice,intent);
                }
            }
        }
        else {
            ProductID=4;
            Toast.makeText(this, "未找到设备", Toast.LENGTH_SHORT).show();
            System.out.println("===============未找到设备==============="+ProductID);
        }
        EventBus.getDefault().post(new EventUtil("ProductID",ProductID));
        EventBus.getDefault().post(new EventUtil("Permission",Permission));
    }

    public byte[] data_to_send=new byte[64];      //发送数据缓存
    public void usb_sendData(final int length) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] bytes=new byte[length];
                bytes[0]= (byte) length;

                for (int i=1;i<length;i++){
                    bytes[i]=data_to_send[i-1];
                }
                System.out.println("=====Re====="+bytes[length-1]);
                if (usbDeviceConnection!=null&&usbEndpoint_OUT!=null){
                    usbDeviceConnection.bulkTransfer(usbEndpoint_OUT,bytes,length,2000);
                }
            }
        }).start();
    }
    private void usb_receiveData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (STATE){
                        if (usbDeviceConnection != null && usbInterface != null) {
                            if (usbDeviceConnection.bulkTransfer(usbEndpoint_IN, buffer, 64, 1000) > 0) {
                                if (buffer[0] > 0 && buffer[0] < 64) {
                                    //System.out.println("=======Data======"+String.valueOf(buffer[0]));
                                    byte[] bytes=new byte[buffer[0]-1];
                                    for (int i=0;i<buffer[0]-1;i++)
                                        bytes[i]=buffer[i+1];
                                    EventBus.getDefault().post(new EventUtil("Buffer",bytes));
                                }
                            }
                        }
                }
            }
        }).start();
    }
    @Override
    public void onStart(Intent intent, int startId) {
        scanUSBDevice();
        System.out.println("=======Service_usb OnStart==========");
        super.onStart(intent, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("===========Service OnBind============="+STATE);
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        STATE=false;
        System.out.println("===========UNBIND ============"+STATE);
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        STATE=false;
        EventBus.getDefault().unregister(this);
        System.out.println("=======Service_usb Destory=========="+STATE);
    }
}
