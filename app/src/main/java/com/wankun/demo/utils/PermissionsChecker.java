/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * 〈权限判断相关方法，还需要优化〉
 *
 * @author wankun
 * @create 2019/5/9
 * @since 1.0.0
 */
public class PermissionsChecker {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * 判断是否缺少权限
     */
    static public boolean lacksPermission(String permission, Context mContext) {
        PackageManager pm = mContext.getPackageManager();
        return (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission(permission, mContext.getPackageName()));
    }

    /**
     * 判断是否需要权限
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    static public boolean lacksPermissionWrite(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        return permission != PackageManager.PERMISSION_GRANTED;
    }
}
