package com.wankun.demo.pictureSelector.image;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.luck.picture.lib.photoview.PhotoView;
import com.wankun.demo.R;
import com.wankun.demo.common.Constant;
import com.wankun.demo.utils.FileUtil;
import com.wankun.demo.utils.ImgUtils;
import com.yalantis.ucrop.util.FileUtils;

import java.io.File;
import java.util.List;



/**
 * Wing_Li
 * 2016/4/15.
 */
public class ImageAdapter extends PagerAdapter {
    Context mContext;

    List<String> imgs;

    public ImageAdapter(Context context, List<String> imgs) {
        this.mContext = context;
        this.imgs = imgs;
    }

    @Override
    public int getCount() {
        return imgs == null ? 0 : imgs.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 需要移除该View，避免View重复加载。
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    /**
     * 加载View
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final String imgUrl = imgs.get(position);

        PhotoView photoView = new PhotoView(mContext);
        photoView.setAdjustViewBounds(true);
        ImgUtils.load(mContext, imgUrl, photoView);
        photoView.setOnLongClickListener(v -> {
            if (TextUtils.isEmpty(imgUrl)){
                Toast.makeText(mContext.getApplicationContext(), R.string.img_err,Toast.LENGTH_SHORT).show();
                return false;
            }
            new AlertDialog.Builder(mContext).setTitle(mContext.getString(R.string.image_save))
                    .setMessage(mContext.getString(R.string.image_save_msg))
                    .setNegativeButton(mContext.getString(R.string.save), (dialog, which) -> ImgUtils.downloadImg(mContext, imgUrl, new ImgUtils.DownloadImage() {
                        @Override
                        public void downloadImage(File imgFile) {
                            if (imgFile != null){
                                String path = Constant.IMAGE_DIRECTORY;
                                String imgName = "wankun_" + System.currentTimeMillis() + ".jpg";
                                File file = new File(path, imgName);

                                // 移动下载的图片到 目标路径
                                boolean moveFile = FileUtil.moveFile(imgFile.getAbsolutePath(), file.getAbsolutePath());

                                if (moveFile){
                                    mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                                    Toast.makeText(mContext.getApplicationContext(), R.string.save_success, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext.getApplicationContext(), R.string.save_fail, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(mContext.getApplicationContext(), R.string.save_fail, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }))//
                    .setPositiveButton(mContext.getString(R.string.alert_cancel), null).create().show();
            return true;
        });

        ((ViewPager) container).addView(photoView);
        return photoView;
    }

}
