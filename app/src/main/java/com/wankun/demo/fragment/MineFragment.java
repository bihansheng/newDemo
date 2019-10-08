/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.orhanobut.logger.Logger;
import com.wankun.demo.R;
import com.wankun.demo.base.BaseMenuFragment;
import com.wankun.demo.common.ConfigDev;
import com.wankun.demo.manage.CommonNetManager;
import com.wankun.demo.model.httpResponse.UpdateVersion;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 〈我的〉
 *
 * @author wankun
 * @create 2019/5/21
 * @since 1.0.0
 */
public class MineFragment extends BaseMenuFragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Override
    public int setLayoutResId() {
        return R.layout.fragment_mine;
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

    }


    void setViews() {
        CommonNetManager.getApkVersion(new Observer<UpdateVersion>() {
            @Override
            public void onSubscribe(Disposable d) {
                Logger.e("onSubscribe>>>");
            }

            @Override
            public void onNext(UpdateVersion updateVersion) {
                Logger.e("onNext>>>" + updateVersion.toString());
            }

            @Override
            public void onError(Throwable e) {
                Logger.e("onError>>>" + e.getLocalizedMessage());
            }

            @Override
            public void onComplete() {
                Logger.e("onComplete>>>");
            }
        });
    }


    private void addFragment(List<String> titles, List<Fragment> fragments, String title, String type) {
        titles.add(title);

        Fragment fragment = new LearnListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        fragments.add(fragment);
    }

}
