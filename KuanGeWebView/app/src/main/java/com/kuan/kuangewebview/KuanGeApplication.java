package com.kuan.kuangewebview;

import com.kingja.loadsir.core.LoadSir;
import com.kuan.base.BaseApplication;
import com.kuan.base.loadsir.CustomCallback;
import com.kuan.base.loadsir.EmptyCallback;
import com.kuan.base.loadsir.ErrorCallback;
import com.kuan.base.loadsir.LoadingCallback;
import com.kuan.base.loadsir.TimeoutCallback;

/**
 * Created by hongkuan on 2021-05-10 0010.
 */
public class KuanGeApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        initLoadSir();

    }

    private void initLoadSir() {
        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .addCallback(new TimeoutCallback())
                .addCallback(new CustomCallback())
                .setDefaultCallback(LoadingCallback.class)
                .commit();
    }
}
