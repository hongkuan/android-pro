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
        //��ʼ
    }

    @Override
    public void onNext(@NonNull T t) {
        //������
        onSuccess(t);
    }

    @Override
    public void onError(@NonNull Throwable e) {
        //������
    }

    @Override
    public void onComplete() {
        //���
    }

    //�ɹ�
    protected abstract void onSuccess(T t);

    //ʧ��
    protected abstract void onFail(Throwable e);
}
