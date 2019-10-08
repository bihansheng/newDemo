/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.widget;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.roughike.bottombar.BottomBarTab;
import com.wankun.demo.R;
import com.wankun.demo.utils.MiscUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 〈底部菜单 单个按钮〉
 *
 * @author wankun
 * @create 2019/6/3
 * @since 1.0.0
 */
public class ButtonBarTab extends RelativeLayout {

    static final int IMAGE_SIZE_SELECTED = 28;
    static final int IMAGE_SIZE_NOT_SELECTED = 32;

    private static final long ANIMATION_DURATION = 250;
    private static final float ACTIVE_TITLE_SCALE = 1;
    private static final float INACTIVE_TITLE_SCALE = 0.86f;

    /**
     * menu的图片
     */
    @BindView(R.id.iv_image)
    ImageView ivImage;
    /**
     * menu的文字
     */
    @BindView(R.id.tv_name)
    TextView tvName;
    /**
     * 消息红点
     */
    @BindView(R.id.red_point)
    ImageView redPoint;


    private int imageSizeSelected;
    private int imageSizeUnselected;
    private int eightDps;
    /**
     * 是否已经被选择
     */
    boolean isActive;

    public ButtonBarTab(Context context) {
        super(context);
        init(context, null);
    }

    public ButtonBarTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }


    /**
     * 初始化View
     */
    private void init(Context context, AttributeSet attrs) {
        imageSizeSelected = MiscUtils.dpToPixel(context, IMAGE_SIZE_SELECTED);
        imageSizeUnselected = MiscUtils.dpToPixel(context, IMAGE_SIZE_NOT_SELECTED);
        eightDps = MiscUtils.dpToPixel(context, 6);


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.bottom_bar_menu, this, true);
        ButterKnife.bind(this);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ButtonBarTab);
        int nameRes = array.getResourceId(R.styleable.ButtonBarTab_menuText, -1);
        int imageRes = array.getResourceId(R.styleable.ButtonBarTab_menuImage, -1);
        if (nameRes != -1) {
            tvName.setText(nameRes);
        }
        if (imageRes != -1) {
            ivImage.setImageResource(imageRes);
        }
        array.recycle();
    }


    /**
     * 设置选中状态
     */
    @Override
    public void setSelected(boolean selected) {
//        tvName.setSelected(selected);
//        ivImage.setSelected(selected);
//        tvName.setTextSize(selected ? R.dimen.t16 : R.dimen.t18);
//        //改变图片大小
//        ivImage.setLayoutParams(selected ? new RelativeLayout.LayoutParams(imageSizeSelected, imageSizeSelected) : new RelativeLayout.LayoutParams(imageSizeUnselected, imageSizeUnselected));

        select();

    }

    /**
     * 设置选择和没选择的动画效果
     */

    public void select() {
        isActive = true;
        animateTitle(eightDps, ACTIVE_TITLE_SCALE, INACTIVE_TITLE_SCALE);
        animateIcon(INACTIVE_TITLE_SCALE, ACTIVE_TITLE_SCALE);
        animateColors(R.color.ash3, R.color.blue);
    }


    /**
     * 消息红点
     *
     * @param show
     */
    public void setRedPoint(boolean show) {
        redPoint.setVisibility(show ? VISIBLE : GONE);
    }


    /**
     * 设置标题动画
     *
     * @param padding 间距
     * @param scale   大小
     * @param alpha   不透明的
     */
    private void animateTitle(int padding, float scale, float alpha) {
        setTopPaddingAnimated(ivImage.getPaddingTop(), padding);
        ViewPropertyAnimatorCompat titleAnimator = ViewCompat.animate(tvName)
                .setDuration(ANIMATION_DURATION)
                .scaleX(scale)
                .scaleY(scale);
        titleAnimator.alpha(alpha);
        titleAnimator.start();
    }

    /**
     * 设置图片大小变化动画
     *
     * @param scale
     */
    private void animateIconScale(float scale) {
        ViewCompat.animate(ivImage)
                .setDuration(ANIMATION_DURATION)
                .scaleX(scale)
                .scaleY(scale)
                .start();
    }

    /**
     * 设置图片不透明的变化动画
     *
     * @param scale
     */
    private void animateIcon(float alpha, float scale) {
        ViewCompat.animate(ivImage)
                .setDuration(ANIMATION_DURATION)
                .alpha(alpha)
                .start();
    }


    private void setTopPaddingAnimated(int start, int end) {
        ValueAnimator paddingAnimator = ValueAnimator.ofInt(start, end);
        paddingAnimator.addUpdateListener(animation -> ivImage.setPadding(
                ivImage.getPaddingLeft(),
                (Integer) animation.getAnimatedValue(),
                ivImage.getPaddingRight(),
                ivImage.getPaddingBottom()
        ));

        paddingAnimator.setDuration(ANIMATION_DURATION);
        paddingAnimator.start();
    }


    /**
     * 设置颜色变化动画
     *
     * @param previousColor
     * @param color
     */
    private void animateColors(int previousColor, int color) {
        ValueAnimator anim = new ValueAnimator();
        anim.setIntValues(previousColor, color);
        anim.setEvaluator(new ArgbEvaluator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                setColors((Integer) valueAnimator.getAnimatedValue());
            }
        });

        anim.setDuration(ANIMATION_DURATION);
        anim.start();
    }

    /**
     * 设置颜色
     *
     * @param color
     */
    private void setColors(int color) {
        if (ivImage != null) {
            ivImage.setColorFilter(color);
            ivImage.setTag(R.id.bb_bottom_bar_color_id, color);
        }
        if (tvName != null) {
            tvName.setTextColor(color);
        }
    }

    /**
     * 设置不透明度
     *
     * @param alpha
     */
    private void setAlphas(float alpha) {
        if (ivImage != null) {
            ViewCompat.setAlpha(ivImage, alpha);
        }

        if (tvName != null) {
            ViewCompat.setAlpha(tvName, alpha);
        }
    }
}