/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.net;

import com.wankun.demo.utils.ToastManager;

/**
 * 〈自定义网络错误〉
 *
 * @author wankun
 * @create 2019/5/10
 * @since 1.0.0
 */
public class NetException extends RuntimeException {

    /**
     * 返回数据为空
     */
    public static final int RES_IS_NULL = -1;
    /**
     * 请求成功
     */
    public static final int RES_IS_SUCCESS = 0;
    /**
     * 请求失败
     */
    public static final int RES_IS_ERROR = 1;
    /**
     * 服务器错误
     */
    public static final int SERVER_ERROR = 3;
    /**
     * 用户尚未登录
     */
    public static final int NO_LOGIN = 9;

    public NetException(int resultCode, String detailMessage) {
        this(getApiExceptionMessage(resultCode, detailMessage));
    }

    public NetException(int resultCode) {
        this(getApiExceptionMessage(resultCode, ""));
    }

    public NetException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 处理服务端传递的错误代码
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     *
     * @param code
     */
    private static String getApiExceptionMessage(int code, String detailMessage) {
        String message;
        switch (code) {
            case RES_IS_NULL:
                message = "返回数据有误";
                break;
            case NO_LOGIN:
                message = detailMessage;
                //TODO　 清除本地数据 并跳转到登录界面
                ToastManager.showToastOnMain("请先登录");
                break;
            case SERVER_ERROR:
                message = "服务器错误:" + detailMessage;
                break;

            default:

                message = detailMessage + "：" + code;

        }
        return message;
    }



    public static class TokenExpiredException extends RuntimeException {
        public TokenExpiredException() {
            super("Token 过期");
        }
    }

    public static class OfflineException extends RuntimeException {
        public OfflineException() {
            super("没有网络连接");
        }
    }

    public static class TimeOutException extends RuntimeException {
        public TimeOutException() {
            super("连接超时");
        }
    }
}