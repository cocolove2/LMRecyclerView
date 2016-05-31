package com.cocolove2.lmrecyclerviewdemo.lmRecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cocolove2.lmrecyclerviewdemo.R;



public class DefaultLoadMoreView extends LoadMoreViewBase {
    private static final String TEXT_STATUS_LOADING = "正在加载";
    private static final String TEXT_STATUS_LOAD_FAIL = "重新加载";
    private static final String TEXT_STATUS_NO_MORE_DATA = "已经到底啦";
    /**
     * progressbar的颜色使用colorAccent
     */
    private ProgressBar mProgressBar;
    private TextView mStatusTv;
    private LinearLayout mRootLinear;



    public DefaultLoadMoreView(Context context) {
        this(context, null);
    }

    public DefaultLoadMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_recycler_loadmore, this, true);
        mProgressBar = (ProgressBar) findViewById(R.id.layout_recycler_loadmore_progressbar);
        mStatusTv = (TextView) findViewById(R.id.layout_recycler_loadmore_textstatus);
        mRootLinear=(LinearLayout)findViewById(R.id.layout_recycler_loadmore_root);
    }

    @Override
    public void onLoadFailureStatus() {
        if(mRootLinear.getVisibility()!=VISIBLE)
        mRootLinear.setVisibility(VISIBLE);
        if (mProgressBar.getVisibility() == VISIBLE)
            mProgressBar.setVisibility(GONE);

        mStatusTv.setText(TEXT_STATUS_LOAD_FAIL);
    }

    @Override
    public void onLoadingStatus() {
        if(mRootLinear.getVisibility()!=VISIBLE)
            mRootLinear.setVisibility(VISIBLE);
        if (mProgressBar.getVisibility() != VISIBLE)
            mProgressBar.setVisibility(VISIBLE);

          mStatusTv.setText(TEXT_STATUS_LOADING);
    }

    @Override
    public void onNoHasMoreStatus() {
        if(mRootLinear.getVisibility()!=VISIBLE)
            mRootLinear.setVisibility(VISIBLE);
        if (mProgressBar.getVisibility() == VISIBLE)
            mProgressBar.setVisibility(GONE);

        mStatusTv.setText(TEXT_STATUS_NO_MORE_DATA);
    }

    @Override
    public void onLoadCompleted(RecyclerView.Adapter adapter) {
        adapter.notifyDataSetChanged();
    }


}
