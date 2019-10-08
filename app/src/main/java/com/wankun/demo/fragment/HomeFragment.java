/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wankun.demo.R;
import com.wankun.demo.base.BaseFragment;

/**
 * 〈首页〉
 *
 * @author wankun
 * @create 2019/5/21
 * @since 1.0.0
 */
public class HomeFragment extends BaseFragment {
    @Override
    public int setLayoutResId() {
        return R.layout.fragment_home;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setView();
    }

    private void setView(){}
}
