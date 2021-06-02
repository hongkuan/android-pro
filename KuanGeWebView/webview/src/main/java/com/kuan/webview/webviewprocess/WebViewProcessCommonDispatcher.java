package com.kuan.webview.webviewprocess;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.kuan.base.BaseApplication;
import com.kuan.webview.ICallbackMainProcessToWebViewProcessInterface;
import com.kuan.webview.IWevViewProcessToMainProcessInterface;
import com.kuan.webview.mainprocess.MainProcessCommonService;

/**
 * Created by hongkuan on 2021-05-11 0011.
 */
public class WebViewProcessCommonDispatcher implements ServiceConnection {
    private static final String TAG = "WebViewProcessCommonDis";
    private static volatile WebViewProcessCommonDispatcher sInstance = null;
    private IWevViewProcessToMainProcessInterface iWebViewProcessToMainProcessInterface;

    private WebViewProcessCommonDispatcher(){

    }

    public static WebViewProcessCommonDispatcher getInstance(){
        if (null == sInstance) {
            synchronized (WebViewProcessCommonDispatcher.class){
                if (null == sInstance) sInstance = new WebViewProcessCommonDispatcher();
            }
        }
        return sInstance;
    }

    public void initAidlConnection(){
        Intent intent = new Intent(BaseApplication.sApplication, MainProcessCommonService.class);
        BaseApplication.sApplication.bindService(intent, this, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        iWebViewProcessToMainProcessInterface = IWevViewProcessToMainProcessInterface.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        iWebViewProcessToMainProcessInterface = null;
        initAidlConnection();
    }

    @Override
    public void onBindingDied(ComponentName name) {
        iWebViewProcessToMainProcessInterface = null;
        initAidlConnection();
    }

    public void executeCommon( String commonName, String jsParam, BaseWebView baseWebView){
        if (null != iWebViewProcessToMainProcessInterface){
            try {
                iWebViewProcessToMainProcessInterface.handlerCommon(commonName, jsParam, new ICallbackMainProcessToWebViewProcessInterface.Stub() {
                    @Override
                    public void onResult(String callbackName, String response) throws RemoteException {
                        baseWebView.handlerCallback(callbackName, response);
                    }
                });
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.e(TAG, "executeCommon: ", e);
            }
        }
    }
}
