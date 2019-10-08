/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.common;

/**
 * 〈常用常量存储类〉
 *
 * @author wankun
 * @create 2019/5/9
 * @since 1.0.0
 */
public class Constant {
    //===========系统常用配置=============

    /**
     * 最大的磁盘缓存大小---50M
     */
    public static final int MAX_DISK_CACHE = 50 * 1024 * 1024;
    /**
     * 最大的图片缓存大小---20M
     */
    public static final int MAX_IAMGE_CACHE = 20 * 1024 * 1024;
    /**
     * 普通请求超时时间
     */
    public static final int TIME_OUT = 30 * 1000;

    /**
     * 普通请求超时时间
     */
    public static final int READ_TIME_OUT = 15 * 1000;

    /**
     * 普通请求超时时间
     */
    public static final int WRITE_TIME_OUT = 15 * 1000;


    //===========网络请求错误码=============
    /**
     * 网络错误码，默认
     */
    public static final int DEFAULT_ERROR_CODE = -1;
    /**
     * 网络错误码，网络异常和加载超时
     */
    public static final int SERVICE_ERROR_CODE = -2;
    /**
     * 网络错误码，加载超时
     */
    public static final int EVENT_REQUEST_TIMEOUT = -3;
    /**
     * 网络错误码  网络请求失败
     */
    public static final int EVENT_REQUEST_FAILED = -4;
    /**
     * 网络错误码  json解析失败
     */
    public static final int EVENT_JSON_EXCEPTION_ERROR = -5;
    /**
     * 网络错误码  未知错误
     */
    public static final int EVENT_UNKNOWN_ERROR = -6;



    //===========本地文件存储=============

    /**
     * SD卡默认50M
     */
    public static final int SDCARD_MEMORY = 50;
    /**
     * SD卡是否可以存储
     */
    public static boolean SDCARD_CAN_SAVE = false;
    /**
     * 存储文件根目录
     */
    public static String FILE_ROOT_DIRECTORY = "";
    /**
     * 日志目录
     */
    public static String LOG_DIRECTORY = "";
    /**
     * 异常目录
     */
    public static String EXCEPTION_DIRECTORY = "";
    /**
     * 缓存目录
     */
    public static String CACHE_DIRECTORY = "";
    /**
     * 图片目录
     */
    public static String IMAGE_DIRECTORY = "";
    /**
     * 安装包目录
     */
    public static String INSTALL_DIRECTORY = "";
    /**
     * 上传目录
     */
    public static String UPLOAD_DIRECTORY = "";

    /**
     * 语音目录
     */
    public static String VOICE_DIRECTORY = "";


    public static final String IMAGES_FILE_NAME = "wankun";


    //===========默认格式=============

    /**
     * 时间格式1
     */
    public static final String DATE_FORMAT1 = "yyyy-MM-dd HH:mm:ss";
    /**
     * 时间格式2
     */
    public static final String DATE_FORMAT2 = "yyyy-MM-dd HHmmss";
    /**
     * 时间格式3
     */
    public static final String DATE_FORMAT3 = "yyyy-MM-dd";
    /**
     * 时间格式4
     */
    public static final String DATE_FORMAT4 = "yyyy-MM-dd HH:mm";

    /**
     * 默认Double值--0.0
     */
    public static final Double DEFAULT_DOUBLE_ZERO = 0.0;
    /**
     * 默认int值(1)
     */
    public static final int DEFAULT_INT_ONE = 1;
    /**
     * 默认int值(0)
     */
    public static final int DEFAULT_INT_ZERO = 0;
    /**
     * 默认int值(-1)
     */
    public static final int DEFAULT_INT_MINUS = -1;
    /**
     * 默认String值("")
     */
    public static final String DEFAULT_STRING = "";
    /**
     * 默认String值("-")
     */
    public static final String DEFAULT_EMPTY_DATA_STRING = "-";
    /**
     * 默认String值("-")
     */
    public static final String DEFAULT_DOUBLE_STRING = "0.00";

}