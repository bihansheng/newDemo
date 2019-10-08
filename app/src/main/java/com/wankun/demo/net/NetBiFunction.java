package com.wankun.demo.net;


import android.util.Log;

import com.wankun.demo.utils.ToastManager;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

import static com.luck.picture.lib.permissions.RxPermissions.TAG;

/**
 * 预处理 返回数据 和 请求错误情况
 * @author wankun
 */

    public class NetBiFunction<T> implements Function< Observer<T>, Observer<T>> {


    @Override
    public Observer<T> apply(Observer observer) throws Exception {
        return new NetBiFunction.ObservableSubscribeHooker(observer);
    }


    class ObservableSubscribeHooker<T> implements Observer<T> {
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
                    throw new NetException(NetException.RES_IS_ERROR, httpResult.getMsg());
                    //其他的错误，将flg传给具体的页面去
                } else if (httpResult.getCode() != NetException.RES_IS_SUCCESS) {

                    if (httpResult.getCode() == 100) {
                        ToastManager.showToast("登录过期");
                        //TODO 跳转到登录，或者重新执行登录操作
                        throw new NetException.TokenExpiredException();
                    }


                    throw new NetException(httpResult.getCode(), httpResult.getMsg());
                }


                actual.onNext((T) httpResult.getData());
            }else{
                actual.onNext(t);
            }
        }



        @Override
        public void onError(Throwable e) {

            if (e instanceof ConnectException) {
                Log.e(TAG, "Connect failed: ", e);
                ToastManager.showToast("无网络连接");
                actual.onError(new NetException.OfflineException());
                return;
            }

            if (e instanceof SocketTimeoutException) {
                Log.e(TAG, "Time out ", e);
                ToastManager.showToast("连接超时");
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

}
