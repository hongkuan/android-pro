package com.kuan.webview.mainprocess;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * Created by hongkuan on 2021-05-11 0011.
 */
public class MainProcessCommonService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return MainProcessCommonManager.getInstance();
    }
}
