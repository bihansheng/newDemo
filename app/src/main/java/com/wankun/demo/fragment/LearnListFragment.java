/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Network;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.wankun.demo.R;
import com.wankun.demo.activity.WebViewActivity;
import com.wankun.demo.adapter.LearnAdapter;
import com.wankun.demo.base.BaseRecyclerFragment;
import com.wankun.demo.model.httpResponse.BaseGankEntity;
import com.wankun.demo.model.httpResponse.GankDataEntity;
import com.wankun.demo.net.NetMethodManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 〈干货列表fragment〉
 * <p>
 * 当有多个加载页面的时候，可以把 上拉刷新。下拉加载，在封装一个BaseLoadFragment；留下 Data 和 ItemView 即可。之后使用直接继承
 *
 * @author wankun
 * @create 2019/8/7
 * @since 1.0.0
 */
public class LearnListFragment extends BaseRecyclerFragment<GankDataEntity> {
    public static final String LEARN_TYPE = "type";

    /**
     * 当前页面显示的内容类型： LearnFragment.TYPE
     **/
    private String type;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        type = bundle.getString(LEARN_TYPE);
    }

    @Override
    protected void initListType() {
        mListType = BaseRecyclerFragment.TYPE_LIST;
    }

    @Override
    protected void initData() {
        mData = new ArrayList<GankDataEntity>();
        mAdapter = new LearnAdapter(getHolder(), mData, R.layout.item_learn);
    }

    @Override
    protected void setSubscribe(Observer observer) {
        NetMethodManager.getGankApi().getGankList(type, page)
                .map(new Function<BaseGankEntity<List<GankDataEntity>>, List<GankDataEntity>>() {
                    @Override
                    public List<GankDataEntity> apply(BaseGankEntity<List<GankDataEntity>> baseGankEntiry) throws Exception {
                        if (!baseGankEntiry.isError()) {
                            return baseGankEntiry.getResults();
                        }
                        return new ArrayList<>();
                    }
                })
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(observer);
    }

    @Override
    protected void ItemClickListener(View itemView, int viewType, int position) {
        GankDataEntity gankDataEntity = (GankDataEntity) mAdapter.getItem(position - 1);
        //避免内存泄露，开启一个新的进程来加载WebView。
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra("url", gankDataEntity.getUrl());
        startActivity(intent);
    }

}