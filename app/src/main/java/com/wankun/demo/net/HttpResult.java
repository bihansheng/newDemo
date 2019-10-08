/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.net;

import java.io.Serializable;

/**
 * 〈网络请求返回数据统一格式〉
 *
 * @author wankun
 * @create 2019/5/10
 * @since 1.0.0
 */
public class HttpResult<T> implements Serializable {
    private int code = -1;
    private String msg;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}