package com.years.ground_station.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.years.ground_station.R;
import com.years.ground_station.widget.sp.SharedPreferencesSave;

/**
 * Created by Administrator on 2016/6/10 0010.
 */
public class Activity_Set extends BaseActivity implements View.OnClickListener {
    private SharedPreferencesSave SP=new SharedPreferencesSave();
    private int Link_Mode;
    private String str_LinkMode;
    private TextView tv_LinkMode;
    private RelativeLayout relat_ble2,relat_ble4,relat_usb,relat_udp,relat_tcp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        initView();
        initListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        Link_Mode= (int) SP.getParams(this,"Link_Mode",0);
        tv_LinkMode= (TextView) findViewById(R.id.tv_linkmode);
        relat_ble2= (RelativeLayout) findViewById(R.id.relat_ble2);
        relat_ble4= (RelativeLayout) findViewById(R.id.relat_ble4);
        relat_usb= (RelativeLayout) findViewById(R.id.relat_usb);
        relat_udp= (RelativeLayout) findViewById(R.id.relat_udp);
        relat_tcp= (RelativeLayout) findViewById(R.id.relat_tcp);
        switch (Link_Mode){
            case 0:
                relat_ble2.setBackgroundColor(getResources().getColor(R.color.c4));
                str_LinkMode="蓝牙2.0";
                break;
            case 1:
                relat_ble4.setBackgroundColor(getResources().getColor(R.color.c4));
                str_LinkMode="蓝牙4.0";
                break;
            case 2:
                relat_usb.setBackgroundColor(getResources().getColor(R.color.c4));
                str_LinkMode="USB HID";
                break;
            case 3:
                relat_udp.setBackgroundColor(getResources().getColor(R.color.c4));
                str_LinkMode="UDP";
                break;
            case 4:
                relat_tcp.setBackgroundColor(getResources().getColor(R.color.c4));
                str_LinkMode="TCP";
                break;
        }
        tv_LinkMode.setText("通信方式： "+str_LinkMode);
    }
    private void initListener()
    {
        relat_ble2.setOnClickListener(this);
        relat_ble4.setOnClickListener(this);
        relat_usb.setOnClickListener(this);
        relat_udp.setOnClickListener(this);
        relat_tcp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.relat_ble2:
                btnReset();
                SP.setParams(this,"Link_Mode",0);
                str_LinkMode="蓝牙2.0";
                relat_ble2.setBackgroundColor(getResources().getColor(R.color.c4));
                break;
            case R.id.relat_ble4:
                btnReset();
                SP.setParams(this,"Link_Mode",1);
                str_LinkMode="蓝牙4.0";
                relat_ble4.setBackgroundColor(getResources().getColor(R.color.c4));
                break;
            case R.id.relat_usb:
                btnReset();
                SP.setParams(this,"Link_Mode",2);
                str_LinkMode="USB HID";
                relat_usb.setBackgroundColor(getResources().getColor(R.color.c4));
                break;
            case R.id.relat_udp:
                btnReset();
                SP.setParams(this,"Link_Mode",3);
                str_LinkMode="UDP";
                relat_udp.setBackgroundColor(getResources().getColor(R.color.c4));
                break;
            case R.id.relat_tcp:
                btnReset();
                SP.setParams(this,"Link_Mode",4);
                str_LinkMode="TCP";
                relat_tcp.setBackgroundColor(getResources().getColor(R.color.c4));
                break;
        }
        tv_LinkMode.setText("通信方式："+str_LinkMode);
    }
    private void btnReset()
    {
        relat_ble2.setBackgroundColor(getResources().getColor(R.color.c20));
        relat_ble4.setBackgroundColor(getResources().getColor(R.color.c15));
        relat_usb.setBackgroundColor(getResources().getColor(R.color.c1));
        relat_udp.setBackgroundColor(getResources().getColor(R.color.c27));
        relat_tcp.setBackgroundColor(getResources().getColor(R.color.c25));
    }
}
