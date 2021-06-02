package com.kuan.common.autoservice;

import android.content.Context;

import androidx.fragment.app.Fragment;

/**
 * Created by hongkuan on 2021-05-08 0008.
 */
public interface IWebViewService {
    void startWebViewActivity(Context context, String url, String title, boolean isShowActionBar);
    Fragment getWebViewFragment(String url, boolean conNativeRefresh);

    void startDemoHtml(Context context);
}
