/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;

import com.wankun.demo.common.Constant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 〈图片处理工具类〉
 *
 * @author wankun
 * @create 2019/5/13
 * @since 1.0.0
 */
public class PictureUtil {
    /**
     * 把bitmap转换成String
     *
     * @param filePath
     * @param ratio     压缩比例
     * @param reqWidth  最大宽高
     * @param reqHeight
     * @return
     */
    public static String bitmapToString(String filePath, int ratio, int reqWidth,
                                        int reqHeight) {
        Bitmap bm = getSmallBitmap(filePath, reqWidth, reqHeight);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, ratio, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);

    }

    public static byte[] bitmapToByte(String filePath) {
        Bitmap bm = getSmallBitmap(filePath, 1000, 1000);
        if (null == bm) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        bm = null;
        return baos.toByteArray();

    }


    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 根据路径获得图片并压缩返回bitmap用于显示
     *
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, int reqWidth,
                                        int reqHeight) {
        // 在通过BitmapFactory.decodeFile(String
        // path)方法将突破转成Bitmap时，遇到大一些的图片，我们经常会遇到OOM(Out Of Memory)的问题
        // 这就用到了我们上面提到的BitmapFactory.Options这个类。
        // BitmapFactory.Options这个类，有一个字段叫做 inJustDecodeBounds
        // 。SDK中对这个成员的说明是这样的：
        // If set to true, the decoder will return null (no bitmap), but the
        // out…
        // 也就是说，如果我们把它设为true，那么BitmapFactory.decodeFile(String path, Options
        // opt)并不会真的返回一个Bitmap给你，
        // 它仅仅会把它的宽，高取回来给你，这样就不会占用太多的内存，也就不会那么频繁的发生OOM了。

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // 这样虽然我们可以得到我们期望大小的ImageView
        // 但是在执行BitmapFactory.decodeFile(path,
        // options);时，并没有节约内存。要想节约内存，还需要用到BitmapFactory.Options这个类里的
        // inSampleSize 这个成员变量。
        // 我们可以根据图片实际的宽高和我们期望的宽高来计算得到这个值
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        //针对有的图片会被旋转处理图片
        int degree = readPictureDegree(filePath);
        Bitmap bit = BitmapFactory.decodeFile(filePath, options);
        return rotaingImageView(degree, bit);


    }

    /**
     * 根据路径删除图片
     *
     * @param path
     */
    public static void deleteTempFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 添加到图库
     */
    public static void galleryAddPic(Context context, String path) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * 获取保存图片的目录,存在了图片存放的标准目录。
     *
     * @return
     */
    public static File getAlbumDir() {
        File dir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                Constant.IMAGES_FILE_NAME);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }


    /**
     * 把程序拍摄的照片放到 SD卡的 Pictures目录中wankun 文件夹中
     * 照片的命名规则为：wankun_20130125_173729.jpg
     *
     * @return
     * @throws IOException
     */
    @SuppressLint("SimpleDateFormat")
    public static File createImageFile() throws IOException {

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timeStamp = format.format(new Date());
        String imageFileName = "zhankoo_" + timeStamp + ".jpg";

        File image = new File(getAlbumDir(), imageFileName);
        return image;
    }

    /**
     * 同过Uri获取文件真实路径
     *
     * @param contentUri
     * @return
     */
    public static String getRealPathFromURI(Uri contentUri, Context ct) {
        String[] proj = {MediaStore.Images.Media.DATA};

        Cursor cursor = ct.getContentResolver().query(contentUri, proj, null,
                null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }




    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        if (null == bitmap) {
            return null;
        }
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.setRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }


}