// ICallbackMainProcessToWebViewProcessInterface.aidl
package com.kuan.webview;

// Declare any non-default types here with import statements

interface ICallbackMainProcessToWebViewProcessInterface {
    void onResult(String callbackName, String response);
}