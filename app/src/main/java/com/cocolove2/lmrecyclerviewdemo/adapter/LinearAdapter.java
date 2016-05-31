package com.cocolove2.lmrecyclerviewdemo.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.cocolove2.lmrecyclerviewdemo.R;

import java.util.List;


public class LinearAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
   private Activity mActivity;
    private List<String>datas;
    LinearLayout.LayoutParams lp;
    public LinearAdapter(Activity activity, List<String>datas){
        mActivity=activity;
        this.datas=datas;
        final float density=activity.getResources().getDisplayMetrics().density;
        lp=new LinearLayout.LayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)(120*density)));
        lp.leftMargin=lp.rightMargin=lp.bottomMargin=lp.topMargin=(int)(2*density);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mActivity).inflate(R.layout.item_img,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final LinearViewHolder vh=(LinearViewHolder)holder;
        vh.mImageView.setLayoutParams(lp);
        Glide.with(mActivity)
                .load(datas.get(position))
                .placeholder(R.mipmap.ic_launcher)
                .into(vh.mImageView);

    }

    @Override
    public int getItemCount() {
        return datas==null?0:datas.size();
    }

    static class LinearViewHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        public LinearViewHolder(View itemView) {
            super(itemView);

            mImageView=(ImageView)itemView.findViewById(R.id.item_imgs_iv);
        }
    }
}
