package com.kuan.kuangeapp.webviewcommmon;

import android.content.ComponentName;
import android.content.Intent;
import android.text.TextUtils;

import com.google.auto.service.AutoService;
import com.kuan.kuangeapp.KuanGeApplication;
import com.kuan.webview.ICallbackMainProcessToWebViewProcessInterface;
import com.kuan.webview.common.ICommand;

import java.util.Map;

/**
 * Created by hongkuan on 2021-05-11 0011.
 */
@AutoService(ICommand.class)
public class CommonOpenMainProcessActivity implements ICommand {
    @Override
    public String getName() {
        return "openActivity";
    }

    @Override
    public void execute(Map paramMap, ICallbackMainProcessToWebViewProcessInterface callback) {
        String targetClass = String.valueOf(paramMap.get("target_class"));
        if (!TextUtils.isEmpty(targetClass)) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(KuanGeApplication.sApplication, targetClass));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            KuanGeApplication.sApplication.startActivity(intent);
        }
    }
}
