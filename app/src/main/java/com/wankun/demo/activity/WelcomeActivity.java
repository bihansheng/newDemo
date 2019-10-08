/**
 * Copyright (C), 2018-2019, wankun
 */
package com.wankun.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.wankun.demo.R;
import com.wankun.demo.base.BaseActivity;
import com.wankun.demo.common.KeyName;
import com.wankun.demo.utils.FileUtil;
import com.wankun.demo.utils.PermissionsChecker;
import com.wankun.demo.utils.SharedPreferencesUtil;

/**
 * 〈启动页〉
 *
 * @author wankun
 * @create 2019/5/9
 * @since 1.0.0
 */
public class WelcomeActivity extends BaseActivity {

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    //如果是第一次打开，进入引导页，如果不是直接进入主页
                    SharedPreferencesUtil.getData(KeyName.IS_FIRST, true);
                    Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                    startActivity(intent);
                    WelcomeActivity.this.finish();
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //取消标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //取消状态栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();
    }


    /**
     * 初始化数据
     */
    private void init() {
        // 权限判断
        PermissionsChecker.verifyStoragePermissions(WelcomeActivity.this);
        //创建本地文件夹
        FileUtil.createDirectory(this);
        handler.sendEmptyMessageDelayed(0, 2000);
    }

    /**
     * 禁用返回按钮
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}