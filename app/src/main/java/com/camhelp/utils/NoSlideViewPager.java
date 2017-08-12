package com.camhelp.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * Created by storm on 2017-08-12.
 * 自定义可设置不可滑动的ViewPaper
 */

public class NoSlideViewPager extends ViewPager {
    //定义一个布尔变量来控制是否允许滑动，该变量的set方法就直接决定了该viewpager是否可以滑动
    private boolean isCanScroll = true;
    private Context context;

    public NoSlideViewPager(Context context) {
        super(context);
        this.context = context;
    }

    public NoSlideViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isCanScroll) {
            //允许滑动则应该调用父类的方法
            return super.onTouchEvent(ev);
        } else {
            //禁止滑动则不做任何操作，直接返回true即可
            return true;
        }
    }

    @Override
    public void scrollTo(int x, int y) {

        /**
         *必须这样重写，否则会出现画面"一半，一半"的现象，根据Debug来分析可能是虽然onTouchEvent方法返回了true
         *但是依然在返回true前，viewpager开始调用了scrollTo方法，导致画面"拖出来一点"
         */
        if (isCanScroll) {
            //只有允许滑动的时候才调用滑动的方法
            super.scrollTo(x, y);
        }
    }

    //设置是否允许滑动，true是可以滑动，false是禁止滑动
    public void setIsCanScroll(boolean isCanScroll) {
        this.isCanScroll = isCanScroll;
    }
}