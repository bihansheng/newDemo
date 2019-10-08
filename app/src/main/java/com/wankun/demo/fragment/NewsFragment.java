/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.wankun.demo.R;
import com.wankun.demo.base.BaseMenuFragment;
import com.wankun.demo.common.ConfigDev;

import java.util.List;

import butterknife.BindView;

/**
 * 〈消息〉
 *
 * @author wankun
 * @create 2019/5/21
 * @since 1.0.0
 */
public class NewsFragment extends BaseMenuFragment {
    @BindView(R.id.action_bar_title_txt)
    TextView actionBarTitleTxt;
    public static final String LEARN_TYPE = "type";

    @Override
    public int setLayoutResId() {
        return R.layout.fragment_list;
    }


    private void addFragment(List<String> titles, List<Fragment> fragments, String title, String type) {
        titles.add(title);

        Fragment fragment = new LearnListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(LEARN_TYPE, type);
        fragment.setArguments(bundle);
        fragments.add(fragment);
    }

    @Override
    public void initData(List<String> titles, List<Fragment> fragments) {
        addFragment(titles, fragments, ConfigDev.GANK_TYPE_ANDROID, ConfigDev.GANK_TYPE_ANDROID);
        addFragment(titles, fragments, ConfigDev.GANK_TYPE_IOS, ConfigDev.GANK_TYPE_IOS);
        addFragment(titles, fragments, ConfigDev.GANK_TYPE_WEB, ConfigDev.GANK_TYPE_WEB);
    }

    @Override
    public void initView(List<String> titles, List<Fragment> mViews) {

    }

    @Override
    public void onPageChanged(List<String> titles, List<Fragment> mViews, int position) {
        actionBarTitleTxt.setText(titles.get(position));

    }
}

