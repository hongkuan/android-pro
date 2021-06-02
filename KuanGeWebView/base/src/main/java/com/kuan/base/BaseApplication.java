package com.kuan.base;

import android.app.Application;

/**
 * Created by hongkuan on 2021-05-10 0010.
 */
public class BaseApplication extends Application {

    public static BaseApplication sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }
}
