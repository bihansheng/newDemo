/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.net;

import android.support.annotation.NonNull;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.wankun.demo.application.DemoApplication;
import com.wankun.demo.common.ConfigDev;
import com.wankun.demo.common.Constant;
import com.wankun.demo.net.api.GankApi;
import com.wankun.demo.net.api.TngouApi;
import com.wankun.demo.net.api.UserApi;
import com.wankun.demo.utils.PackageInfoUtil;
import com.wankun.demo.utils.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ConnectionPool;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 〈http 网络请求 实例方法〉
 *
 * @author wankun
 * @create 2019/5/10
 * @since 1.0.0
 */
public class NetMethodManager {
    private static UserApi mUserApi;
    private static TngouApi mTngouApi;
    private static GankApi mGankApi;
    private static OkHttpClient.Builder httpClientBuilder;

    private static List<OkHttpClient> clients = new ArrayList<>();

    private NetMethodManager() {
    }


    @NonNull
    private static Retrofit getRetrofit(String url) {
        if (httpClientBuilder == null) {
            initOkHttp();
        }

        //将生成的okHttpClient 放入list中，统一管理
        OkHttpClient okHttpClient = httpClientBuilder.build();
        clients.add(okHttpClient);

        //获取base_url
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    /**
     * 初始化 OkHttp
     */
    public static void initOkHttp() {
        httpClientBuilder = new OkHttpClient.Builder();
        //设置网络链接对象
        httpClientBuilder
                .addInterceptor(getHeaderInterceptor())
                .retryOnConnectionFailure(true)
                .connectTimeout(Constant.TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(Constant.READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(Constant.WRITE_TIME_OUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, 15, TimeUnit.SECONDS));
        if (ConfigDev.IS_DEV) {
            //设置打印日志的拦截
            httpClientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }

        //添加网络不稳定时程序崩溃问题
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Logger.e("》》》》Error:", throwable.getLocalizedMessage());
            }
        });

        //设置返回数据预处理，网络请求错误预处理
        initResponsePretreatment();
    }


    /**
     * 添加全局参数   和设置请求头信息
     *
     * @return
     */
    private static Interceptor getHeaderInterceptor() {
        //设置请求头信息
        return chain -> {
            Request originalRequest = chain.request();
            Request.Builder requestBuilder = originalRequest.newBuilder();
            //添加公共头请求头信息
            requestBuilder.method(originalRequest.method(), originalRequest.body());
            requestBuilder.addHeader("Accept", "application/json");
            requestBuilder.addHeader("Content-Type", "application/json; charset=utf-8");
            //添加公共的请求参数
            HttpUrl originalHttpUrl = originalRequest.url();
            HttpUrl.Builder httpUrlBuilder = originalHttpUrl.newBuilder();
            httpUrlBuilder.addQueryParameter("from_type", "android");
            httpUrlBuilder.addQueryParameter("app_version", PackageInfoUtil.getVersion(DemoApplication.getAppContext()));
            HttpUrl url = httpUrlBuilder.build();
            Request request = requestBuilder.url(url).build();
            return chain.proceed(request);
        };

    }


    /**
     * 指定运行的线程
     * subscribeOn(): 指定 subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程。或者叫做事件产生的线程。
     * observeOn(): 指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。
     * subscribeOn(Schedulers.io())  指定 subscribe() 发生在 IO 线程,内部实现是是用一个无数量上限的线程池，可以重用空闲的线程
     * 不要把计算工作放在 io() 中，可以避免创建不必要的线程。
     * observeOn(AndroidSchedulers.mainThread())   指定 Subscriber 的回调发生在主线程
     */
    /**
     * 不在ui线程中接受返回结果
     */
    public static final ObservableTransformer IO_NOT_UI = upstream ->
            upstream.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io());

    /**
     * 在ui线程中接受返回结果
     */
    public static final ObservableTransformer IO_UI = upstream ->
            upstream.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());


    /**
     * 获取 UserApi
     *
     * @return
     */
    public static UserApi getUserApi() {
        if (mUserApi == null) {
            mUserApi = getRetrofit(ConfigDev.BASE_URL).create(UserApi.class);
        }
        return mUserApi;
    }

    /**
     * 获取 TngouApi
     *
     * @return
     */
    public static TngouApi getTngouApi() {
        if (mTngouApi == null) {
            mTngouApi = getRetrofit(ConfigDev.URL_TNGOU).create(TngouApi.class);
        }
        return mTngouApi;
    }


    /**
     * 获取 GankApi
     *
     * @return
     */
    public static GankApi getGankApi() {
        if (mGankApi == null) {
            mGankApi = getRetrofit(ConfigDev.URL_GANK).create(GankApi.class);
        }
        return mGankApi;
    }


    /**
     * 关闭所有网络请求
     */
    public static void cancelAll() {
        if (clients != null && clients.size() > 0) {
            for (OkHttpClient clients : clients) {
                if (clients != null) {
                    clients.dispatcher().cancelAll();
                }
            }
        }
    }


    /**
     * 设置返回数据预处理，网络请求错误预处理
     */
    public static void initResponsePretreatment() {
        RxJavaPlugins.setOnObservableSubscribe(new BiFunction<Observable, Observer, Observer>() {
            @Override
            public Observer apply(Observable observable, Observer observer) throws Exception {
                return new ObservableSubscribeHooker(observer);
            }
        });
    }
}