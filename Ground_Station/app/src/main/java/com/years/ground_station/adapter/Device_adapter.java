package com.years.ground_station.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.years.ground_station.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/29 0029.
 */
public class Device_adapter extends BaseAdapter {
    public ArrayList<BluetoothDevice> device_list;
    private Context mcontext;

    public Device_adapter(Context context)
    {
        super();
        device_list=new ArrayList<BluetoothDevice>();
        mcontext=context;
    }
    public void addDevice(BluetoothDevice device)
    {
        if (!device_list.contains(device))
            device_list.add(device);
    }

    public BluetoothDevice getDevice(int posiation)
    {
        return device_list.get(posiation);
    }
    public void clear()
    {
        device_list.clear();
    }

    @Override
    public int getCount() {
        return device_list.size();
    }

    @Override
    public Object getItem(int position) {
        return device_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view==null)
        {
            viewHolder=new ViewHolder();
            view= LayoutInflater.from(mcontext).inflate(R.layout.device_id,null);
            viewHolder.device_Name= (TextView) view.findViewById(R.id.device_name);
            viewHolder.device_Address= (TextView) view.findViewById(R.id.device_address);
            view.setTag(viewHolder);
        }
        else  viewHolder= (ViewHolder) view.getTag();
        return view;
    }
    static class ViewHolder {
        TextView device_Name;
        TextView device_Address;
    }
}
