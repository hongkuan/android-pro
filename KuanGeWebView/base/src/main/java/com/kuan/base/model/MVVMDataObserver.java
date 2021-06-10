package com.kuan.base.model;

public interface MVVMDataObserver<T> {
    void onSuccess(T data, boolean isFromCache);
    void onFailure(Throwable e);
}
