/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.application;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.github.moduth.blockcanary.BlockCanary;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.squareup.leakcanary.LeakCanary;
import com.wankun.demo.common.AppBlockCanaryContext;
import com.wankun.demo.common.ConfigDev;
import com.wankun.demo.utils.SharedPreferencesUtil;

/**
 * 〈application 项目实例〉
 *
 * @author wankun
 * @create 2019/5/8
 * @since 1.0.0
 */
public class DemoApplication extends MultiDexApplication {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化内存监听
        setupLeakCanary();
    }


    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        //初始化分包功能
        MultiDex.install(context);
        mContext = context;
        //初始化日志打印工具
        initLogger();
        // 注册App异常崩溃处理器
        AppCrashHandler.getInstance().init(mContext);
        //初始化SharedPreferencesUtil工具类
        SharedPreferencesUtil.initInstance(mContext);
        //启动本地服务
        startService(new Intent(mContext, MainService.class));

    }


    /**
     * 获取实例上下文
     */
    public static Context getAppContext() {
        return mContext;
    }

    /**
     * 初始化日志打印功能
     */
    private void initLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .tag("LOGGER_WANKUN")
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return ConfigDev.IS_DEV;
            }
        });
    }

    /**
     * 初始 内存溢出监听
     */
    private void setupLeakCanary() {
        if (ConfigDev.IS_DEV) {
            //内存溢出监听功能
            if (LeakCanary.isInAnalyzerProcess(DemoApplication.this)) {
                return;
            }
            LeakCanary.install(DemoApplication.this);
            //界面卡顿监听工具
            BlockCanary.install(this, new AppBlockCanaryContext()).start();
        }
    }

}