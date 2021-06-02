package com.kuan.webview;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import com.google.auto.service.AutoService;
import com.kuan.common.autoservice.IWebViewService;
import com.kuan.webview.utils.Constants;

/**
 * Created by hongkuan on 2021-05-08 0008.
 */
@AutoService(IWebViewService.class)
public class WebViewServiceImpl implements IWebViewService {
    @Override
    public void startWebViewActivity(Context context, String url, String title, boolean isShowActionBar) {
        if (null != context){
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra(Constants.TITLE, title);
            intent.putExtra(Constants.URL, url);
            intent.putExtra(Constants.IS_SHOW_ACTION_BAR, isShowActionBar);
            context.startActivity(intent);
        }
    }

    @Override
    public Fragment getWebViewFragment(String url, boolean conNativeRefresh) {
        return WebViewFragment.newInstance(url,conNativeRefresh);
    }

    @Override
    public void startDemoHtml(Context context) {
        if (null != context){
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra(Constants.TITLE, "测试demo");
            //intent.putExtra(Constants.URL, Constants.ANDROID_ASSET_URI+"demo.html");
            intent.putExtra(Constants.URL, "http://192.168.5.10:9877/H5/demo.html");
            intent.putExtra(Constants.IS_SHOW_ACTION_BAR, true);
            context.startActivity(intent);
        }
    }
}
