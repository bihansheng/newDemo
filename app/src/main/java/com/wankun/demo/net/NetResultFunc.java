/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.net;

import io.reactivex.functions.Function;
 /**
 * 〈网络请求结束预处理方法〉
 *  用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
 * @author wankun
 * @create 2019/5/10
 * @since 1.0.0
 */
public class NetResultFunc<T> implements Function<HttpResult<T>, T> {
     @Override
     public T apply(HttpResult<T> httpResult) throws Exception {
         //flg为0，即请求结果错误
         if (httpResult.getCode() == NetException.RES_IS_ERROR ) {
             //打印服务端传来的数据
             throw new NetException(NetException.RES_IS_ERROR,httpResult.getMsg());
             //其他的错误，将flg传给具体的页面去
         }else if(httpResult.getCode() != NetException.RES_IS_SUCCESS){
             throw new NetException(httpResult.getCode(),httpResult.getMsg());
         }
         return httpResult.getData();
     }
 }