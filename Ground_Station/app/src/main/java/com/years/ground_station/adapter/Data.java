package com.years.ground_station.adapter;

/**
 * Created by Administrator on 2016/9/22 0022.
 */
public class Data {
    public int Permission=0;
    public int ProductID=0;
    public float Ang_rol=0,Ang_pit=0,Ang_yaw=0;
    public float Bar_Height=0,Real_Height=0,VSpeed=0,HSpeed=0,UIt_Height=0;
    public int Lock_State=0,GpsState=0,Fly_Mode=0;
    public boolean Receive_State=false;
    public short Acc_x=0,Acc_y=0,Acc_z=0;
    public short Gyro_x=0,Gyro_y=0,Gyro_z=0;
    public short Comp_x=0,Comp_y=0,Comp_z=0;
    public float Voltage=0,Electric=0;
    public short  PID1_P,PID1_I,PID1_D, PID2_P,PID2_I,PID2_D, PID3_P,PID3_I,PID3_D, PID4_P,PID4_I,PID4_D,
            PID5_P,PID5_I,PID5_D, PID6_P,PID6_I,PID6_D, PID7_P,PID7_I,PID7_D, PID8_P,PID8_I,PID8_D, PID9_P,PID9_I,PID9_D,
            PID10_P,PID10_I,PID10_D;

    public short AUX1=0,AUX2=0,AUX3=0,AUX4=0,AUX5=0,AUX6=0,AUX7=0,AUX8=0;  //遥控器通道数据
    public short M1=0,M2=0,M3=0,M4=0,M5=0,M6=0,M7=0,M8=0;                  //电机数据
}
