package com.kuan.network.observer;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by hongkuan on 2021-06-08 0008.
 */
public abstract class BaseObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(@NonNull Disposable d) {
        //开始
    }

    @Override
    public void onNext(@NonNull T t) {
        //进行中
        onSuccess(t);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        //出错了
    }

    @Override
    public void onComplete() {
        //完成
    }

    //成功
    protected abstract void onSuccess(T t);

    //失败
    protected abstract void onFail(Throwable e);
}
