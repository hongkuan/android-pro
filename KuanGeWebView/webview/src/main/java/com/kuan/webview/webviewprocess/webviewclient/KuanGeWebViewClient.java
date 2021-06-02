package com.kuan.webview.webviewprocess.webviewclient;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.MimeTypeMap;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.kuan.base.BaseApplication;
import com.kuan.webview.WebViewCallback;
import com.kuan.webview.utils.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by hongkuan on 2021-05-10 0010.
 */
public class KuanGeWebViewClient extends WebViewClient {
    private static final String TAG = "KuanGeWebViewClient";
    private final WebViewCallback mWebViewCallback;

    public KuanGeWebViewClient(WebViewCallback callback){
        this.mWebViewCallback = callback;
    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return tmpShouldOverrideUrlLoading(view, url);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return tmpShouldOverrideUrlLoading(view, request.getUrl().toString());
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (null != mWebViewCallback){
            mWebViewCallback.onPageStart(url);
        }else {
            Log.e(TAG, "onPageStarted: WebViewCallback is null.");
        }
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (null != mWebViewCallback){
            mWebViewCallback.onPageFinished(url);
        } else {
            Log.e(TAG, "onPageFinished: WebViewCallback is null.");
        }
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        if (null != mWebViewCallback){
            mWebViewCallback.onPageError();
        } else {
            Log.e(TAG, "onReceivedError: WebViewCallback is null.");
        }
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
        WebResourceResponse wetRes = loadLocalRes(request.getUrl().toString());
        if (null != wetRes) return wetRes;
        return super.shouldInterceptRequest(view, request);
    }

    @Nullable
    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        WebResourceResponse wetRes = loadLocalRes(url);
        if (null != wetRes) return wetRes;
        return super.shouldInterceptRequest(view, url);
    }

    private boolean tmpShouldOverrideUrlLoading(WebView view, String url){
        if (url.startsWith("http:") || url.startsWith("https:")|| url.startsWith("file:")) {
            view.loadUrl(url);
            return false;
        } else {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                view.getContext().startActivity(intent);
            } catch (Exception e){
                Log.e(TAG,e.getMessage());
            }
            return true;
        }
    }

    private WebResourceResponse loadLocalRes(String url){
        if (url.contains(Constants.LOCAL_FILE_PATH_KEY)){
            File file = getLocalFile(url, Constants.LOCAL_FILE_PATH_KEY);
            try {
                return interceptRes(file.getName(), new FileInputStream(file.getAbsolutePath()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        } else if (url.contains(Constants.APK_ASSETS_FILE_PATH_KEY)) {
            String filePath = url.split(Constants.APK_ASSETS_FILE_PATH_KEY)[1];
            InputStream is = getApkAssetsFile(url);
            return interceptRes(filePath, is);
        }
        return null;
    }

    private InputStream getApkAssetsFile( String filePath) {

        AssetManager assetsManager = BaseApplication.sApplication.getAssets();
        try {
            return assetsManager.open(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private File getLocalFile(String url, String localFilePathKey) {
        String filePath = url.split(localFilePathKey)[1];
        File file = new File(localFilePathKey + filePath);//获取本地的资源
        if (!file.exists()) {
            //假设本地没有这个资源,即开始没有下载好,则返回null
            //表示不拦截,去请求服务器的资源
            Log.d(TAG, "getLocalFile =》file no found");
            return null;
        }
        return file;
    }

    private WebResourceResponse interceptRes(String fileName, InputStream is) {
        if (null == is) return null;
        //假设请求url包含res资源目录,则表明是去想服务器请求资源,而本地
        //已经包含了这个资源,则拦截这个请求,返回本地的资源
        //注意返回一个WebResourceResponse则表示拦截,如果返回null则表示不拦截,正常请求服务器
        WebResourceResponse webResourceResponse = null;
        try {
            int dianIndex = fileName.lastIndexOf(".");
            if (dianIndex != -1) {//获取资源文件的格式,例如这是一个.png,还是.js,还是.mp3
                String mime = fileName.substring(dianIndex + 1);
                if (mime.equals("js")) {//MimeTypeMap没有js格式需要特殊处理
                    mime = "application/x-javascript";
                } else if (mime.equals("ttf")) {//MimeTypeMap没有字体格式需要特殊处理
                    mime = "application/octet-stream";
                } else{//其他格式可以MimeTypeMap获取其mime类型
                    mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(mime);
                }
                if (mime == null) {//假设没有获取到正常的mime类型,则设置为通用类型
                    mime = "*/*";
                }
                Log.d(TAG, " mime: " + mime);

                //一切正常,创建webResourceResponse
                webResourceResponse = new WebResourceResponse(mime, "utf-8", is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return webResourceResponse;
    }
}
