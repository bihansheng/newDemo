/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.application;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.wankun.demo.common.ConfigDev;

/**
 * 〈程序主服务，管理心跳和大部分配置数据获取〉
 *
 * @author wankun
 * @create 2019/5/10
 * @since 1.0.0
 */
public class MainService extends Service {


    private Runnable mRunnable;
    private Handler mHandler;
    //文件上传是否成功
    private boolean isUpLoadCompleted;

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    /**
     * 初始化数据
     */
    private void init() {
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                //判断是否有日志，如果有，上传日志
//                uploadCrashInfo();
//                heabat();//心跳
                Logger.e(">>>>>> 服务");
                //设置心跳时间
                mHandler.postDelayed(this, ConfigDev.HEART_BEAT_TIME);
            }
        };
        mHandler.post(mRunnable);
    }
//
//
//    /**
//     * 心跳接口  这里如果没有登录也会执行心跳，用于后台做数据统计
//     */
//    private void heabat() {
//        if (CheckNet.checkNetwork(YQWApplication.getApplication())) {
//            NetMethodManager.getInstance()
//                    .getNetService()
//                    .heabat(UserManager.getUserId(), UserManager.getTicket())
//                    .map(new NetResultFunc<>())
//                    .subscribeOn(Schedulers.io())
//                    .unsubscribeOn(Schedulers.io())
//                    .observeOn(Schedulers.io())
//                    .subscribe(new Subscriber<String>() {
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            Logger.e(e == null ? "心跳失败" : "心跳失败" + e.getMessage());
//
//                        }
//
//                        @Override
//                        public void onNext(String s) {
//                            Logger.e("心跳");
//
//                        }
//                    });
//        }
//    }
//
//
//

//    /**
//     * 上传崩溃日志
//     */
//    private void uploadCrashInfo() {
//        if (CheckNet.checkNetwork(ConfigService.this) && !(isUpLoadCompleted)) {
//            //如果文件夹不存在
//            if (TextUtils.isEmpty(Constant.LOG_DIRECTORY)) {
//                isUpLoadCompleted = true;
//                return;
//            }
//            File file = new File(Constant.LOG_DIRECTORY);
//            if (file.exists()) {
//                File[] files = file.listFiles();
//                if (files != null && files.length > 0) {//如果有文件，上传文件
//                    if (files.length > 50) {//如果超过50条，只取50条
//                        try {
//                            File[] temporary = new File[50];
//                            System.arraycopy(files, 0, temporary, 0, 50);
//                            files = temporary;
//                        } catch (Exception e) {
//                            Logger.e( "读取错误日志失败");
//                            isUpLoadCompleted = true;
//                            return;
//                        }
//                    }
//                    ActivityManager.uploadFiles(files, "1", new Subscriber<FilesList>() {
//                        @Override
//                        public void onCompleted() {
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            //                            errorNum++;
//                            Logger.e(Constant.TAG, e == null ? "上传日志失败" : "上传日志失败" + e.getMessage());
//                        }
//
//                        @Override
//                        public void onNext(FilesList baseResponse) {
//                            //清除已上传的文件
//                            isUpLoadCompleted = true;
//                            FileUtil.deleteAllFile(Constant.LOG_DIRECTORY);
//                        }
//                    });
//                } else {
//                    isUpLoadCompleted = true;
//                }
//            } else {
//                isUpLoadCompleted = true;
//            }
//        }
//    }


    @Override
    public void onDestroy() {
        stopTiming();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 停止定时器
     */
    private void stopTiming() {
        if (null != mHandler && null != mRunnable) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

}