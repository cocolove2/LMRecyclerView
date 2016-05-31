package com.cocolove2.lmrecyclerviewdemo.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.cocolove2.lmrecyclerviewdemo.R;

import java.util.List;


public class StaggeredAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Activity mActivity;
    private List<String> datas;
//    LinearLayout.LayoutParams lp;
//    int height;

    private int TYPE_HEADER = 1;

    public StaggeredAdapter(Activity activity, List<String> datas) {
        mActivity = activity;
        this.datas = datas;
//       final float  density=activity.getResources().getDisplayMetrics().density;
//        final int width=activity.getResources().getDisplayMetrics().widthPixels/2-(int)(8*density);
//        height=activity.getResources().getDisplayMetrics().heightPixels;
//        lp=new LinearLayout.LayoutParams(new RecyclerView.LayoutParams(width,(int)(120*density)));
//        lp.leftMargin=lp.rightMargin=lp.bottomMargin=lp.topMargin=(int)(2*density);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            return new HeaderViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.layout_header, parent, false));
        } else {
            return new StaggeredViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_img, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof  StaggeredViewHolder) {
            final StaggeredViewHolder vh = (StaggeredViewHolder) holder;
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ((StaggeredViewHolder) holder).mImageView.getLayoutParams();
            if (position % 2 == 0) {
                lp.height = 480;
            } else {
                lp.height = 640;
            }

            vh.mImageView.setLayoutParams(lp);
            Glide.with(mActivity)
                    .load(datas.get(position))
                    .placeholder(R.mipmap.ic_launcher)
                    .into(vh.mImageView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    static class StaggeredViewHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;

        public StaggeredViewHolder(View itemView) {
            super(itemView);

            mImageView = (ImageView) itemView.findViewById(R.id.item_imgs_iv);
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
}
