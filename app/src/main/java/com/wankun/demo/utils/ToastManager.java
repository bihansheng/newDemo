/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.utils;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.wankun.demo.application.DemoApplication;

import static android.os.Looper.getMainLooper;

/**
 * 〈吐司管理方法〉
 *
 * @author wankun
 * @create 2019/5/9
 * @since 1.0.0
 */
public class ToastManager {
    private static Toast toast;

    public static void initToast() {
        toast = Toast.makeText(DemoApplication.getAppContext(), "", Toast.LENGTH_SHORT);
    }

    /**
     * 提示文字
     *
     * @param msg 文本内容
     */
    public static void showToast(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            if (toast == null) {
                initToast();
            }
            toast.setText(msg);
            toast.show();
        }
    }

    /**
     * 在主线程中打印土司
     *
     * @param msg 文本内容
     */
    public static void showToastOnMain(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            //在主线程中打印土司
            new Handler(getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    showToast(msg);
                }
            });
        }
    }

    /**
     * 提示文字
     *
     * @param msgResId 文本内容ID
     */
    public static void showToast(int msgResId) {
        if (msgResId != -1) {
            if (toast == null) {
                initToast();
            }
            toast.setText(msgResId);
            toast.show();
        }
    }

    /**
     * 提示时间更长的文字
     *
     * @param msg
     */
    public static void showLongToast(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            if (toast == null) {
                initToast();
            }
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setText(msg);
            toast.show();
        }
    }

    /**
     * 居中显示toast
     *
     * @param msg
     */
    public static void showToastMid(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            if (toast == null) {
                initToast();
            }
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setText(msg);
            toast.show();
        }
    }


}