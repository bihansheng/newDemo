/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.common;

/**
 * 〈开发阶段相关配置〉
 *
 * @author wankun
 * @create 2019/5/10
 * @since 1.0.0
 */
public class ConfigDev {
    /**
     * 是否打印日志
     */
    public static boolean IS_DEV = true;

    /**
     * 服务器地址
     */
    public static String BASE_URL = "http://172.23.1.163:8099";

    /**
     * 360图片 请求地址
     */
    public static final String URL_TNGOU = "http://image.so.com/";

    /**
     * 干货 请求地址
     */
    public static final String URL_GANK = "http://gank.io/api/";

    /**
     * 上传照片上限
     */
    public static final int   IMAGE_UPPER_LIMIT     = 5;


    /**
     * 心跳时间
     */
    public static final int HEART_BEAT_TIME =3 * 60 * 1000 ;


    /**
     * 首页网页地址
     */
    public static final String HOME_PAGE_URL ="https://blog.csdn.net/bihansheng2010";

    /**
     * Gank.io API 接口的类型
     */
    public static final String GANK_TYPE_ANDROID = "Android";
    public static final String GANK_TYPE_IOS = "iOS";
    public static final String GANK_TYPE_WEB = "前端";

}