/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.pictureSelector;

import android.Manifest;
import android.app.Activity;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.wankun.demo.R;
import com.wankun.demo.utils.ToastManager;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 〈Glide 图片加载工具相关功能〉
 *
 * @author wankun
 * @create 2019/5/13
 * @since 1.0.0
 */
public class GlideManger {


//
//    /**
//     * 图片添加处理逻辑
//     * 设置全局的ImagePicker参数，
//     * 注意，如果只是在特定的地方需要修改部分参数，完成选项后 必须重新改回全局的设置
//     */
//    public static void imagePicker(Activity activity) {
//        ImagePicker imagePicker = ImagePicker.getInstance();
//        //设置图片加载器
//        imagePicker.setImageLoader(new GlideImageLoader());
//        //显示拍照按钮
//        imagePicker.setShowCamera(true);
//        //允许裁剪（单选才有效）
//        imagePicker.setCrop(false);
//        //是否按矩形区域保存
//        imagePicker.setSaveRectangle(true);
//        //选中数量限制
//        imagePicker.setSelectLimit(ConfigDev.IMAGE_UPPER_LIMIT);
//        //裁剪框的形状
//        imagePicker.setStyle(CropImageView.Style.RECTANGLE);
//        //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setFocusWidth(800);
//        //裁剪框的高度。单位像素（圆形自动取宽高最小值）
//        imagePicker.setFocusHeight(400);
//        //保存文件的宽度。单位像素
//        imagePicker.setOutPutX(1000);
//        //保存文件的高度。单位像素
//        imagePicker.setOutPutY(1000);
//        Intent intent = new Intent(activity, ImageGridActivity.class);
//        activity.startActivityForResult(intent, KeyName.IMAGE_PICKER);
//    }


    /**
     * 图片添加处理逻辑
     */
    public static void imagePicker(Activity activity) {

        // 进入相册 以下是例子：用不到的api可以不写
        PictureSelector.create(activity)
                //全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .openGallery(PictureMimeType.ofImage())
                //主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                .theme(R.style.pictureDefaultStyle)
                // 最大图片选择数量 int
                .maxSelectNum(9)
                // 最小选择数量 int
                .minSelectNum(1)
                // 每行显示个数 int
                .imageSpanCount(4)
                // 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                .selectionMode(PictureConfig.MULTIPLE)
//                // 是否可预览图片 true or false
//                .previewImage(true)
//                // 是否可预览视频 true or false
//                .previewVideo(true)
//                // 是否可播放音频 true or false
//                .enablePreviewAudio(true)
//                // 是否显示拍照按钮 true or false
//                .isCamera(true)
//                // 拍照保存图片格式后缀,默认jpeg
//                .imageFormat(PictureMimeType.PNG)
//                // 图片列表点击 缩放效果 默认true
//                .isZoomAnim(true)
//                // glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
//                .sizeMultiplier(0.5f)
//                // 自定义拍照保存路径,可不填
//                .setOutputCameraPath(Constant.IMAGE_DIRECTORY)
//                // 是否裁剪 true or false
//                .enableCrop(false)
//                // 是否压缩 true or false
//                .compress(false)
//                // int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                .glideOverride(100, 100)
//                // int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                .withAspectRatio(16, 9)
//                // 是否显示uCrop工具栏，默认不显示 true or false
//                .hideBottomControls(false)
//                // 是否显示gif图片 true or false
//                .isGif(true)
//                // 是否开启点击声音 true or false
//                .openClickSound(false)
//                // 是否传入已选图片 List<LocalMedia> list
//                .selectionMedia(new ArrayList<>())
//                // 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
//                .previewEggs(true)
//                //压缩图片保存地址
//                .compressSavePath(Constant.IMAGE_DIRECTORY)
//                // 裁剪框是否可拖拽 true or false
//                .freeStyleCropEnabled(true)
//                // 是否圆形裁剪 true or false
//                .circleDimmedLayer(false)
//                // 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
//                .showCropFrame(false)
//                // 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
//                .showCropGrid(true)
//                // 裁剪压缩质量 默认90 int
//                .cropCompressQuality(90)
//                // 小于100kb的图片不压缩
//                .minimumCompressSize(100)
//                //同步true或异步false 压缩 默认同步
//                .synOrAsy(true)
//                // 裁剪宽高比，设置如果大于图片本身宽高则无效 int
//                .cropWH(1, 1)
//                // 裁剪是否可旋转图片 true or false
//                .rotateEnabled(true)
//                // 裁剪是否可放大缩小图片 true or false
//                .scaleEnabled(true)
//                // 视频录制质量 0 or 1 int
//                .videoQuality(1)
//                // 显示多少秒以内的视频or音频也可适用 int
//                .videoMaxSecond(15)
//                // 显示多少秒以内的视频or音频也可适用 int
//                .videoMinSecond(10)
//                //视频秒数录制 默认60s int
//                .recordVideoSecond(60)
//                // 是否可拖动裁剪框(固定)
//                .isDragFrame(false)
                //结果回调onActivityResult code
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }


    /**
     * 清除选择图片是 产生的缓存
     * 包括裁剪和压缩后的缓存，要在上传成功后调用，注意：需要系统sd卡权限
     */
   static public void deleteCacheDirFile(Activity activity) {

        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        RxPermissions permissions = new RxPermissions(activity);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    PictureFileUtils.deleteCacheDirFile(activity);
                } else {
                    ToastManager.showToast(R.string.picture_jurisdiction);
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });


    }


}