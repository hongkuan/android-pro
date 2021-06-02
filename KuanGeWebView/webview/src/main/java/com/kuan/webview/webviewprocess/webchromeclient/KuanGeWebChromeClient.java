package com.kuan.webview.webviewprocess.webchromeclient;

import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.kuan.webview.WebViewCallback;

/**
 * Created by hongkuan on 2021-05-10 0010.
 */
public class KuanGeWebChromeClient extends WebChromeClient {
    private static final String TAG = "KuanGeWebChromeClient";
    private final WebViewCallback mWebViewCallback;

    public KuanGeWebChromeClient(WebViewCallback webViewCallback){
        this.mWebViewCallback = webViewCallback;
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        if (null != mWebViewCallback){
            mWebViewCallback.updateTitle(title);
        }else {
            Log.e(TAG, "onReceivedTitle: WebViewCallback is null.");
        }
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        Log.d(TAG, consoleMessage.message());
        return super.onConsoleMessage(consoleMessage);
    }
}
