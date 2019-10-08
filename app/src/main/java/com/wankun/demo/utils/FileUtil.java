/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;
import com.wankun.demo.application.DemoApplication;
import com.wankun.demo.common.Constant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 〈文件操作处理方法〉
 *
 * @author wankun
 * @create 2019/5/9
 * @since 1.0.0
 */
public class FileUtil {

    /**
     * 复制文件（非目录）
     *
     * @param srcFile  要复制的源文件
     * @param destFile 复制到的目标文件
     * @return
     */
    private static boolean copyFile(String srcFile, String destFile) {
        try {
            InputStream streamFrom = new FileInputStream(srcFile);
            OutputStream streamTo = new FileOutputStream(destFile);
            byte buffer[] = new byte[1024];
            int len;
            while ((len = streamFrom.read(buffer)) > 0) {
                streamTo.write(buffer, 0, len);
            }
            streamFrom.close();
            streamTo.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 移动文件目录到某一路径下
     *
     * @param srcFile
     * @param destDir
     * @return
     */
    public static boolean moveFile(String srcFile, String destDir) {
        //复制后删除原目录
        if (copyFile(srcFile, destDir)) {
            deleteFile(new File(srcFile));
            return true;
        }
        return false;
    }


    /**
     * 删除文件（包括目录）
     *
     * @param delFile
     */
    public static void deleteFile(File delFile) {
        //如果是目录递归删除
        if (delFile.isDirectory()) {
            File[] files = delFile.listFiles();
            for (File file : files) {
                deleteFile(file);
            }
        } else {
            delFile.delete();
        }
        // 如果不执行下面这句，目录下所有文件都删除了，但是还剩下子目录空文件夹
        delFile.delete();
    }


    /**
     * 递归删除文件
     *
     * @param filePath 文件路径
     */
    public static void deleteAllFile(String filePath) {
        try {
            if (!TextUtils.isEmpty(filePath)) {
                File file = new File(filePath);
                if (!file.exists()) {
                    return;
                }
                if (file.isFile()) {
                    file.delete();
                    return;
                }
                if (file.isDirectory()) {
                    File[] childFile = file.listFiles();
                    if (childFile == null || childFile.length == 0) {
                        return;
                    }
                    for (File tempFile : childFile) {
                        deleteAllFile(tempFile.getPath());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算SDCARD的剩余空间
     *
     * @return 返回空间大小
     */
    @SuppressWarnings("deprecation")
    public static long checkFreeDiskSpace() {
        long freeSpace = 0L;
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
                if (null != statFs) {
                    long blockSize = statFs.getBlockSize();
                    long availableBlocks = statFs.getAvailableBlocks();
                    freeSpace = availableBlocks * blockSize / 1024 / 1024;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return freeSpace;
    }

    /**
     * 创建文件夹目录
     *
     * @param directory 文件夹路径
     */
    public static void createDirectory(String directory) {
        try {
            if (!TextUtils.isEmpty(directory)) {
                File file = new File(directory);
                if (!file.exists()) {
                    file.mkdirs();
                }
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
        }
    }

    /**
     * 往SDCARD文件里面写内容
     *
     * @param fileName 文件名称
     * @param content  内容
     * @param append   true追加 false覆盖
     */
    public static void writeContentToSDCard(String fileName, String content, boolean append) {
        try {
            if (!TextUtils.isEmpty(fileName) && !TextUtils.isEmpty(content)) {
                File file = new File(fileName);
                if (!file.exists()) {
                    //创建本地文件夹
                    if (PermissionsChecker.lacksPermission(Manifest.permission.READ_EXTERNAL_STORAGE, DemoApplication.getAppContext())) {
                        file.createNewFile();
                    }
                }
                FileOutputStream fos = new FileOutputStream(file, append);
                fos.write(content.getBytes("UTF-8"));
                fos.flush();
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取SDCARD文件的内容
     *
     * @param fileName 文件路径
     * @return 返回内容
     */
    public static String readFileInSDCard(String fileName) {
        try {
            if (!TextUtils.isEmpty(fileName)) {
                File file = new File(fileName);
                if (file.exists()) {
                    FileInputStream fis = new FileInputStream(file);
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[fis.available()];
                    int length = -1;
                    while ((length = fis.read(buffer)) != -1) {
                        outStream.write(buffer, 0, length);
                    }
                    outStream.close();
                    fis.close();
                    return outStream.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 创建文件目录
     *
     * @param context Context
     */
    public static void createDirectory(Context context) {
        try {
            long space = checkFreeDiskSpace();
            // 判断SD卡空间若大于50Mb，则视为可以存储
            Constant.SDCARD_CAN_SAVE = space > Constant.SDCARD_MEMORY;
            if (Constant.SDCARD_CAN_SAVE) {
                // 配置文件目录放置在SD卡根目录
                Constant.FILE_ROOT_DIRECTORY = Environment.getExternalStorageDirectory().getPath() + File.separator + context.getPackageName();
            } else {
                // 内存中创建目录
                Constant.FILE_ROOT_DIRECTORY = context.getFilesDir().getPath();
            }
            // 日志
            Constant.LOG_DIRECTORY = Constant.FILE_ROOT_DIRECTORY + "/log/";
            createDirectory(Constant.LOG_DIRECTORY);
            // 异常
            Constant.EXCEPTION_DIRECTORY = Constant.FILE_ROOT_DIRECTORY + "/exception/";
            createDirectory(Constant.EXCEPTION_DIRECTORY);
            // 缓存
            Constant.CACHE_DIRECTORY = Constant.FILE_ROOT_DIRECTORY + "/cache/";
            createDirectory(Constant.CACHE_DIRECTORY);
            // 图片
            Constant.IMAGE_DIRECTORY = Constant.FILE_ROOT_DIRECTORY + "/image/";
            createDirectory(Constant.IMAGE_DIRECTORY);
            // 安装包
            Constant.INSTALL_DIRECTORY = Constant.FILE_ROOT_DIRECTORY + "/apk/";
            createDirectory(Constant.INSTALL_DIRECTORY);
            // 上传
            Constant.UPLOAD_DIRECTORY = Constant.FILE_ROOT_DIRECTORY + "/upload/";
            createDirectory(Constant.UPLOAD_DIRECTORY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 保存方法
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static String saveBitmap(Bitmap bm, String name) throws IOException {
        Logger.e("保存图片");
        String FileName = String.format("%s%s", name, ".jpeg");
        String FilePath = Constant.IMAGE_DIRECTORY + "QRCode";

        File file = new File(FilePath, FileName);

        if (file.exists()) {
            file.delete();
        } else {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        if (bm.compress(Bitmap.CompressFormat.PNG, 100, fos)) {
            fos.flush();
            fos.close();
            Logger.e("已经保存   -- 》》 " + file.getAbsolutePath());
            return file.getAbsolutePath();
        }
        return "";
    }

    /**
     * 保存图片
     * @param context
     * @param bmp
     * @param name
     * @return
     */
    public static String saveImageToGallery(Context context, Bitmap bmp, String name) {
        // 首先保存图片
        if (null == bmp || null == context) {
            return "";
        }
        if (TextUtils.isEmpty(name)) {
            name = String.valueOf(System.currentTimeMillis());
        }

        try {
            File appDir = Environment.getExternalStorageDirectory();
            if (appDir == null) {
                return "";
            }
            if (!appDir.exists()) {
                boolean mkdir = appDir.mkdir();
                if (!mkdir) {
                    return "";
                }
            }
            String fileName = name + ".jpeg";
            File file = new File(appDir, fileName);

            if (file.exists()) {
                boolean delete = file.delete();
            } else {
                if (!file.createNewFile()) {
                    return "";
                }
            }
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            // 其次把文件插入到系统图库
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
            //                         最后通知图库更新
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" +
                    Environment.getExternalStorageDirectory())));
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).setData(Uri.fromFile(file)));
            //这个广播的目的就是更新图库
            Environment.getExternalStorageDirectory();
            return file.getAbsolutePath();
        } catch (Exception e) {
            return "";
        }
    }



}