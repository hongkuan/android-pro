package com.kuan.webview.bean;

import com.google.gson.JsonObject;

/**
 * Created by hongkuan on 2021-05-11 0011.
 */
public class BaseJSParam {
    private String name;
    private JsonObject param;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonObject getParam() {
        return param;
    }

    public void setParam(JsonObject param) {
        this.param = param;
    }
}
