/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.wankun.demo.R;
import com.wankun.demo.adapter.SuperListAdapter;
import com.wankun.demo.base.BaseRecyclerFragment;
import com.wankun.demo.model.httpResponse.SuperGalleryResponse;
import com.wankun.demo.net.NetMethodManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.functions.Function;

/**
 * 〈我的〉
 *
 * @author wankun
 * @create 2019/5/21
 * @since 1.0.0
 */
public class SuperFragment extends BaseRecyclerFragment {
    public static final String SUPER_TYPE = "type";
    /**
     * 当前页面显示的分类id
     */
    private int typeId;

    /**
     * 每页显示多少个
     */
    private static final int ROWS = 20;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        typeId = getArguments().getInt(SuperFragment.SUPER_TYPE);
    }


    @Override
    @SuppressLint("CheckResult")
    protected void setSubscribe(Observer observer) {
        //请求数据
        NetMethodManager.getTngouApi().getPetList(typeId, page * ROWS).map(new Function<SuperGalleryResponse, List<SuperGalleryResponse.ListBean>>() {
            @Override
            public List<SuperGalleryResponse.ListBean> apply(SuperGalleryResponse superGalleryEntity) throws Exception {
                //解析返回数据，
                if (superGalleryEntity.getCount() > 0 && superGalleryEntity.getList() !=null) {
                    return superGalleryEntity.getList();
                }
                return new ArrayList<SuperGalleryResponse.ListBean>();
            }
        }).compose(((RxAppCompatActivity) getHolder()).bindUntilEvent(ActivityEvent.DESTROY))
                .compose(NetMethodManager.IO_UI)
                .subscribeWith(observer);
    }

    @Override
    protected void initListType() {
        mListType = BaseRecyclerFragment.TYPE_STAG_V;
    }

    @Override
    protected void initData() {
        mData = new ArrayList<SuperGalleryResponse.ListBean>();
        mAdapter = new SuperListAdapter(getHolder(), mData, R.layout.item_image_v);
    }


    @Override
    protected void ItemClickListener(View itemView, int viewType, int position) {
        //点击事件
    }
}
