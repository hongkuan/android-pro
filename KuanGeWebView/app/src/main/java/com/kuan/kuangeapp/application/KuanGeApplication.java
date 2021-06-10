package com.kuan.kuangeapp.application;

import androidx.multidex.MultiDex;

import com.kingja.loadsir.core.LoadSir;
import com.kuan.base.BaseApplication;
import com.kuan.base.loadsir.CustomCallback;
import com.kuan.base.loadsir.EmptyCallback;
import com.kuan.base.loadsir.ErrorCallback;
import com.kuan.base.loadsir.LoadingCallback;
import com.kuan.base.loadsir.TimeoutCallback;
import com.kuan.base.preference.PreferencesUtil;
import com.kuan.kuangeapp.BuildConfig;
import com.kuan.network.base.INetworkConfigInfo;
import com.kuan.network.TecentNetworkApi;

/**
 * Created by hongkuan on 2021-05-10 0010.
 */
public class KuanGeApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        PreferencesUtil.init(this);
        MultiDex.install(this);
        initLoadSir();
        initNetworkConfigInfo();
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

    private void initNetworkConfigInfo(){
        TecentNetworkApi.init(new INetworkConfigInfo() {
            @Override
            public String getAPPVersionName() {
                return BuildConfig.VERSION_NAME;
            }

            @Override
            public String getAPPVersionCode() {
                return String.valueOf(BuildConfig.VERSION_CODE);
            }

            @Override
            public boolean isDebug() {
                return BuildConfig.DEBUG;
            }
        });
    }
}
