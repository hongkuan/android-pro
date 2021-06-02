// IWevViewProcessToMainProcessInterface.aidl
package com.kuan.webview;

// Declare any non-default types here with import statements
import com.kuan.webview.ICallbackMainProcessToWebViewProcessInterface;

interface IWevViewProcessToMainProcessInterface {

    void handlerCommon(String commonName, String jsParam, ICallbackMainProcessToWebViewProcessInterface callback);
}