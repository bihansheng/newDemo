/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.reactivestreams.Subscription;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 〈fragment 基类 设置基础设置〉
 *
 * @author wankun
 * @create 2019/5/8
 * @since 1.0.0
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    public RxAppCompatActivity holder;
    public View rootView;
    private Unbinder unbinder;
    private  CompositeDisposable mCompositeDisposable;
    @Override
    public void onAttach(Context context) {
        super.onAttach( context );
        if (context instanceof Activity) {
            holder = (RxAppCompatActivity) context;
        }
    }



    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate( setLayoutResId(), container, false );
            ButterKnife.bind( this, rootView );
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView );
        }
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }


    /**
     * 注意：在fragment中eventBus的注册需要在onCreateView方法之后即onActivityCreated进行，防止view空指针
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String event) {
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDestroyView() {
        //取消注册EventBus
        EventBus.getDefault().unregister(this);
        dispose();
        //解绑butterkNife
        unbinder.unbind();
        super.onDestroyView();

    }

    public String getName() {
        return BaseFragment.class.getName();
    }


    public RxAppCompatActivity getHolder() {
        if (holder == null) {
            throw new IllegalArgumentException( "the acticity must be extends BaseActivity" );
        }
        return holder;
    }


    /**
     * 设置布局文件id
     */
    public abstract int setLayoutResId();


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