/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.wankun.demo.R;
import com.wankun.demo.application.AppActivityManager;
import com.wankun.demo.utils.InputMethodUtils;
import com.wankun.demo.utils.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.reactivestreams.Subscription;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * 〈activity基类，设置了通用的配置〉
 *
 * @author wankun
 * @create 2019/5/8
 * @since 1.0.0
 */
public class BaseActivity extends RxAppCompatActivity {
    public Context mContext;
    private Unbinder unbinder;
    CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        //activity栈管理
        AppActivityManager.getInstance().addActivity(this);
        //注册EventBus
        EventBus.getDefault().register(this);
        StatusBarUtil.setStatusBarColor(this, R.color.color_4d);
        //设置状态栏黑色字体图标
        StatusBarUtil.StatusBarLightMode(this);
        //网络请问管理 用于在ui层关闭时，停止网络请求，防止view为空而崩溃
        mCompositeDisposable = new CompositeDisposable();

    }


    /**
     * 设置布局文件，并设置标题
     *
     * @param layoutResID
     */
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        //绑定初始化ButterKnife
        unbinder = ButterKnife.bind(this);
    }


    /**
     * 接收eventBus事件，（这里是为了防止子类中没有接收方法而报错，子类不需要继承该方法）
     *
     * @param event
     */
    @Subscribe
    public void onEventMainThread(String event) {
    }


    @Override
    public void finish() {
        InputMethodUtils.closeInputMethod((Activity) mContext);
        super.finish();

    }

    @Override
    protected void onDestroy() {
        //取消注册EventBus
        EventBus.getDefault().unregister(this);
        //解绑butterknife
        unbinder.unbind();
        dispose();
        //删除activity 栈中的activity对象
        AppActivityManager.getInstance().finishActivity(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }



    /**
     * 关闭网络请求
     */
    private void  dispose(){
        if (mCompositeDisposable != null) {
            // clear时网络请求会随即cancel
            mCompositeDisposable.clear();
            mCompositeDisposable = null;
        }
    }

    /**
     * 将网络请求 对象 加入管理容器中
     */
    private void  addDisposable (Disposable d){
        if (mCompositeDisposable != null) {
            mCompositeDisposable.add(d);
        }
    }

}