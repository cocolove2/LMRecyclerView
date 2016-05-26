package com.cocolove2.lmrecyclerviewdemo;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private Fragment[]mFragments=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragments=new Fragment[]{new LinearFragment(),new GridFragment(),new StaggedFragment()};

        mTabLayout=(TabLayout)findViewById(R.id.main_tabs);
        mViewPager=(ViewPager)findViewById(R.id.main_viewpager);
        mViewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mViewPager);
    }




    private class MyViewPagerAdapter extends FragmentPagerAdapter{

        final String[] TITLES={"线性","网格","瀑布流"};

        public MyViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }
    }
}
