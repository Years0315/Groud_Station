package com.years.ground_station.widget.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by Administrator on 2015/12/5 0005.
 */
public class ElasticScrollViewLayout extends LinearLayout {
    private static final float OVERSHOOT_TENSION = 0.75f;          //弹性系数
    private Scroller mScroller;                                    //平滑滚动器


    public ElasticScrollViewLayout(Context context) {
        super(context);
        this.setOrientation(LinearLayout.VERTICAL);
        mScroller=new Scroller(getContext(),new OvershootInterpolator(OVERSHOOT_TENSION));
    }

    public ElasticScrollViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(LinearLayout.VERTICAL);
        mScroller=new Scroller(getContext(),new OvershootInterpolator(OVERSHOOT_TENSION)); //初始化
    }
    public void smoothScrollTo(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(dx, dy);
    }
    public void smoothScrollBy(int dx, int dy) {

        // 设置mScroller的滚动偏移量
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy,800);
        // 这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
        invalidate();
    }

    @Override
    public void computeScroll() {
        // 先判断mScroller滚动是否完成
        if (mScroller.computeScrollOffset()) {

            // 这里调用View的scrollTo()完成实际的滚动
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());

            // 必须调用该方法，否则不一定能看到滚动效果
            postInvalidate();
        }
        super.computeScroll();
    }

    public final void smoothScrollToNormal() {
        smoothScrollTo(0, 0);
    }
    public final int getScrollerCurrY() {
        return mScroller.getCurrY();
    }



}
