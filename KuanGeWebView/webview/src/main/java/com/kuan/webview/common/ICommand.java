package com.kuan.webview.common;

import com.kuan.webview.ICallbackMainProcessToWebViewProcessInterface;

import java.util.Map;

/**
 * Created by hongkuan on 2021-05-11 0011.
 */
public interface ICommand {
    String getName();
    void execute(Map paramMap, ICallbackMainProcessToWebViewProcessInterface callback);
}
