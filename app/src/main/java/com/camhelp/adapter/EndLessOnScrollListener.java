package com.camhelp.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by storm on 2017-07-24.
 * 实现RecyclerView上拉加载更多功能
 * 问题：未检测滑动方向，到底部上滑也会加载更多
 * */

public abstract class EndLessOnScrollListener extends  RecyclerView.OnScrollListener{

    //声明一个LinearLayoutManager
    private LinearLayoutManager mLinearLayoutManager;

    //当前页，从0开始
    public int currentPage = 0;

    //已经加载出来的Item的数量
    private int totalItemCount;

    //主要用来存储上一个totalItemCount
    private int previousTotal = 0;

    //在屏幕上可见的item数量
    private int visibleItemCount;

    //在屏幕可见的Item中的第一个
    private int firstVisibleItem;

    //是否正在上拉数据
    private boolean loading = true;

    public EndLessOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }


    /*滑到底部检测，只要一到底部就会执行*/
//    @Override
//    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//        super.onScrolled(recyclerView, dx, dy);
//        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
//                >= recyclerView.computeVerticalScrollRange()) {
//            onLoadMore(1);
//        }
//    }

    /*滑到底部再划一下才执行*/
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange()) {
            if (newState == 1) {
                onLoadMore(1);
            }
        }
    }

    /**
     * 提供一个抽闲方法，在Activity中监听到这个EndLessOnScrollListener
     * 并且实现这个方法
     * */
    public abstract boolean onLoadMore(int currentPage);
}
