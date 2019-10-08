package com.wankun.demo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.wankun.demo.R;
import com.wankun.demo.adapter.FragmentPageAdpater;
import com.wankun.demo.adapter.TabPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 带 ViewPager 的 Fragment 基类封装
 * 使用者只需要重写 setFragment() 方法填充数据，就可以显示内容。
 * <p>
 * 2019/7/5.
 *
 * @author wankun
 */
public abstract class BaseMenuFragment extends BaseFragment {

    @BindView(R.id.m_tablayout)
    TabLayout mTabLayout;
    @BindView(R.id.m_viewpager)
    ViewPager mViewpager;


    protected List<String> mTitles = new ArrayList<>();
    protected List<Fragment> mFragments = new ArrayList<Fragment>();
    protected FragmentPageAdpater mFragmentPageAdpater;

    /**
     * 建立 Fragment List，设置页面
     *
     * @param titles 页面的标题，添加的数量，必须与 页面 对应
     * @param mViews Fragment 页面，添加的数量必须与 标题 对应
     */
    public abstract void initData(List<String> titles, List<Fragment> mViews);

    /**
     * 建立 Fragment List，设置页面
     *
     * @param titles 页面的标题，添加的数量，必须与 页面 对应
     * @param mViews Fragment 页面，添加的数量必须与 标题 对应
     */
    public abstract void initView(List<String> titles, List<Fragment> mViews);


    /**
     * 滑动页面所改变的内容;给子类提供一个方法
     *
     * @param titles   页面的标题，添加的数量，必须与 页面 对应
     * @param mViews   Fragment 页面，添加的数量必须与 标题 对应
     * @param position 位置
     */
    public abstract void onPageChanged(List<String> titles, List<Fragment> mViews, int position);



    @Override
    public int setLayoutResId() {
        return R.layout.fragment_base_menu;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setView();
    }


    /**
     * 将 ViewPage 和 Tablayout 绑定
     */
    public void setView() {
        initData(mTitles, mFragments);
        initView(mTitles, mFragments);
        mFragmentPageAdpater = new FragmentPageAdpater(getChildFragmentManager(),mFragments,mTitles);
        mViewpager.setAdapter(mFragmentPageAdpater);
        mTabLayout.setupWithViewPager(mViewpager);

        // 滑动 Viewpager 修改标题
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mTitles != null && mTitles.size() > 0) {
                    onPageChanged(mTitles, mFragments, position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setTabLayout();
        //默认选中第一个
        onPageChanged(mTitles, mFragments, 0);

    }

    /**
     * 当 tab 大于 4 个，就可以滑动
     */
    protected void setTabLayout() {
        if (mFragmentPageAdpater.getCount() > 4) {
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else {
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        }
    }


}
