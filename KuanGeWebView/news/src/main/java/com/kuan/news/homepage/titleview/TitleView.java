package com.kuan.news.homepage.titleview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.kuan.base.autoservice.KuanGeServiceLoader;
import com.kuan.common.autoservice.IWebViewService;
import com.kuan.news.R;
import com.kuan.news.databinding.TitleViewBinding;
import com.kuan.news.homepage.base.ICustomView;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import static com.kuan.news.BR.viewModel;

public class TitleView extends LinearLayout implements ICustomView<TitleViewModel> {

    private TitleViewBinding dataBinding;
    private TitleViewModel mViewModel;

    public TitleView(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.title_view, this, false);
        dataBinding.getRoot().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mViewModel && !TextUtils.isEmpty(mViewModel.link)){
                    IWebViewService webViewService = KuanGeServiceLoader.load(IWebViewService.class);
                    webViewService.startWebViewActivity(getContext(), mViewModel.link, "新闻", true);
                }
            }
        });
        this.addView(dataBinding.getRoot());
    }

    @Override
    public void setData(TitleViewModel data) {
        dataBinding.setViewModel(data);
        dataBinding.executePendingBindings();
        this.mViewModel = data;
    }
}
