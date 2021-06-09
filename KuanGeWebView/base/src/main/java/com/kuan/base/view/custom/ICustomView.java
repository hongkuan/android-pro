package com.kuan.base.view.custom;

import com.kuan.base.view.custom.BaseCustomViewModel;

public interface ICustomView<T extends BaseCustomViewModel> {

    void setData(T data);
}
