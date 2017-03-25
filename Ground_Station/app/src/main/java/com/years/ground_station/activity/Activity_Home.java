package com.years.ground_station.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.years.ground_station.R;
import com.years.ground_station.adapter.EventUtil;
import com.years.ground_station.service.Service_Test;
import com.years.ground_station.service.Service_ble2;
import com.years.ground_station.service.Service_ble4;
import com.years.ground_station.service.Service_tcp;
import com.years.ground_station.service.Service_udp;
import com.years.ground_station.service.Service_usb;
import com.years.ground_station.widget.sp.SharedPreferencesSave;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/6/9 0009.
 */
public class Activity_Home extends AppCompatActivity implements View.OnClickListener {
    private int ProductID;
    private int Permission;
    private static int TIME=5;
    private int Link_Mode=0;
    private SharedPreferencesSave SP=new SharedPreferencesSave();
    Service_usb service_usb;
    /*************************PopupWindow****************************/
    private View deviceWindow;
    private PopupWindow popupWindow;
    private ListView list_device;
    private RelativeLayout relat_scan, relat_Receive,relat_Frame,relat_Set,relat_Curve,relat_State,
            relat_Control,relat_Calibration,relat_PID,relat_Connection,relat_Other;
    private TextView tv_ScanResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        EventBus.getDefault().register(this);

        initActionBar();

        initView();

