package com.years.ground_station.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.years.ground_station.R;

/**
 * Created by Administrator on 2016/6/10 0010.
 */
public class Activity_About extends BaseActivity implements View.OnClickListener {

    private TextView tv_share,tv_version;
    private RelativeLayout relat_update;
    private ImageView img_reddot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initView();
        initListener();
        tv_version.setText(getVersion());
    }
    private void initListener() {
        tv_share.setOnClickListener(this);
        relat_update.setOnClickListener(this);
    }

    private String getVersion()
    {
        PackageManager packageManager=getPackageManager();
        try {
            PackageInfo packageInfo=packageManager.getPackageInfo(getPackageName(),0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return  "版本未知";
        }
    }
    private void initView() {
        tv_share= (TextView) findViewById(R.id.tv_share);
        tv_version= (TextView) findViewById(R.id.tv_version);
        relat_update= (RelativeLayout) findViewById(R.id.rl_update);
        img_reddot= (ImageView) findViewById(R.id.img_red_dot);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_share:
                Toast.makeText(Activity_About.this, "功能未添加", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_update:
                Toast.makeText(Activity_About.this, getVersion(), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
