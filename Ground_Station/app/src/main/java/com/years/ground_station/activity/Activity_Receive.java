package com.years.ground_station.activity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.years.ground_station.R;
import com.years.ground_station.adapter.EventUtil;
import com.years.ground_station.service.Service_usb;
import com.years.ground_station.widget.sp.SharedPreferencesSave;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.years.ground_station.R.id.tv_receive;

/**
 * Created by Administrator on 2016/6/10 0010.
 */
public class Activity_Receive extends BaseActivity implements View.OnClickListener{
    private static final String TAG="===Activity Receive===";
    private SharedPreferencesSave SP=new SharedPreferencesSave();

    private int Link_Mode;
    Service_usb service_usb;
    private EditText et_Send;
    private TextView tv_Receive,tv_rx,tv_tx,tv_LinkState;
    private Button btn_Clear,btn_Send;
    private CheckBox cek_HEX;

    private String str_RX;
    private int Rx,Tx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive);
        EventBus.getDefault().register(this);
        initView();
        initListener();
        EventBus.getDefault().post(new EventUtil("Link_State",0));
    }
    private static String byte2HexString(byte[] bytes){
        String str=new String();
        for (int i=0;i<bytes.length;i++){
            int temp=bytes[i]&0xff;
            String s=Integer.toHexString(temp);
            if (s.length()<2)
                s='0'+s+" ";
            else s=s+" ";
            str+=s;
        }
        return str;
    }
    private static byte char2Byte(char c){
        return (byte)"0123456789ABCDEF".indexOf(c);
    }
    private static byte[] hexString2Bytes(String str){
        if (str==null||str.equals(""))
            return null;
        str=str.toUpperCase();
        int length=str.length()/2;
        char[] hexChars=str.toCharArray();
        byte[] bytes=new byte[length];
        for (int i=0;i<length;i++){
            int pos=i*2;
            bytes[i]= (byte) (char2Byte(hexChars[pos])<<4|char2Byte(hexChars[pos+1]));
        }
        return bytes;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EventUtil eventUtil){
        if (eventUtil.getMsg().equals("Buffer")){
            String str;
           byte[] buf= (byte[]) eventUtil.getParam();
            str=byte2HexString(buf);
            Rx+=buf.length;
            tv_rx.setText("RX "+String.valueOf(Rx));
            textScroll(str);
        }
        else if (eventUtil.getMsg().equals("Link")){
            byte[] buf= (byte[]) eventUtil.getParam();
            updateLinkState(buf);
        }
    }
    private void initListener() {
        btn_Clear.setOnClickListener(this);
        btn_Send.setOnClickListener(this);
    }

    private void initView() {
        tv_rx= (TextView) findViewById(R.id.tv_rx);
        tv_tx= (TextView) findViewById(R.id.tv_tx);
        tv_LinkState= (TextView) findViewById(R.id.tv_linkstate);
        cek_HEX= (CheckBox) findViewById(R.id.cek_hex);
        btn_Clear= (Button) findViewById(R.id.btn_clear);
        btn_Send= (Button) findViewById(R.id.btn_send);
        et_Send= (EditText) findViewById(R.id.et_send);
        tv_Receive= (TextView) findViewById(tv_receive);
        tv_Receive.setMovementMethod(ScrollingMovementMethod.getInstance());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_clear:
                tv_Receive.setText(" ");
                Rx=0;
                tv_rx.setText("RX 0");
                break;
            case R.id.btn_send:
                String str;
                str= String.valueOf(et_Send.getText());
                String s=str.replaceAll("\\s","");
                byte[] buf=hexString2Bytes(s);
                EventBus.getDefault().post(new EventUtil("Send_Buffer",buf));
                break;
        }
    }

    private void updateLinkState(byte[] buf){
        Link_Mode= (int) SP.getParams(this,"Link_Mode",0);
        if (Link_Mode==2){
            service_usb=new Service_usb();
            if (buf[0]==1)
                tv_LinkState.setText("连接到匿名数传");
            else if (buf[1]==2)
                tv_LinkState.setText("连接到匿名飞控");
            else if (buf[1]==3)
                tv_LinkState.setText("连接到第三方USB设备");
            else tv_LinkState.setText("未连接");
        }
    }
    private void textScroll(final String str)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_Receive.append(str);
                int offset=tv_Receive.getLineCount()*tv_Receive.getLineHeight();
                if (offset>tv_Receive.getHeight())
                    tv_Receive.scrollTo(0,offset-tv_Receive.getHeight());
            }
        });
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