        initListener();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventUtil eventUtil){
        if (eventUtil.getMsg().equals("ProductID")){
            ProductID= (int) eventUtil.getParam();
        }
        else if (eventUtil.getMsg().equals("Permission")){
            Permission= (int) eventUtil.getParam();
            updateLinkState();
        }
        else if (eventUtil.getMsg().equals("Link_State")){
            byte[] buf=new byte[2];
            buf[0]= (byte) Permission;
            buf[1]= (byte) ProductID;
            //System.out.println("=======Pei Pro====="+p);
            EventBus.getDefault().post(new EventUtil("Link",buf));
        }
    }
    Timer timer=new Timer();
    TimerTask timerTask=new TimerTask() {
        @Override
        public void run() {
          //  AnlData1(service_usb.getBuffer());
        }
    };
    /**================解析数据变量==================**/
    private int sta=0;                             //接收状态
    private byte[] data_Buffer=new byte[256];      // 数据缓存
    private byte[] buffer = new byte[64];
    private int f_len=0;                           //本帧数据长度
    private int  r_len=0;                           //已经接收数据长度
    public byte[] data_to_send=new byte[64];      //发送数据缓存
    public byte int16tobyteL(short cnt)
    {
        byte r=0;
        r=(byte)(cnt&0xff);
        return r;
    }
    public byte int16tobyteH(short cnt)
    {
        byte r=0;
        r=(byte)((cnt>>8)&0xff);
        return r;
    }
    public short  bytetoint16(int cnt) {
        short r=0;
        r<<=8;
        r|=(data_Buffer[cnt]&0x00ff);
        r<<=8;
        r|=(data_Buffer[cnt+1]&0x00ff);
        return r;
    }
    public int bytetoint32(int cnt) {
        int s=0,t=0;
        s |= (data_Buffer[cnt] & 0x00ff);
        s <<= 8;
        s |= (data_Buffer[cnt+1] & 0x00ff);

        t |= (data_Buffer[cnt+2] & 0x00ff);
        t <<= 8;
        t |= (data_Buffer[cnt+3] & 0x00ff);
        t=(s<<16)|t;
        return t;
    }
    private void AnlData1(byte[] data) {
        System.out.println("========分析数据第一层=========");
        int length=data[0];
        for (int i=1;i<=length;i++)
        {
            if (sta==0)                    //验证第一个AA
            {
                if (data[i]==(byte)0xaa)
                    sta=1;
            }
            else if (sta==1)               //验证第二个AA
            {
                if (data[i]==(byte)0xaa) {
                    sta=2;
                }
                else sta=0;
            }
            else if (sta==2)               //获取功能字
            {
                sta=3;
                data_Buffer[0]=(byte)0xaa;
                data_Buffer[1]=(byte)0xaa;
                data_Buffer[2]=data[i];
            }
            else if (sta==3)               //接收数据长度字节
            {
                f_len=data[i];
                //System.out.println("+++++++++++++++++++++++"+f_len);
                if (f_len>20||f_len<=0)
                    sta=0;
                else {
                    sta=4;
                    data_Buffer[3]=data[i];}
                r_len=0;
            }
            else if (sta==4)               //接收数据
            {
                data_Buffer[r_len+4]=data[i];
                r_len++;
                if (r_len==f_len)
                    sta=5;
            }
            else if (sta==5)               //进行sum校验
            {
                byte sum=0;
                data_Buffer[r_len+4]=data[i];
                for (int j=0;j<(data_Buffer[3]+4);j++)
                    sum += data_Buffer[j];
                if (sum==data[i])
                {
                    AnlData2();
                }
                sta=0;
            }
        }
    }
    private void AnlData2()
    {
        System.out.println("========分析数据第二层=========");
        switch (data_Buffer[2])
        {
            case (byte)0x00:              //版本信息
                break;
            case (byte)0x01:
                break;
            case (byte)0x02:                     //sensor
                break;
            case (byte)0x03:
                break;
            case (byte)0x06:
                break;
            case (byte)0x07:
                break;
            case (byte)0x10:

                break;
            case (byte)0x11:

                break;
            case (byte)0x12:

                break;
            case (byte)0x13:

                break;
            default:
                break;
        }
    }
    private void initView() {
        relat_scan= (RelativeLayout) findViewById(R.id.relat_scan);
        tv_ScanResult= (TextView) findViewById(R.id.tv_scanresult);
        relat_Receive= (RelativeLayout) findViewById(R.id.relat_receive);
        relat_Frame= (RelativeLayout) findViewById(R.id.relat_frame);
        relat_Set= (RelativeLayout) findViewById(R.id.relat_set);
        relat_Curve= (RelativeLayout) findViewById(R.id.relat_curve);
        relat_State= (RelativeLayout) findViewById(R.id.relat_state);
        relat_Control= (RelativeLayout) findViewById(R.id.relat_control);
        relat_Calibration= (RelativeLayout) findViewById(R.id.relat_calibration);
        relat_PID= (RelativeLayout) findViewById(R.id.relat_pid);
        relat_Connection= (RelativeLayout) findViewById(R.id.relat_connection);
        relat_Other= (RelativeLayout) findViewById(R.id.relat_other);
        if((int)SP.getParams(this,"Link_Mode",0)==0)
        {
            SP.setParams(this,"Link_Mode",2);
        }
    }
    private void updateLinkState(){
        if (ProductID==4)
            tv_ScanResult.setText("未搜索到设备");
        if (Permission==1){
            if (ProductID==1)
                tv_ScanResult.setText("连接到匿名数传");
            else if (ProductID==2)
                tv_ScanResult.setText("连接到匿名飞控");
            else if (ProductID==3)
                tv_ScanResult.setText("连接到第三方USB设备");
        }
        else if (Permission==2){
            if (ProductID==1)
                tv_ScanResult.setText("搜索到匿名数传");
            else if (ProductID==2)
                tv_ScanResult.setText("搜索到匿名飞控");
            else if (ProductID==3)
                tv_ScanResult.setText("搜索到第三方USB设备");
        }
    }
    private void initListener(){
        relat_scan.setOnClickListener(this);
        relat_Receive.setOnClickListener(this);
        relat_Frame.setOnClickListener(this);
        relat_Set.setOnClickListener(this);
        relat_Curve.setOnClickListener(this);
        relat_State.setOnClickListener(this);
        relat_Control.setOnClickListener(this);
        relat_Calibration.setOnClickListener(this);
        relat_PID.setOnClickListener(this);
        relat_Connection.setOnClickListener(this);
        relat_Other.setOnClickListener(this);
    }

    private void initActionBar() {
        ActionBar localActionBar=getSupportActionBar();
        if (localActionBar!=null)
        {
            localActionBar.setElevation(0);
            ActionBar.LayoutParams localLayoutParams=new ActionBar.LayoutParams(-2,-2);
            localLayoutParams.gravity=(0x1|0xfffffff8&localLayoutParams.gravity);

            localActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_CUSTOM|ActionBar.DISPLAY_HOME_AS_UP);
            localActionBar.setDisplayShowCustomEnabled(true);
            localActionBar.setDisplayShowHomeEnabled(false);
            localActionBar.setDisplayHomeAsUpEnabled(false);
            View localView=null;

            localView=getLayoutInflater().inflate(R.layout.actionbar_home,null);

            localActionBar.setCustomView(localView, localLayoutParams);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.relat_scan:
                Link_Mode= (int) SP.getParams(this,"Link_Mode",0);
                serviceStart(Link_Mode);
                break;
            case R.id.relat_receive:
                startActivity(new Intent(this,Activity_Receive.class));
                break;
            case R.id.relat_frame:
                break;
            case R.id.relat_set:
                Intent intent_set=new Intent(Activity_Home.this,Activity_Set.class);
                startActivity(intent_set);
                break;
            case R.id.relat_curve:
                startActivity(new Intent(this,Activity_Curve.class));
                break;
            case R.id.relat_state:
                break;
            case R.id.relat_control:
                break;
            case R.id.relat_calibration:
                break;
            case R.id.relat_pid:
                break;
            case R.id.relat_connection:
                break;
            case R.id.relat_other:
                break;
        }
    }
    private void serviceStart(int mode)
    {
        switch (mode)
        {
            case 0:
                startService(new Intent(this, Service_ble2.class));
                break;
            case 1:
                startService(new Intent(this, Service_ble4.class));
                break;
            case 2:
                Intent service_usb=new Intent(this,Service_usb.class);
                startService(service_usb);
                break;
            case 3:
                startService(new Intent(this, Service_udp.class));
                break;
            case 4:
                startService(new Intent(this, Service_tcp.class));
                break;
            case 5:
                startService(new Intent(this, Service_Test.class));
                break;
        }
    }
    private void serviceStop(int mode)
    {
        switch (mode)
        {
            case 0:
                stopService(new Intent(this, Service_ble2.class));
                break;
            case 1:
                stopService(new Intent(this, Service_ble4.class));
                break;
            case 2:
                stopService(new Intent(this, Service_usb.class));
                break;
            case 3:
                stopService(new Intent(this, Service_udp.class));
                break;
            case 4:
                stopService(new Intent(this, Service_tcp.class));
                break;
            case 5:
                stopService(new Intent(this,Service_Test.class));
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_about)
        {
            Intent intent =new Intent(Activity_Home.this,Activity_About.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        System.out.println("======Activity_Home  Restart====");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK)
            exitDialog();
        return super.onKeyDown(keyCode, event);
    }
    private void exitDialog()
    {
        Dialog dialog=new AlertDialog.Builder(this).setTitle("退出手机调试助手？").setMessage("确定退出么 小伙儿？").
                setPositiveButton("退出",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Activity_Home.this.finish();
                    }
                }).setNegativeButton("取消",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        serviceStop(Link_Mode);
      //  timer.cancel();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
