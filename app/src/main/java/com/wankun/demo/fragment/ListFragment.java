/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.wankun.demo.R;
import com.wankun.demo.base.BaseMenuFragment;

import java.util.List;

import butterknife.BindView;

/**
 * 〈产品列表〉
 *
 * @author wankun
 * @create 2019/5/21
 * @since 1.0.0
 */
public class ListFragment extends BaseMenuFragment {
    @BindView(R.id.action_bar_title_txt)
    TextView actionBarTitleTxt;



    @Override
    public int setLayoutResId() {
        return R.layout.fragment_list;
    }

    @Override
    public void initData(List<String> titles, List<Fragment> fragments) {
        addFragment(titles, fragments, "全部", 234);
        addFragment(titles, fragments, "汪星人", 235);
        addFragment(titles, fragments, "喵星人", 236);
        addFragment(titles, fragments, "哈士奇", 237);
        addFragment(titles, fragments, "小兔兔", 238);
    }

    @Override
    public void initView(List<String> titles, List<Fragment> fragments) {

    }

    @Override
    public void onPageChanged(List<String> titles, List<Fragment> fragments, int position) {
        actionBarTitleTxt.setText(titles.get(position));
    }

    private void addFragment(List<String> titles, List<Fragment> fragments, String title, int id) {
        titles.add(title);
        Fragment fragment = new SuperFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(SuperFragment.SUPER_TYPE, id);
        fragment.setArguments(bundle);
        fragments.add(fragment);
    }

}
