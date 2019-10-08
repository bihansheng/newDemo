package com.wankun.demo.net;


import com.orhanobut.logger.Logger;
import com.wankun.demo.utils.ToastManager;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import static com.luck.picture.lib.permissions.RxPermissions.TAG;

/**
 * 返回数据预处理
 * @author wankun
 */
public class ObservableSubscribeHooker<T> implements Observer<T> {
    private Observer<T> actual;

    public ObservableSubscribeHooker(Observer<T> actual) {
        this.actual = actual;
    }

    @Override
    public void onSubscribe(Disposable d) {
        actual.onSubscribe(d);
    }

    @Override
    public void onNext(T t) {
        if (t instanceof HttpResult) {
            HttpResult httpResult = (HttpResult) t;
            //flg为0，即请求结果错误
            if (httpResult.getCode() == NetException.RES_IS_ERROR) {
                //打印服务端传来的数据
                ToastManager.showToastOnMain(httpResult.getMsg());
                throw new NetException(NetException.RES_IS_ERROR, httpResult.getMsg());
            } else if (httpResult.getCode() != NetException.RES_IS_SUCCESS) {
                //其他的错误，将flg传给具体的页面去

                //TODO 跳转到登录，或者重新执行登录操作
                switch (httpResult.getCode()) {
                    case 100:
                        ToastManager.showToastOnMain("登录过期");
                        throw new NetException.TokenExpiredException();
                    default:
                        throw new NetException(httpResult.getCode(), httpResult.getMsg());
                }
            }
            actual.onNext((T) httpResult.getData());
        } else {
            actual.onNext(t);
        }
    }


    @Override
    public void onError(Throwable e) {
        if (e instanceof ConnectException) {
            Logger.e(TAG, "Connect failed: ", e);
            ToastManager.showToastOnMain("链接服务器失败");
            actual.onError(new NetException.OfflineException());
            return;
        }

        if (e instanceof SocketTimeoutException) {
            Logger.e(TAG, "Time out ", e);
            ToastManager.showToastOnMain("连接超时");
            actual.onError(new NetException.TimeOutException());
            return;
        }

        //其余的异常处理...

        actual.onError(e);
    }

    @Override
    public void onComplete() {
        actual.onComplete();
    }
}