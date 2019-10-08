/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.application;

import android.content.Context;
import android.os.SystemClock;

import com.orhanobut.logger.Logger;
import com.wankun.demo.common.ConfigDev;
import com.wankun.demo.common.Constant;
import com.wankun.demo.utils.DateUtil;
import com.wankun.demo.utils.FileUtil;
import com.wankun.demo.utils.LocalThreadPools;
import com.wankun.demo.utils.PackageInfoUtil;
import com.wankun.demo.utils.ToastManager;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * 〈app崩溃日志捕获和处理〉
 *
 * @author wankun
 * @create 2019/5/9
 * @since 1.0.0
 */
public class AppCrashHandler implements UncaughtExceptionHandler {
    /**
     * CrashHandler实例
     */
    private static AppCrashHandler instance;

    /**
     * 程序的Context对象
     */
    private Context mContext;

    /**
     * 系统默认的UncaughtException处理类
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;


    /**
     * 保证只有一个CrashHandler实例
     */
    private AppCrashHandler() {
    }

    /**
     * 获取CrashHandler实例 ,单例模式
     */
    public static AppCrashHandler getInstance() {
        if (instance == null) {
            instance = new AppCrashHandler();
        }
        return instance;
    }

    /**
     * 初始化
     */
    public void init(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Logger.e("程序崩溃！", ex.getMessage());
        if(ConfigDev.IS_DEV){
            mDefaultHandler.uncaughtException(thread, ex);
        }else{
            if (!handleException(ex) && mDefaultHandler != null) {
                //如果用户没有处理则让系统默认的异常处理器来处理
                mDefaultHandler.uncaughtException(thread, ex);
            } else {
                SystemClock.sleep(3000);
                //退出程序
                AppActivityManager.getInstance().appExit(true);
            }
        }
    }


    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //保存日志文件
        saveCrashInfoToFile(ex);
        //使用Toast来显示异常信息
        LocalThreadPools.getInstance().execute(() -> ToastManager.showToastOnMain("很抱歉,程序出现异常,即将退出"));
        return true;
    }


    /**
     * 生成崩溃日志
     *
     * @param ex 异常
     */
    private void saveCrashInfoToFile(Throwable ex) {
        try {
            if (Constant.SDCARD_CAN_SAVE) {
                StringWriter errors = new StringWriter();
                ex.printStackTrace(new PrintWriter(errors));
                String fileName = String.format("%sCrashInfo%s.txt", Constant.LOG_DIRECTORY, DateUtil.getCurrentDate(Constant.DATE_FORMAT2));
                //在报错日志的基础上添加版本信息和时间
                String content = String.format("=======\nversionCode=%s    versionName=%s\n CrashTime ：%s \n=======\n%s", PackageInfoUtil.getVersion(DemoApplication.getAppContext()), PackageInfoUtil.getCode(DemoApplication.getAppContext()), DateUtil.getCurrentDate(), errors.toString());
                FileUtil.writeContentToSDCard(fileName, content, true);
            } else {
                Logger.e("文件夹不存在");
            }
        } catch (Exception e) {
            Logger.e("写错误日志失败！");
        }
    }




}