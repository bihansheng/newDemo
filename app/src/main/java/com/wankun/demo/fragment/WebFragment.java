package com.wankun.demo.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;
import com.orhanobut.logger.Logger;
import com.wankun.demo.R;
import com.wankun.demo.base.BaseFragment;
import com.wankun.demo.pictureSelector.GlideManger;

import java.io.File;
import java.util.List;

import butterknife.BindView;


/**
 * Description：
 * Created by : wankun
 * Time：2017-03-10  11-00
 *
 * @author wankun
 */
@SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
public class WebFragment extends BaseFragment {

    @BindView(R.id.webView)
    public WebView mWebView;
    @BindView(R.id.progressbar)
    public ProgressBar mProgressbar;

    String url;

    /**
     * 网页类型
     */
    static public final int NORMAL = 0;
    /**
     * //文件选择返回监听
     */
    private ValueCallback<Uri> mUploadMessage;
    /**
     * //5。0以上版本
     */
    private ValueCallback<Uri[]> mUploadCallbackAboveL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public int setLayoutResId() {
        return R.layout.fragment_web;
    }


    public static WebFragment newInstance(String url, int type) {
        WebFragment fragment = new WebFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }



    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void initView() {
        url = getArguments().getString("url");
        //填补js远程代码执行漏洞
        if (Build.VERSION.SDK_INT >= 11) {
            mWebView.removeJavascriptInterface("searchBoxJavaBridge_");
            mWebView.removeJavascriptInterface("accessibility");
            mWebView.removeJavascriptInterface("accessibilityTraversal");
        }
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //允许访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setSupportZoom(false);
        //设置缓存模式（不使用缓存，只从网络获取数据.）
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//支持缓存
        ////启动内置缩放
        webSettings.setBuiltInZoomControls(true);
        //自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        //自适应屏幕
        webSettings.setLoadWithOverviewMode(true);
        mWebView.addJavascriptInterface(new MJavaScriptInterface(getHolder()), "kuaihuoha");
        //进度条的加载
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (null != mProgressbar) {
                    if (newProgress == 0) {
                        mProgressbar.setVisibility(View.VISIBLE);
                    } else if (newProgress == 100) {
                        mProgressbar.setVisibility(View.GONE);
                    } else {
                        if (mProgressbar.getVisibility() == View.GONE) {
                            mProgressbar.setVisibility(View.VISIBLE);
                        }
                        mProgressbar.setProgress(newProgress);
                    }
                }
                super.onProgressChanged(view, newProgress);
            }

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                openFileChooser(mUploadMessage, "", "");
            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                openFileChooser(mUploadMessage, acceptType, "");
            }

            //For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                getPic();

            }

            // For Android 5.0+
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mUploadCallbackAboveL = filePathCallback;
                getPic();
                return true;
            }
        });


        loadPage(url);
    }


    /**
     * 选择图片
     */
    private void getPic() {
        GlideManger.imagePicker(getHolder());
    }


    /**
     * 文件选择返回 监听
     *
     * @param resultCode
     * @param data
     */
    public void fileChooseResult(int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (null == mUploadCallbackAboveL) {
                return;
            }
            Uri[] results = null;
            if ((data != null)) {
                List<LocalMedia> images = PictureSelector.obtainMultipleResult(data);
                if (images != null && images.size() > 0) {
                    String path = images.get(0).getPath();
                    results = new Uri[]{Uri.fromFile(new File(path))};
                }
            }
            mUploadCallbackAboveL.onReceiveValue(results);
            mUploadCallbackAboveL = null;
            return;
        } else {
            if (null == mUploadMessage) {
                return;
            }
            Uri result = null;
            if ((data != null)) {
                List<LocalMedia> images = PictureSelector.obtainMultipleResult(data);
                if (images != null && images.size() > 0) {
                    String path = images.get(0).getPath();
                    result = Uri.fromFile(new File(path));
                }
            }
            if (mUploadMessage != null) {
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }


    }


    /**
     * 这是js从android调用方法的类
     *
     * @author Administrator
     */
    public class MJavaScriptInterface {
        Context context;

        public MJavaScriptInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public boolean openPage(String s) {
            return mOpenPage(s);
        }

        @JavascriptInterface
        public boolean closePage() {
            getActivity().finish();
            return true;
        }

    }

    public void loadPage(String url) {
        Logger.e(">>>>>>>>>>>" + url);
        Logger.e(url);
        mWebView.loadUrl(url);
    }


    /**
     * TODO  拦截网页中url跳转事件
     *
     * @param url
     * @return
     */
    public boolean mOpenPage(String url) {
        Logger.e(">>>>>>>>>>>" + url);
        //此处能拦截超链接的url,即拦截href请求的内容.
        if (TextUtils.isEmpty(url)) {
            return true;
        }
        //其他的连接在当前界面打开
        return true;

    }


    /**
     * 点击放回按钮
     *
     * @return
     */
    public boolean goBack() {
        if (mWebView.canGoBack()) {
            WebBackForwardList mWebBackForwardList = mWebView.copyBackForwardList();
            if (mWebBackForwardList.getCurrentIndex() > 0) {
                String historyUrl = mWebBackForwardList.getItemAtIndex(mWebBackForwardList.getCurrentIndex() - 1).getUrl();
                if (!historyUrl.equals(url)) {
                    mWebView.goBack();
                    return true;
                }
            }
        }
        return false;
    }

    public void clearCache() {
        if (mWebView != null) {
            mWebView.clearFormData();
            mWebView.clearHistory();
            mWebView.clearCache(true);
        }
    }

    @Override
    public void onDestroy() {
        //关闭时 销户webview防止内存泄漏
        try {
            if (mWebView != null) {
                ViewParent parent = mWebView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(mWebView);
                }
                mWebView.stopLoading();
                // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
                mWebView.getSettings().setJavaScriptEnabled(false);
                mWebView.clearHistory();
                mWebView.clearView();
                mWebView.removeAllViews();
                mWebView.destroy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
