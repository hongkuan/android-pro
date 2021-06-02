package com.kuan.webview.webviewprocess;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.kuan.webview.WebViewCallback;
import com.kuan.webview.bean.BaseJSParam;
import com.kuan.webview.webviewprocess.webchromeclient.KuanGeWebChromeClient;
import com.kuan.webview.webviewprocess.webviewclient.KuanGeWebViewClient;
import com.kuan.webview.webviewprocess.webviewsetting.WebViewDefaultSettings;

import java.util.Map;

/**
 * Created by hongkuan on 2021-05-11 0011.
 */
public class BaseWebView extends WebView {
    private static final String TAG = "BaseWebView";

    public BaseWebView(Context context) {
        super(context);
        init();
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        WebViewProcessCommonDispatcher.getInstance().initAidlConnection();
        WebViewDefaultSettings.getInstance().setSettings(this);
        addJavascriptInterface(this, "kuangewebview");
    }

    public void setWebViewCallback(WebViewCallback webViewCallback) {
        setWebViewClient(new KuanGeWebViewClient(webViewCallback));
        setWebChromeClient(new KuanGeWebChromeClient(webViewCallback));
    }

    @JavascriptInterface
    public void takeNativeAction(final String jsParam) {
        Log.i(TAG, "jsParam: " + jsParam);
        if (!TextUtils.isEmpty(jsParam)) {
            BaseJSParam baseJSParam = new Gson().fromJson(jsParam, BaseJSParam.class);
            if (null != baseJSParam) {
                WebViewProcessCommonDispatcher.getInstance().executeCommon(baseJSParam.getName(), new Gson().toJson(baseJSParam.getParam()), this);
            }
        }
    }

    public void handlerCallback(String callbackName, String response){
        if (TextUtils.isEmpty(callbackName)){
            Log.e(TAG, "handlerCallback: callbackName is null!");
            return;
        }
        if (TextUtils.isEmpty(response)){
            Log.e(TAG, "handlerCallback: response is null!");
            return;
        }

        post(new Runnable() {
            @Override
            public void run() {
                String jsCode = "javascript:kuangejs.callback('" + callbackName + "'," + response + ")";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    evaluateJavascript(jsCode, null);
                }
            }
        });
    }
}
