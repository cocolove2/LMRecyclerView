package com.cocolove2.lmrecyclerviewdemo.lmRecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * 拥有加载更多功能的RecyclerView
 * 适用于线性,网格,瀑布流
 * <p>
 * 1.支持头部添加(1.0.1版本添加)
 * <p>
 *  version 1.0.1
 * Created by cocolove2 on 5/25/16.
 */
public class LMRecyclerView extends RecyclerView {

    private LoadMoreViewBase mLoadMoreView;
    //是否激活加载更好功能
    private boolean loadMoreEnable = true;
    //是否支持头部(针对网格和瀑布流)
    private boolean isSupportHeader = true;
    //是否正在加载数据
    private boolean isLoadingData = false;
    //是否加载失败
    private boolean isLoadingFail = false;
    //是否还有数据可以加载()
    private boolean isHasMoreData = true;

    //加载更多的接口
    private OnRecyclerLoadMoreListener mLoadingListener;
    //包装原始的adapter使其可以支持加载更多布局
    private WrapAdapter mWrapAdapter;


    public LMRecyclerView(Context context) {
        this(context, null);
    }

    public LMRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //默认使用DefaultLoadMoreView
        setLoadMoreView(new DefaultLoadMoreView(context));
    }


    public void setLoadingMoreEnable(boolean loadMoreEnable) {
        this.loadMoreEnable = loadMoreEnable;
    }

    public void setOnRecyclerLoadMoreListener(OnRecyclerLoadMoreListener loadingListener) {
        this.mLoadingListener = loadingListener;
    }

    /**
     * 设置自定义加载更多布局
     * 默认使用{@link DefaultLoadMoreView}
     *
     * @param loadMoreView 自定义加载更多布局
     */
    public void setLoadMoreView(@Nullable LoadMoreViewBase loadMoreView) {
        mLoadMoreView = loadMoreView;
        mLoadMoreView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLoadingFail) {
                    isLoadingData = true;
                    mLoadMoreView.onLoadingStatus();
                    mLoadingListener.onLoadMore(LMRecyclerView.this);
                }
            }
        });
    }

    public void setSupportHeader(boolean isSupportHeader) {
        this.isSupportHeader = isSupportHeader;
    }



    //onScrollStateChanged 中必须是要等到释放是才能触发加载,如果想在没有释放之前触发的话,在onScrolled方法中实现

    @Override
    public void onScrollStateChanged(int state) {
//        super.onScrollStateChanged(state);
            doLoadMore();
    }



    private void doLoadMore() {
        if (mLoadingListener != null
                && !isLoadingData && loadMoreEnable && !isLoadingFail) {

            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            //定位最后显示的视图的position
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                final int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = findMax(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }

            final int childCount = layoutManager.getChildCount();
            final int itemCount = layoutManager.getItemCount();

            if (childCount > 0 && lastVisibleItemPosition >= itemCount - 2 && itemCount > childCount && isHasMoreData) {
                isLoadingData = true;
                mLoadMoreView.onLoadingStatus();
                mLoadingListener.onLoadMore(this);
            }
        }
    }


    @Override
    public void setAdapter(Adapter adapter) {
        if (mLoadMoreView == null) {
            throw new NullPointerException("the footview is null");
        }
        mWrapAdapter = new WrapAdapter(adapter, mLoadMoreView);
        super.setAdapter(mWrapAdapter);
        adapter.registerAdapterDataObserver(mDataObserver);
        final LayoutManager manager = getLayoutManager();
        if (manager instanceof GridLayoutManager)
            handleGridlayoutmanager((GridLayoutManager) manager);
    }

    /**
     * 加载完成后要调用该方法
     *
     * @param isSuccess 是否刷新成功
     * @param isHasMore 服务端是否还有更多数据
     */
    public void loadCompleted(boolean isSuccess, boolean isHasMore) {
        isLoadingData = false;
        isLoadingFail = !isSuccess;
        isHasMoreData = !isSuccess | isHasMore;
        mLoadMoreView.onLoadCompleted(mWrapAdapter);
    }


    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }


    private class WrapAdapter extends Adapter<ViewHolder> {
        private static final int TYPE_FOOTER = -404;

        private Adapter adapter;
        private LoadMoreViewBase mFootView;

        public WrapAdapter(Adapter adapter, LoadMoreViewBase footView) {
            if (adapter == null)
                throw new NullPointerException("adapter is null");
            this.adapter = adapter;
            mFootView = footView;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_FOOTER) {
                return new SimpleViewHolder(mFootView);
            } else {
                return adapter.onCreateViewHolder(parent, viewType);
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            if (getLayoutManager() instanceof StaggeredGridLayoutManager)
                handleStaggerLayoutManager(holder, position);

            if (!isFooter(position)) {
                adapter.onBindViewHolder(holder, position);
            } else {
                //判断是否显示加载更多布局(防止数据不足一页时显示加载更多布局)
                if (!isShowLoadMore()) {
                    mFootView.setVisibility(GONE);
                } else {
                    mFootView.setVisibility(VISIBLE);
                }
            }
            //
            if (!isLoadingData && isLoadingFail) {
                mFootView.onLoadFailureStatus();
            } else if (!isLoadingData && !isHasMoreData) {
                mFootView.onNoHasMoreStatus();
            }
        }

        public boolean isShowLoadMore() {
            return loadMoreEnable && getChildCount() > 0 && getItemCount() > getChildCount() + 1;
        }

        public boolean isFooter(int position) {
            return position >= (getItemCount() - 1);
        }

        private void handleStaggerLayoutManager(ViewHolder holder, int position) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();

            if (layoutParams == null || !(layoutParams instanceof StaggeredGridLayoutManager.LayoutParams)) {
                holder.itemView.setLayoutParams(new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                layoutParams = holder.itemView.getLayoutParams();
            }

            if ((isSupportHeader && position == 0) || isFooter(position)) {
                ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
            } else {
                ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(false);
            }

        }

        @Override
        public int getItemCount() {
            return adapter.getItemCount() + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1) {
                return TYPE_FOOTER;
            } else {
                return adapter.getItemViewType(position);
            }
        }
    }


    private void handleGridlayoutmanager(final GridLayoutManager gridLayoutManager) {
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if ((mWrapAdapter.isFooter(position) && mWrapAdapter.isShowLoadMore()) || (isSupportHeader && position == 0)) {
                    return gridLayoutManager.getSpanCount();
                } else {
                    return 1;
                }
            }
        });
    }


    private class SimpleViewHolder extends ViewHolder {

        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }

    public interface OnRecyclerLoadMoreListener {
        void onLoadMore(LMRecyclerView lmRecyclerView);
    }

    private final AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            mWrapAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    };
}
