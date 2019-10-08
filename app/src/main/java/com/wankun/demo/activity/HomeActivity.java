package com.wankun.demo.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.roughike.bottombar.BottomBar;
import com.wankun.demo.R;
import com.wankun.demo.base.BaseActivity;
import com.wankun.demo.base.BaseFragment;
import com.wankun.demo.common.ConfigDev;
import com.wankun.demo.fragment.ListFragment;
import com.wankun.demo.fragment.MineFragment;
import com.wankun.demo.fragment.NewsFragment;
import com.wankun.demo.fragment.WebFragment;
import com.wankun.demo.utils.ToastManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author wankun
 */
public class HomeActivity extends BaseActivity {


    BaseFragment homeFragment;
    BaseFragment listFragment;
    BaseFragment mineFragment;
    BaseFragment newsFragment;
    /**
     * 当前显示的fragment
     */
    BaseFragment showFragment;

    public FragmentManager manager;
    @BindView(R.id.bottom_bar)
    BottomBar bottomBar;
    @BindView(R.id.view_line)
    View viewLine;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        manager = getSupportFragmentManager();
        //初始布局
        initLayout();


    }


    /**
     * 初始化布局
     */
    private void initLayout() {
        /**
         * 底部按钮点击 选择监听
         */
        bottomBar.setOnTabSelectListener(this::switchFragment);
        /**
         * 底部按钮 重新点击  选择监听
         */
        bottomBar.setOnTabReselectListener(tabId -> {
            if (tabId == R.id.tab_home) {
                // The tab with id R.id.tab_favorites was reselected,
                // change your content accordingly.
            }
        });

        /**
         * 底部按钮  选择拦截
         */
        bottomBar.setTabSelectionInterceptor((oldTabId, newTabId) -> {
            if (newTabId == R.id.tab_home) {
                return false;
            }

            return false;
        });
        //默认选中第一个
        bottomBar.selectTabAtPosition(0);
    }


    /**
     * 切换Fragment
     */
    public void switchFragment(int id) {
        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (showFragment != null) {
            transaction.hide(showFragment);
        }
        showFragment = getFragmentById(id, transaction);
        transaction.show(showFragment);
        transaction.commit();

    }


    /**
     * 获取fragment
     *
     * @param id
     */
    private BaseFragment getFragmentById(int id, FragmentTransaction transaction) {
        switch (id) {

            case R.id.tab_list:
                listFragment = (ListFragment) manager.findFragmentByTag("List");
                if (listFragment == null) {
                    listFragment = new ListFragment();
                    transaction.add(R.id.content, listFragment, "List");

                }
                return listFragment;
            case R.id.tab_news:
                newsFragment = (NewsFragment) manager.findFragmentByTag("NEWs");
                if (newsFragment == null) {
                    newsFragment = new NewsFragment();
                    transaction.add(R.id.content, newsFragment, "NEWs");
                }
                return newsFragment;
            case R.id.tab_mine:
                mineFragment = (MineFragment) manager.findFragmentByTag("MINE");
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    transaction.add(R.id.content, mineFragment, "MINE");
                }
                return mineFragment;
            default:
            case R.id.tab_home:
                homeFragment = (WebFragment) manager.findFragmentByTag("HOME");
                if (homeFragment == null) {
                    homeFragment = WebFragment.newInstance(ConfigDev.HOME_PAGE_URL, WebFragment.NORMAL);
                    transaction.add(R.id.content, homeFragment, "HOME");
                }
                return homeFragment;
        }
    }

    @OnClick(R.id.view_line)
    public void onClick() {
        ToastManager.showLongToast("按钮被点击");
    }
}
