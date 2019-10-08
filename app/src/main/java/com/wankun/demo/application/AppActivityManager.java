/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.application;

import android.app.Activity;
import android.content.Intent;

import com.wankun.demo.net.NetMethodManager;

import java.util.Stack;

/**
 * 〈app中activity的管理类〉
 *
 * @author wankun
 * @create 2019/5/9
 * @since 1.0.0
 */
public class AppActivityManager {
    private static Stack<Activity> activityStack;
    private static AppActivityManager instance;

    private AppActivityManager() {
        if (null == activityStack) {
            activityStack = new Stack<>();
        }
    }

    public synchronized static AppActivityManager getInstance() {
        if (instance == null) {
            instance = new AppActivityManager();
        }
        return instance;
    }

    /**
     * 添加Activity
     *
     * @param activity Activity
     */
    public void addActivity(Activity activity) {
        activityStack.add(activity);
    }

    /**
     * 判断APP是否正在运行
     *
     * @return
     */
    public boolean isAppRunning() {
        if (null == activityStack || activityStack.size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * 判断APP是否只有一个Activity
     *
     * @return
     */
    public boolean isOneActivity() {
        if (null != activityStack && activityStack.size() == 1) {
            return true;
        }
        return false;
    }

    /**
     * 获取当前Activity
     *
     * @return Activity
     */
    public Activity currentActivity() {
        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity
     */
    public void finishActivity() {
        finishActivity(activityStack.lastElement());
    }

    /**
     * 结束指定的Activity
     *
     * @param activity Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 结束指定类的Activity
     *
     * @param cls 类
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有的Activity
     */
    private void finishAllActivity() {
        if (activityStack != null && activityStack.size() > 0) {
            int size = activityStack.size();
            for (int i = 0; i < size; i++) {
                Activity activity = activityStack.get(i);
                if (null != activity && !activity.isFinishing()) {
                    activity.finish();
                }
            }
            activityStack.clear();
        }
    }


    /**
     * 保留指定的activity
     *
     * @param cls
     */
    public void finishAllButMainActivity(Class<?> cls) {
        if (activityStack != null && activityStack.size() > 0) {
            Stack<Activity> activities = new Stack<>();
            for (Activity activity : activityStack) {
                if (!activity.getClass().equals(cls)) {
                    activities.add(activity);
                    if (null != activity && !activity.isFinishing()) {
                        activity.finish();
                    }
                }
            }
            activityStack.remove(activities);
        }
    }

    /**
     * 是否完全退出应用程序
     *
     * @param isExitApp 是true，否false
     */
    public void appExit(boolean isExitApp) {
        try {
            // 停止网络请求
            NetMethodManager.cancelAll();
//            //停止百度定位
//            BDLocationService bdLocationService = BDLocationService.getInstance();
//            if (bdLocationService.isStart()) {
//                bdLocationService.destroyLocationManager();
//            }
            //关闭主服务
            Intent serviceIntent = new Intent(DemoApplication.getAppContext(), MainService.class);
            DemoApplication.getAppContext().stopService(serviceIntent);

            //TODO 停止相关的服务

            // 关闭所有Activity
            finishAllActivity();
            if (isExitApp) {
                // 杀死该应用进程
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}