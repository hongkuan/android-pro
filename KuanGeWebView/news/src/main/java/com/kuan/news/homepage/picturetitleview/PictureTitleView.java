package com.kuan.news.homepage.picturetitleview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.kuan.base.autoservice.KuanGeServiceLoader;
import com.kuan.common.autoservice.IWebViewService;
import com.kuan.news.R;
import com.kuan.news.databinding.PictureTitleViewBinding;
import com.kuan.news.homepage.base.ICustomView;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public class PictureTitleView extends LinearLayout implements ICustomView<PictureTitleViewModel> {
    private PictureTitleViewBinding dataBinding;
    private PictureTitleViewModel mViewModel;

    public PictureTitleView(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.picture_title_view, this, false);
        dataBinding.getRoot().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mViewModel && !TextUtils.isEmpty(mViewModel.link)){
                    IWebViewService webViewService = KuanGeServiceLoader.load(IWebViewService.class);
                    webViewService.startWebViewActivity(getContext(), mViewModel.link, "新闻",true);
                }
            }
        });
        this.addView(dataBinding.getRoot());
    }

    @Override
    public void setData(PictureTitleViewModel data) {
        this.dataBinding.setViewModel(data);
        this.dataBinding.executePendingBindings();
        this.mViewModel = data;
    }
}
