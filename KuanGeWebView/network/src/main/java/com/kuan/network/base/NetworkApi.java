package com.kuan.network.base;

import com.kuan.network.beans.TecentBaseResponse;
import com.kuan.network.commoninterceptor.CommonRequestInterceptor;
import com.kuan.network.commoninterceptor.CommonResponseInterceptor;
import com.kuan.network.error.ExceptionHandle;
import com.kuan.network.error.HttpErrorHandle;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class NetworkApi {
    private static final String TAG = "NetworkApi";
    private static INetworkConfigInfo mINetworkConfigInfo = null;
    private static Map<String, Retrofit> mCacheRetrofit = new HashMap<>();

    public static void init(INetworkConfigInfo iNetworkConfigInfo) {
        mINetworkConfigInfo = iNetworkConfigInfo;
    }

    protected Retrofit getRetrofit(Class service) {
        String key = getBaseUrl() + service.getName();
        if (mCacheRetrofit.containsKey(key)) {
            return mCacheRetrofit.get(key);
        }

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(getBaseUrl());
        builder.client(getOkHttpClient());
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        Retrofit retrofit = builder.build();
        mCacheRetrofit.put(key, retrofit);
        return retrofit;
    }

    public <T> T getService(Class<T> service) {
        return getRetrofit(service).create(service);
    }
    private OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        if (null != getInterceptor()) {
            okBuilder.addInterceptor(getInterceptor());
        }

        okBuilder.addInterceptor(new CommonRequestInterceptor(mINetworkConfigInfo))
                .addInterceptor(new CommonResponseInterceptor());

        if (null != mINetworkConfigInfo && mINetworkConfigInfo.isDebug()) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okBuilder.addInterceptor(loggingInterceptor);
        }

        return okBuilder.build();
    }

    public <T> ObservableTransformer<T, T> applySchedulers(Observer<T> observer) {
        return new ObservableTransformer<T, T>() {
            @NonNull
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                Observable<T> observable = upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(getAppErrorHandler())
                        .onErrorResumeNext(new HttpErrorHandle<T>());
                observable.subscribe(observer);
                return observable;
            }
        };
    }

    protected abstract <T> Function<T, T> getAppErrorHandler();

    protected abstract String getBaseUrl();

    protected abstract Interceptor getInterceptor();
}
