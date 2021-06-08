package com.kuan.network.commoninterceptor;

import com.kuan.network.base.INetworkConfigInfo;
import com.kuan.network.utils.TecentUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CommonRequestInterceptor implements Interceptor {
    private INetworkConfigInfo mConfigInfo = null;
    public CommonRequestInterceptor(INetworkConfigInfo configInfo) {
        mConfigInfo = configInfo;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.addHeader("appVersionName",mConfigInfo.getAPPVersionName());
        builder.addHeader("appVersionCode",mConfigInfo.getAPPVersionCode());
        return chain.proceed(builder.build());
    }
}
