/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.manage;

import android.annotation.SuppressLint;

import com.luck.picture.lib.entity.LocalMedia;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.wankun.demo.model.httpResponse.UpdateVersion;
import com.wankun.demo.net.NetMethodManager;
import com.wankun.demo.net.NetResultFunc;
import com.wankun.demo.model.UserInfo;
import com.wankun.demo.model.httpResponse.UploadFiles;
import com.wankun.demo.net.api.UserApi;
import com.wankun.demo.utils.PictureUtil;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Observer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 〈通用的通过http数据加载类〉
 *
 * @author wankun
 * @create 2019/5/10
 * @since 1.0.0
 */
public class CommonNetManager {
    /**
     * 上传文件类型 图片
     */
    static final String UPLODE_IMAGE = "UPLODE_IMAGE";
    /**
     * 上传文件类型 普通文件
     */
    static final String UPLODE_FILE = "UPLODE_FILE";

    /**
     * 登录
     *
     * @RxAppCompatActivity 如果是咋activity 中执行网络请求，绑定RxAppCompatActivity 处理内存泄漏问题
     */
    @SuppressLint("CheckResult")
    static public void login(RxAppCompatActivity rxAppCompatActivity, String username, String password, Observer<UserInfo> observer) {
        NetMethodManager
                .getUserApi()
                .login(username, password)
                .map(new NetResultFunc<UserInfo>())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(NetMethodManager.IO_UI)
                .subscribeWith(observer);
    }


    /**
     * 上传多张图片 ImageItem对象
     * 默认不改变图片大小
     */
    @SuppressLint("CheckResult")
    static public void uploadImages(RxAppCompatActivity rxAppCompatActivity,
                                    ArrayList<LocalMedia> images,
                                    String userId,
                                    String authKey,
                                    Observer<UploadFiles> observer) {
        uploadImages(rxAppCompatActivity,
                images,
                false,
                userId,
                authKey,
                observer);
    }


    /**
     * 上传多张图片 ImageItem对象
     *
     * @param changedImage 是否改变图片，如果改变图片，则会按照默认的方法去压缩图片，再上传
     */
    @SuppressLint("CheckResult")
    static public void uploadImages(RxAppCompatActivity rxAppCompatActivity,
                                    ArrayList<LocalMedia> images,
                                    boolean changedImage,
                                    String userId,
                                    String authKey,
                                    Observer<UploadFiles> observer) {
        MultipartBody.Part[] parts = new MultipartBody.Part[images.size()];
        if (changedImage) {
            for (int i = 0; i < images.size(); i++) {
                byte[] b = PictureUtil.bitmapToByte(images.get(i).getPath());
                parts[i] = MultipartBody.Part.createFormData("file", images.get(i).getPath(), RequestBody.create(MediaType.parse("image/png"), b));
                b = null;
            }
        } else {
            for (int i = 0; i < images.size(); i++) {
                File file = new File(images.get(i).getPath());
                parts[i] = MultipartBody.Part.createFormData("file", i + "", RequestBody.create(MediaType.parse("image/png"), file));

            }
        }
        upload(rxAppCompatActivity, parts, userId, authKey, observer);
    }


    /**
     * 上传多个文件
     *
     * @type 上传文件类型，默认为文件类型
     */
    @SuppressLint("CheckResult")
    static public void uploadFiles(RxAppCompatActivity rxAppCompatActivity,
                                   ArrayList<File> files,
                                   String userId, String authKey,
                                   Observer<UploadFiles> observer) {
        MultipartBody.Part[] parts = new MultipartBody.Part[files.size()];
        for (int i = 0; i < files.size(); i++) {
            parts[i] = MultipartBody.Part.createFormData("file", files.get(i).getName(), RequestBody.create(MediaType.parse("text/plain"), files.get(i)));
        }
        upload(rxAppCompatActivity, parts, userId, authKey, observer);

    }

    /**
     * 执行文件上传
     *
     * @param rxAppCompatActivity
     * @param parts
     * @param userId
     * @param authKey
     * @param observer
     */
    @SuppressLint("CheckResult")
    static public void upload(RxAppCompatActivity rxAppCompatActivity,
                              MultipartBody.Part[] parts,
                              String userId, String authKey,
                              Observer<UploadFiles> observer) {
        NetMethodManager
                .getUserApi()
                .uploadFiles(RequestBody.create(null, userId), RequestBody.create(null, authKey), parts)
                .map(new NetResultFunc<>())
                .compose(rxAppCompatActivity.bindUntilEvent(ActivityEvent.DESTROY))
                .compose(NetMethodManager.IO_UI)
                .subscribeWith(observer);
    }


    /**
     * 检查最新版本
     */
    @SuppressLint("CheckResult")
    static public void getApkVersion(Observer<UpdateVersion> observer) {
        NetMethodManager.getUserApi().getApkVersion(UserApi.TYP_ANDROID)
                .compose(NetMethodManager.IO_UI)
                .subscribeWith(observer);
    }


}