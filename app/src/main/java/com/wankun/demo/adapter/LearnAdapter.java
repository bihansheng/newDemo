/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.wankun.demo.R;
import com.wankun.demo.base.MyBaseAdapter;
import com.wankun.demo.model.httpResponse.GankDataEntity;

import org.byteam.superadapter.internal.SuperViewHolder;

import java.util.List;
import java.util.Random;


/**
 * 〈干货数据列表adapter〉
 *
 * @author wankun
 * @create 2019/8/7
 * @since 1.0.0
 */
public class LearnAdapter extends MyBaseAdapter<GankDataEntity> {

    public LearnAdapter(Context context, List<GankDataEntity> items, int layoutResId) {
        super(context, items, layoutResId);
    }

    @Override
    public void onBind(SuperViewHolder holder, int viewType, int position, GankDataEntity data) {
        super.onBind(holder, viewType, position, data);
        holder.setText(R.id.item_learn_title, data.getDesc().trim());

        String who = data.getWho();
        if (TextUtils.isEmpty(who)) {
            who = mContext.getString(R.string.default_who);
        }

        TextView whoTv = holder.getView(R.id.item_learn_tho);
        whoTv.setText(who.trim());
        whoTv.setTextColor(mContext.getResources().getColor(getColors()));

        holder.setText(R.id.item_learn_date, " - " + data.getCreatedAt().substring(0, 10).trim());
    }



    public static int[] mTextColors = {R.color.tomato, R.color.wheat, R.color.lightcoral, R.color.violet, R.color.gainsboro, R.color
            .goldenrod, R.color.lightblue, R.color.darkorchid, R.color.darkseagreen, R.color.slateblue};

    public static int getColors() {
        return mTextColors[new Random().nextInt(6)];
    }


}