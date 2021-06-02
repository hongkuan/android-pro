package com.kuan.webview.utils;

import android.os.Environment;

/**
 * Created by hongkuan on 2021-05-08 0008.
 */
public class Constants {
    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String IS_SHOW_ACTION_BAR = "is_show_action_bar";
    public static final String CAN_NATIVE_REFRESH = "can_native_refresh";
    public static final String ANDROID_ASSET_URI = "file:///android_asset/";

    public static final String LOCAL_FILE_PATH_KEY = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String APK_ASSETS_FILE_PATH_KEY = "apk_assets_file";
}
