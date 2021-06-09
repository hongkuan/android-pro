package com.kuan.common.views.titleview;

import android.content.Context;
import android.view.View;

import com.kuan.base.autoservice.KuanGeServiceLoader;
import com.kuan.base.view.custom.BaseCustomView;
import com.kuan.common.R;
import com.kuan.common.autoservice.IWebViewService;
import com.kuan.common.databinding.TitleViewBinding;

public class TitleView extends BaseCustomView<TitleViewBinding, TitleViewModel> {

    public TitleView(Context context) {
        super(context);
    }

    @Override
    protected int getViewLayoutId() {
        return R.layout.title_view;
    }

    @Override
    protected void onRootClick(View view) {
        IWebViewService webViewService = KuanGeServiceLoader.load(IWebViewService.class);
        webViewService.startWebViewActivity(getContext(), getViewModel().link, "新闻", true);
    }

    @Override
    protected void setDataToView(TitleViewModel data) {
        getDataBinding().setViewModel(data);
    }


}
