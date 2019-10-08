package com.wankun.demo.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.wang.avi.AVLoadingIndicatorView;
import com.wankun.demo.R;
import com.wankun.demo.utils.ToastManager;
import com.wankun.demo.widget.recycler.LinearLayoutManagerWrapper;
import com.wankun.demo.widget.recycler.OnRecycleViewScrollListener;
import org.byteam.superadapter.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * 自定义列表
 * 2016/4/8.
 * @author wankun
 */
public abstract class BaseRecyclerFragment<T> extends BaseFragment {

    public static final int TYPE_LIST = 0;
    public static final int TYPE_GRID = 1;
    public static final int TYPE_STAG_V = 2;
    public static final int TYPE_STAG_H = 3;

    /**
     * 网格列表的列数
     */
    public static final int GRID_COUNT = 2;

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.mSwipeRefreshLayout)
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    @Nullable
    @BindView(R.id.loadingView)
    AVLoadingIndicatorView mLoadingView;

    protected List<T> mData;
    protected MyBaseAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected int mListType;

    protected int page = 1;

    protected boolean isLoading = false;

    @Override
    public int setLayoutResId() {
        return R.layout.fragment_base_recycler;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListType();
        initView();
        loadMore();
    }

    @Override
    public void onDestroy() {
        stopLoad();
        stopLoadingView();
        super.onDestroy();
    }

    /**
     * 设置列表的显示方式
     * 设置 滑动、刷新、点击 事件
     */
    public void initView() {
        //必须在子页面new 出列表的Adapter

        switch (mListType) {
            //列表
            case TYPE_LIST:
                mLayoutManager = new LinearLayoutManagerWrapper(getHolder(), LinearLayoutManager.VERTICAL, false);
                break;
            //网格
            case TYPE_GRID:
                mLayoutManager = new GridLayoutManager(getHolder(), GRID_COUNT);
                break;
            //竖向瀑布流
            case TYPE_STAG_V:
                mLayoutManager = new StaggeredGridLayoutManager(GRID_COUNT, StaggeredGridLayoutManager.VERTICAL);
                break;
            //横向瀑布流
            case TYPE_STAG_H:
                mLayoutManager = new StaggeredGridLayoutManager(GRID_COUNT, StaggeredGridLayoutManager.HORIZONTAL);
                break;

            default:
                break;
        }

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new OnRecycleViewScrollListener() {
            @Override
            public void onLoadMore() {
                if (!isLoading) {
                    loadMore();
                }
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isLoading) {
                    page = 1;
                    int count = mAdapter.getCount();
                    mAdapter.clear();
                    mAdapter.notifyItemRangeRemoved(0, count);

                    loadMore();
                }
            }
        });
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int viewType, int position) {
                ItemClickListener(itemView, viewType, position);
            }
        });
    }

    /**
     * 加载更多
     */
    protected void loadMore() {
        isLoading = true;
        setRefreshing(true);
        //执行网络请求
        setSubscribe(observer);
    }

    /**
     * 网络请求成功
     */
    private Observer<List<T>> observer = new Observer<List<T>>() {

        @Override
        public void onSubscribe(Disposable d) {
            //开始 ，不管成功失败最后都会调用这个方法
        }

        @Override
        public void onComplete() {
            //完成，不管成功失败最后都会调用这个方法
        }



        @Override
        public void onError(Throwable e) {
            ToastManager.showToast(R.string.msg_net_erro);
            stopLoadingView();
            stopLoad();
        }


        @Override
        public void onNext(List<T> dataEntities) {
            stopLoadingView();
            if (dataEntities != null) {
                mAdapter.addAll(dataEntities);
            }
            stopLoad();
            page++;
        }
    };

    /**
     * 停止加载
     */
    private void stopLoad() {
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        isLoading = false;
    }

    /**
     * 停止加载动画
     */
    private void stopLoadingView() {
        if (mLoadingView != null && mLoadingView.isShown()) {
            mLoadingView.setVisibility(View.GONE);
            ((ViewGroup) mLoadingView.getParent()).removeView(mLoadingView);
        }
    }

    /**
     * 显示刷新的进度圈
     */
    protected void setRefreshing(boolean b) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(b);
        }
    }

    /**
     * 设置订阅的 发布事件，observer在父类已经写好,如果需要做特殊处理，则重写一个。
     */
    protected abstract void setSubscribe(Observer observer);

    /**
     * 设置列表的类型
     * 给 mListType 赋值以下类型
     * <p/>
     * BaseRecyclerFragment.TYPE_LIST 列表；</>
     * BaseRecyclerFragment.TYPE_GRID 网格；</>
     * BaseRecyclerFragment.TYPE_STAG_V 竖向瀑布流；
     */
    protected abstract void initListType();

    /**
     * 初始化 mData 和 mAdaper
     * <p/>
     * mData 是 List<T> <p/>
     * mAdaper 必须继承 MyBaseAdapter
     */
    protected abstract void initData();

    /**
     * 点击事件
     *
     * @param itemView 当前的 Item
     * @param viewType 当有多种 Item 时，当前的类型
     * @param position 第几个
     */
    protected abstract void ItemClickListener(View itemView, int viewType, int position);
}
