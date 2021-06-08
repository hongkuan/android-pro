package com.kuan.news.homepage.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface INewsChannelApi {

    @GET("release/channel")
    Observable<NewsChannelsBean> getNewsChannels();

    @GET("release/news")
    Observable<NewsListBean> getNewsList(@Query("channelId") String channelId,
                                         @Query("channelName") String channelName,
                                         @Query("page") String page);
}
