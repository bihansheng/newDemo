/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 〈软键盘的显示 隐藏控制，〉
 *
 * 相关方法好像有时候不起作用
 *
 *
 * @author wankun
 * @create 2019/5/9
 * @since 1.0.0
 */
public class InputMethodUtils {
    /**
     * 显示软键盘
     */
    public static void showInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    /**
     * 显示软键盘
     */
    public static void showInputMethod(Context context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }




    /**
     * 隐藏输入法
     *
     * @param activity Activity
     */
    public static void closeInputMethod(Activity activity) {
        try {
            InputMethodManager manager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager.isActive() && activity.getCurrentFocus()!=null) {
                manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 隐藏输入法
     * @param myView   控件
     */
    public static void closeInputMethod( View myView) {
        try {
            InputMethodManager manager = (InputMethodManager) myView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(myView.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}