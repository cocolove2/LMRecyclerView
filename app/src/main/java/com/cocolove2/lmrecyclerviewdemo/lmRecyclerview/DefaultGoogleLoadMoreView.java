package com.cocolove2.lmrecyclerviewdemo.lmRecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.cocolove2.lmrecyclerviewdemo.view.CircleImageView;
import com.cocolove2.lmrecyclerviewdemo.view.MaterialProgressDrawable;


public class DefaultGoogleLoadMoreView extends LoadMoreViewBase {
    private final int[] colors = {
            0xFFFF0000, 0xFFFF7F00, 0xFFFFFF00, 0xFF00FF00
            , 0xFF00FFFF, 0xFF0000FF, 0xFF8B00FF};

    private static final int CIRCLE_BG_LIGHT = 0xFFFAFAFA;
    private static final int CIRCLE_DIAMETER = 40;

    private CircleImageView mCircleView;
    private MaterialProgressDrawable mProgress;

    public DefaultGoogleLoadMoreView(Context context) {
        this(context, null);
    }

    public DefaultGoogleLoadMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        createProgressView(context);
    }

    private void createProgressView(Context context) {
        mCircleView = new CircleImageView(context, CIRCLE_BG_LIGHT, CIRCLE_DIAMETER / 2);
        mProgress = new MaterialProgressDrawable(context, this);
        mProgress.setBackgroundColor(CIRCLE_BG_LIGHT);
        mProgress.setAlpha(255);
        mCircleView.setImageDrawable(mProgress);
        addView(mCircleView);
    }


    @Override
    public void onLoadFailureStatus() {
        Log.e("TAG", "onLoadFailureStatus");
    }

    @Override
    public void onLoadingStatus() {
        Log.e("TAG", "onLoadingStatus");
        mCircleView.setVisibility(VISIBLE);
        mProgress.start();
    }

    @Override
    public void onNoHasMoreStatus() {
        Log.e("TAG", "onNoHasMoreStatus");
    }

    @Override
    public void onLoadCompleted(final RecyclerView.Adapter adapter) {
        mCircleView.setVisibility(INVISIBLE);
        mProgress.stop();
        postDelayed(new Runnable() {
            @Override
            public void run() {
               adapter.notifyDataSetChanged();
            }
        },500);
    }



}
