package com.cocolove2.lmrecyclerviewdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cocolove2.lmrecyclerviewdemo.adapter.GridAdapter;
import com.cocolove2.lmrecyclerviewdemo.adapter.LinearAdapter;
import com.cocolove2.lmrecyclerviewdemo.lmRecyclerview.LMRecyclerView;

import java.util.ArrayList;
import java.util.List;


public class GridFragment extends Fragment implements LMRecyclerView.OnRecyclerLoadMoreListener{
    LMRecyclerView mLMRecyclerView;


    private String[] images = {"http://img2.imgtn.bdimg.com/it/u=3093785514,1341050958&fm=21&gp=0.jpg",
            "http://img2.3lian.com/2014/f2/37/d/40.jpg",
            "http://d.3987.com/sqmy_131219/001.jpg",
            "http://img2.3lian.com/2014/f2/37/d/39.jpg",
            "http://www.8kmm.com/UploadFiles/2012/8/201208140920132659.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg"
    };

    private List<String>mDatas=new ArrayList<>();

    private GridAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        mLMRecyclerView = (LMRecyclerView) view.findViewById(R.id.fragment_content_lmrv);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDatas();
        mLMRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2, LinearLayoutManager.VERTICAL,false));
        mAdapter = new GridAdapter(getActivity(), mDatas);
        mLMRecyclerView.setAdapter(mAdapter);
        mLMRecyclerView.setOnRecyclerLoadMoreListener(this);
    }

    private void initDatas(){
        mDatas.clear();
        for(int i=0;i<images.length;i++){
            mDatas.add(images[i]);
            mDatas.add(images[i]);
            mDatas.add(images[i]);
        }
    }

    @Override
    public void onLoadMore(final LMRecyclerView lmRecyclerView) {
        mLMRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDatas.add(images[0]);
                mDatas.add(images[1]);
                mDatas.add(images[2]);
                lmRecyclerView.loadCompleted(true,true);
            }
        },3000);
    }
}
