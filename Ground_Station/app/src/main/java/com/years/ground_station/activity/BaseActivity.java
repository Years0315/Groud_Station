package com.years.ground_station.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.years.ground_station.R;

/**
 * Created by zdphpn on 2016/3/6.
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateActionBar();

    }

    protected void updateActionBar(){
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setElevation(0);
            actionBar.setDisplayHomeAsUpEnabled(true);

            View localView = getLayoutInflater().inflate(R.layout.actionbar_activity_all, null);
            actionBar.setCustomView(localView);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            ActionBar.LayoutParams localLayoutParams = new ActionBar.LayoutParams(-2, -2);
            localLayoutParams.gravity = (0x1 | 0xFFFFFFF8 & localLayoutParams.gravity);
            actionBar.setCustomView(localView, localLayoutParams);
            ((TextView)actionBar.getCustomView().findViewById(R.id.tv_title)).setText(getTitle());
        }
    }

}
