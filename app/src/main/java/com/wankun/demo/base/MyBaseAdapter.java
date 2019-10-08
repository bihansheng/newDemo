package com.wankun.demo.base;

import android.content.Context;

import org.byteam.superadapter.SuperAdapter;
import org.byteam.superadapter.internal.SuperViewHolder;

import java.util.List;

/**
 *  使用说明https://www.jianshu.com/p/d6a76fd3ea5b
 * 本着二次封装的原则，把使用的这个库在封装一遍，如果这个库出现重大问题，使用新库时将其封装为相同的用法。
 * @author wankun

 */
public class MyBaseAdapter<T> extends SuperAdapter<T> {

    public Context mContext;

    public MyBaseAdapter(Context context, List<T> items, int layoutResId) {
        super(context, items, layoutResId);
        mContext = context;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int position, T item) {

    }

}
