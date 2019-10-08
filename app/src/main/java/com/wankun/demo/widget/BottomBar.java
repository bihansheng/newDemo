/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.widget;

import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.VisibleForTesting;
import android.support.annotation.XmlRes;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wankun.demo.R;

/**
 * 〈首页切换菜单〉
 *
 * @author wankun
 * @create 2019/6/3
 * @since 1.0.0
 */
public class BottomBar extends LinearLayout {
    public BottomBar(Context context) {
        super(context);
        init(context, null);
    }

    public BottomBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BottomBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }



    /**
     * 初始化View
     */
    private void init(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.bottom_bar_menu, this, true);

    }

}
