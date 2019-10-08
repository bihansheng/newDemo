/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.luck.picture.lib.config.PictureConfig;
import com.wankun.demo.R;
import com.wankun.demo.base.BaseActivity;
import com.wankun.demo.fragment.WebFragment;

import butterknife.ButterKnife;

/**
 * 〈展示网页 〉
 *
 * @author wankun
 * @create 2019/7/5
 * @since 1.0.0
 */
public class WebViewActivity extends BaseActivity {
    WebFragment webFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        String url = getIntent().getStringExtra("url");
        webFragment = WebFragment.newInstance(url, 0);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_web, webFragment).commitAllowingStateLoss();
    }

    /**
     * 点击返回键，返回上一个页面，而不是退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webFragment.goBack()) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        webFragment.clearCache();
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //选择图片
        if (requestCode == PictureConfig.CHOOSE_REQUEST) {
            webFragment.fileChooseResult(resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
