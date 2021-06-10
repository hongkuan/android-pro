package com.kuan.base.model;

public interface IBaseModeListener<T> {
    void onLoadFinish(T data, PagingResult... pagingResults);

    void onLoadFail(String errMsg, PagingResult... pagingResults);
}
