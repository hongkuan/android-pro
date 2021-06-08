package com.kuan.news.homepage.base;

public interface ICustomView<T extends BaseCustomViewModel> {

    void setData(T data);
}
