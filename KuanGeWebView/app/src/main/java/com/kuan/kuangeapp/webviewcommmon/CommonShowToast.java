package com.kuan.kuangeapp.webviewcommmon;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.auto.service.AutoService;
import com.kuan.kuangeapp.application.KuanGeApplication;
import com.kuan.webview.ICallbackMainProcessToWebViewProcessInterface;
import com.kuan.webview.common.ICommand;

import java.util.Map;

/**
 * Created by hongkuan on 2021-05-11 0011.
 */
@AutoService(ICommand.class)
public class CommonShowToast implements ICommand {
    @Override
    public String getName() {
        return "showToast";
    }

    @Override
    public void execute(Map paramMap, ICallbackMainProcessToWebViewProcessInterface callback) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(KuanGeApplication.sApplication,
                        String.valueOf(paramMap.get("message")),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
