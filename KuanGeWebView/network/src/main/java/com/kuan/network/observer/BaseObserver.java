package com.kuan.network.observer;

import com.kuan.base.model.MVVMBaseMode;
import com.kuan.base.model.MVVMDataObserver;
import com.kuan.network.error.ExceptionHandle;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by hongkuan on 2021-06-08 0008.
 */
public class BaseObserver<T> implements Observer<T> {
    private MVVMBaseMode mMVVMBaseMode;
    private MVVMDataObserver mMVVMDataObserver;

    public BaseObserver(MVVMBaseMode MVVMBaseMode, MVVMDataObserver MVVMDataObserver) {
        mMVVMBaseMode = MVVMBaseMode;
        mMVVMDataObserver = MVVMDataObserver;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        //开始
        if (null != mMVVMBaseMode) mMVVMBaseMode.addDisposable(d);
    }

    @Override
    public void onNext(@NonNull T t) {
        //成功
        if (null != mMVVMDataObserver) mMVVMDataObserver.onSuccess(t, false);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        //错误
        if (null == mMVVMDataObserver) return;
        if (e instanceof ExceptionHandle.ResponseException) {
            mMVVMDataObserver.onFailure(e);
        } else
            mMVVMDataObserver.onFailure(new ExceptionHandle.ResponseException(e, ExceptionHandle.ERROR.UNKNOWN));
    }

    @Override
    public void onComplete() {
        //完成
    }
}
