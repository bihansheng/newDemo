package com.wankun.demo.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Description：tab 关联的page使用的adapter
 * Created by : wankun
 * Time：2016-05-18  17-50
 */
public class TabPagerAdapter extends PagerAdapter {
    private List<View> mViewList;
    private List<String> mTitles;

    public TabPagerAdapter(List<View> mViewList, List<String> mTitles) {
        this.mViewList = mViewList;
        this.mTitles = mTitles;
    }

    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(mViewList.get(position));
        return mViewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
