package com.wankun.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.roughike.bottombar.TabSelectionInterceptor;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.wankun.demo.R;
import com.wankun.demo.pictureSelector.FullyGridLayoutManager;
import com.wankun.demo.pictureSelector.GlideManger;
import com.wankun.demo.pictureSelector.GridImageAdapter;
import com.wankun.demo.manage.CommonNetManager;
import com.wankun.demo.application.AppActivityManager;
import com.wankun.demo.model.httpResponse.UploadFiles;
import com.wankun.demo.utils.ToastManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author wankun
 */
public class MainActivity extends RxAppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    Context context;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.bottomBar)
    BottomBar bottomBar;
    GridImageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);

        initImagePicker();


        /**
         * 底部按钮点击 选择监听
         */
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_home) {

                }
            }
        });
        /**
         * 底部按钮 重新点击  选择监听
         */
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_home) {
                    // The tab with id R.id.tab_favorites was reselected,
                    // change your content accordingly.
                }
            }
        });

        /**
         * 底部按钮  选择拦截
         */
        bottomBar.setTabSelectionInterceptor(new TabSelectionInterceptor() {
            @Override
            public boolean shouldInterceptTabSelection(@IdRes int oldTabId, @IdRes int newTabId) {
                if (newTabId == R.id.tab_home) {
                    return false;
                }

                return false;
            }
        });

    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 初始化顶部菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    /**
     * 顶部菜单 点击事件
     *
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 侧滑菜单点击事件
     *
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            GlideManger.imagePicker(MainActivity.this);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 取图片返回后的处理逻辑
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片、视频、音频选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    adapter.replaceAll(selectList);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 取到图片后的处理逻辑
     */
    private void uploaderImage(ArrayList<LocalMedia> selectList) {
        //选择图片返回监听
        if (null != selectList && selectList.size() > 0) {
            CommonNetManager.uploadImages(MainActivity.this, selectList, "SSSS", "SSSs", new Observer<UploadFiles>() {
                @Override
                public void onSubscribe(Disposable d) {
                    Logger.e("onSubscribe");
                }

                @Override
                public void onNext(UploadFiles sUploadFiles) {
                    Logger.e("onNext>>" + sUploadFiles.src.size());
                }

                @Override
                public void onError(Throwable e) {
                    Logger.e("onError" + e.getLocalizedMessage());
                }

                @Override
                public void onComplete() {
                    Logger.e("onComplete");
                }
            });

        }
    }


    private void initImagePicker() {
        List<LocalMedia> selectList = new ArrayList<>();
        int maxSelectNum = 9;

        FullyGridLayoutManager manager = new FullyGridLayoutManager(MainActivity.this, 4, GridLayoutManager.VERTICAL, false);
        recycler.setLayoutManager(manager);
        adapter = new GridImageAdapter(MainActivity.this, new GridImageAdapter.OnAddPicClickListener() {
            @Override
            public void onAddPicClick() {
                GlideManger.imagePicker(MainActivity.this);
            }
        });
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        recycler.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            PictureSelector.create(MainActivity.this).themeStyle(R.style.pictureDefaultStyle).openExternalPreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(MainActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(MainActivity.this).externalPictureAudio(media.getPath());
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    /**
     * 返回按钮点击计时
     */
    private long firsTime = 0;

    /**
     * 连续点击弹出退出提示
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //提示用户退出程序
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (null != drawerLayout && drawerLayout.isDrawerOpen(GravityCompat.START)) {
                //关闭侧滑栏
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

            if ((System.currentTimeMillis() - firsTime) > 2000) {
                ToastManager.showToast(R.string.out_tips);
                firsTime = System.currentTimeMillis();
            } else {
                AppActivityManager.getInstance().appExit(true);
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
