package com.kuan.webview;

/**
 * Created by hongkuan on 2021-05-10 0010.
 */
public interface WebViewCallback {
    void onPageStart(String url);
    void onPageFinished(String url);
    void onPageError();

    void updateTitle(String title);
}
