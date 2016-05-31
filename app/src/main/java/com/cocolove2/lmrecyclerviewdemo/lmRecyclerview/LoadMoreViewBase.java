package com.cocolove2.lmrecyclerviewdemo.lmRecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public abstract class LoadMoreViewBase extends LinearLayout {


    public LoadMoreViewBase(Context context) {
        this(context,null);
    }

    public LoadMoreViewBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGravity(Gravity.CENTER);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    /**
     * 处于加载失败状态
     */
    public abstract void onLoadFailureStatus();
    /**
     * 处于加载中状态
     */
    public abstract void onLoadingStatus();

    /**
     * 处于加载不到更多数据状态(此时已经没有数据了)
     */
    public abstract void onNoHasMoreStatus();

    public abstract  void onLoadCompleted(RecyclerView.Adapter adapter);
}
