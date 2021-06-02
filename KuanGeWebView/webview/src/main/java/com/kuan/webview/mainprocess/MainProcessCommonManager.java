package com.kuan.webview.mainprocess;

import android.content.ComponentName;
import android.content.Intent;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.kuan.base.BaseApplication;
import com.kuan.webview.ICallbackMainProcessToWebViewProcessInterface;
import com.kuan.webview.IWevViewProcessToMainProcessInterface;
import com.kuan.webview.common.ICommand;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Created by hongkuan on 2021-05-11 0011.
 */
public class MainProcessCommonManager extends IWevViewProcessToMainProcessInterface.Stub {
    private static final String TAG = "MainProcessCommonManage";
    private volatile static MainProcessCommonManager sInstance = null;
    private final Map<String, ICommand> mCommands = new HashMap<>();

    private MainProcessCommonManager() {
        Iterator<ICommand> serviceLoader = ServiceLoader.load(ICommand.class).iterator();
        while (serviceLoader.hasNext()) {
            ICommand iCommand = serviceLoader.next();
            mCommands.put(iCommand.getName(), iCommand);
        }
    }

    public static MainProcessCommonManager getInstance() {
        if (null == sInstance) {
            synchronized (MainProcessCommonManager.class) {
                if (null == sInstance) sInstance = new MainProcessCommonManager();
            }
        }
        return sInstance;
    }

    private void executeCommon(String commonName, Map paramMap,
                               ICallbackMainProcessToWebViewProcessInterface callback) {
        ICommand common = mCommands.get(commonName);
        if (null != common) {
            common.execute(paramMap, callback);
        } else {
            Log.e(TAG, commonName+"--未注册命令");
        }
    }

    @Override
    public void handlerCommon(String commonName, String jsParam,
                              ICallbackMainProcessToWebViewProcessInterface callback) throws RemoteException {
        executeCommon(commonName, new Gson().fromJson(jsParam, Map.class), callback);
    }
}
