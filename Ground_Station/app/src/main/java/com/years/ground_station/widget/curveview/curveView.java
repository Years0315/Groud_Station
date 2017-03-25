package com.years.ground_station.widget.curveview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by Administrator on 2016/9/30 0030.
 */
public class curveView extends View {

    private final static String stringX[]={"0","10","20","30","40","50","60","70","80","90","100"};
    private final static String stringY[]={"-5000","-4500","-4000","-3500","-3000","-2500","-2000","-1500", "-1000","-500",
                                            "0","500","1000","1500","2000","2500","3000","3500","4000","4500","5000"};

    private float Height,Width;              //宽高
    private float OrignX,OrignY;             //原点坐标
    private float PadX,PadY;                 //边距
    private float AverageX=11,AverageY=21;   //坐标均分
    private float DistanceX,DistanceY;       //均分距离
    private float DistanceZ;                 //描点距离

    private float[] point1=new float[101];   //曲线1数据缓存


    private Paint paint_x,paint_y,paint_z;
    private Paint paint_axis,paint_line,paint_text;
    private Paint paint_1,paint_2,paint_3;

    private final static int DEFAULT_AXIS_LINE_COLOR =0xff000000;   //坐标轴颜色
    private final static int DEFAULT_AXIS_TEXT_COLOR =0xfff74d30;   //坐标轴字体颜色
    private final static int DEFAULT_AXIS_GRID_COLOR =0xf0157efb;   //网格颜色

    private final static int DEFAULT_CURVE_COLOR_1  =0xffff0000;    //曲线1颜色

    public curveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paintInit();
    }

    private void paintInit(){
        paint_axis=new Paint();
        paint_axis.setStrokeWidth(5);
        paint_text=new Paint();
        paint_text.setStrokeWidth(20);
        paint_line=new Paint();
        paint_line.setStrokeWidth(2);
        paint_1=new Paint();
        paint_1.setStrokeWidth(3);
    }
    private void getMeasure(){
        if (Height==0||Width==0){
            Height=getHeight();
            Width=getWidth();
            if (Height==0||Width==0)
                return;
        }
        PadX=Width/20;
        PadY=Height/20;
        OrignX=PadX;
        OrignY=Height-PadY;
        DistanceX=(Width-PadX*2)/(AverageX-1);
        DistanceY=(Height-PadY*2)/(AverageY-1);
        DistanceZ=(Width-PadX*2)/100;
    }
    public void addPoints(int data1)
    {
        int i=0;
        for (i=0;i<100;i++){
            point1[i]=point1[i+1];
        }
        point1[100]=data1;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        getMeasure();
        paint_axis.setColor(DEFAULT_AXIS_LINE_COLOR);
        canvas.drawLine(OrignX, OrignY, Width, OrignY, paint_axis);                   //X轴
        canvas.drawLine(OrignX, OrignY, OrignX, 0, paint_axis);                       //Y轴
        float temp;
        int i;
        paint_text.setColor(DEFAULT_AXIS_TEXT_COLOR);
        paint_text.setTextSize(sp2px(15));
        paint_line.setColor(DEFAULT_AXIS_GRID_COLOR);
        for (i=1;i<AverageX;i++){                                                     //X轴标注网格
            temp=OrignX+DistanceX*i;
            canvas.drawLine(temp,OrignY,temp,PadY-10,paint_line);
            canvas.drawText(stringX[i], (float) (temp-sp2px(20)*0.5), (float) (OrignY+sp2px(20)*0.75),paint_text);
        }
        for (i=0;i<AverageY;i++){                                                    //Y轴标注网格
            temp=OrignY-DistanceY*i;
            canvas.drawLine(OrignX,temp,Width-PadX+10,temp,paint_line);
            canvas.drawText(stringY[i], (float) (OrignX-stringY[i].length()*sp2px(20)/2.5), (float) (temp+sp2px(20)*0.25),paint_text);
        }

        paint_1.setColor(DEFAULT_CURVE_COLOR_1);
        paint_1.setAntiAlias(true);
        point1[50]=500;
        for (i=0;i<100;i++){
            canvas.drawLine(OrignX+DistanceZ*i,OrignY-DistanceY*(AverageY-1)/2-point1[i]/500*DistanceY,
                    OrignX+DistanceZ*(i+1),OrignY-DistanceY*(AverageY-1)/2-point1[i+1]/500*DistanceY,paint_1);
        }



        super.onDraw(canvas);
    }
    private int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }

    private int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dpVal,getResources().getDisplayMetrics());
    }
}
