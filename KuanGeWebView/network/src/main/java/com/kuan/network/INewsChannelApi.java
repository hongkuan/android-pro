package com.kuan.network;

import com.kuan.network.beans.NewsChannelsBean;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface INewsChannelApi {

    @GET("release/channel")
    Call<NewsChannelsBean> getNewsChannels();
}
