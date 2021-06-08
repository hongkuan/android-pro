package com.kuan.news.api;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface INewsChannelApi {

    @GET("release/channel")
    Observable<NewsChannelsBean> getNewsChannels();
}
