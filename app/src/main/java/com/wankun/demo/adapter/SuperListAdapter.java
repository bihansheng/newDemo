package com.wankun.demo.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.wankun.demo.R;
import com.wankun.demo.base.MyBaseAdapter;
import com.wankun.demo.model.httpResponse.SuperGalleryResponse;
import com.wankun.demo.utils.ImgUtils;

import org.byteam.superadapter.internal.SuperViewHolder;

import java.util.List;

/**
 * @author wankun
 */
public class SuperListAdapter extends MyBaseAdapter<SuperGalleryResponse.ListBean> {

    private Context context;

    public SuperListAdapter(Context context, List<SuperGalleryResponse.ListBean> items, int layoutResId) {
        super( context, items, layoutResId );
        this.context = context;
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int position, SuperGalleryResponse.ListBean item) {
        super.onBind( holder, viewType, position, item );
        ImgUtils.load( context, item.getQhimg_thumb_url(), (ImageView) holder.getView( R.id.item_image_v ), item.getQhimg_width(), item.getQhimg_height() );
    }

}
