package com.kuan.network;

import android.util.Log;

import com.google.gson.Gson;
import com.kuan.network.beans.NewsChannelsBean;
import com.kuan.network.commoninterceptor.CommonRequestInterceptor;
import com.kuan.network.commoninterceptor.CommonResponseInterceptor;
import com.kuan.network.utils.TecentUtil;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkApi {
    public static final String TECENT_BASE_URL = "http://service-o5ikp40z-1255468759.ap-shanghai.apigateway.myqcloud.com/";
    private static final String TAG = "NetworkApi";
    private static INetworkConfigInfo mINetworkConfigInfo = null;

    public static void init(INetworkConfigInfo iNetworkConfigInfo){
        mINetworkConfigInfo =iNetworkConfigInfo;
    }

    public static void getTecentNewsChannels(){
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(TECENT_BASE_URL);
        builder.client(getOkHttpClient());
        builder.addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        INewsChannelApi channelsApi = retrofit.create(INewsChannelApi.class);
        Call<NewsChannelsBean> newChannels = channelsApi.getNewsChannels();
        newChannels.enqueue(new Callback<NewsChannelsBean>() {
            @Override
            public void onResponse(Call<NewsChannelsBean> call, Response<NewsChannelsBean> response) {
                Log.d(TAG, "onResponse: date=>" + new Gson().toJson(response.body()));
            }

            @Override
            public void onFailure(Call<NewsChannelsBean> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        okBuilder.addInterceptor(new CommonRequestInterceptor(mINetworkConfigInfo))
                .addInterceptor(new CommonResponseInterceptor());

        if (null != mINetworkConfigInfo && mINetworkConfigInfo.isDebug()){
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okBuilder.addInterceptor(loggingInterceptor);
        }

        return okBuilder.build();
    }
}
