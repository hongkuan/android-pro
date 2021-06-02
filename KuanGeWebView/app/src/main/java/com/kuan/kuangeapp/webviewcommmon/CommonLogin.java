package com.kuan.kuangeapp.webviewcommmon;

import android.os.RemoteException;
import android.util.Log;

import com.google.auto.service.AutoService;
import com.google.gson.Gson;
import com.kuan.base.autoservice.KuanGeServiceLoader;
import com.kuan.common.autoservice.IUserCenterService;
import com.kuan.common.eventbus.LoginEvent;
import com.kuan.webview.ICallbackMainProcessToWebViewProcessInterface;
import com.kuan.webview.common.ICommand;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hongkuan on 2021-05-12 0012.
 */
@AutoService(ICommand.class)
public class CommonLogin implements ICommand {
    private static final String TAG = "CommonLogin";
    private String mWebViewCallbackName;
    private ICallbackMainProcessToWebViewProcessInterface mWebViewCallback;
    private final IUserCenterService userCenterService;

    public CommonLogin(){
        EventBus.getDefault().register(this);
        userCenterService = KuanGeServiceLoader.load(IUserCenterService.class);
    }

    @Override
    public String getName() {
        return "login";
    }

    @Override
    public void execute(Map paramMap, ICallbackMainProcessToWebViewProcessInterface callback) {
        IUserCenterService service = KuanGeServiceLoader.load(IUserCenterService.class);
        String callbackName = String.valueOf(paramMap.get("callbackname"));
        if (null != userCenterService) {
            if (userCenterService.isLogin()){
                userCenterService.login();
                this.mWebViewCallbackName = callbackName;
                this.mWebViewCallback = callback;
            }
        } else {
            Log.e(TAG, "未找到Login模块的service实现");
        }
    }

    @Subscribe
    public void onMesEvent(LoginEvent loginEvent){
        String userName = loginEvent.getUserName();
        Map responseMap = new HashMap();
        responseMap.put("accountName", userName);
        try {
            this.mWebViewCallback.onResult(this.mWebViewCallbackName, new Gson().toJson(responseMap));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
