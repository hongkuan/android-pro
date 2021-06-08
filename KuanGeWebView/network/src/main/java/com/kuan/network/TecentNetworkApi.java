package com.kuan.network;

import com.kuan.network.base.INetworkConfigInfo;
import com.kuan.network.base.NetworkApi;
import com.kuan.network.beans.TecentBaseResponse;
import com.kuan.network.commoninterceptor.CommonRequestInterceptor;
import com.kuan.network.commoninterceptor.CommonResponseInterceptor;
import com.kuan.network.error.ExceptionHandle;
import com.kuan.network.error.HttpErrorHandle;
import com.kuan.network.utils.TecentUtil;

import java.io.IOException;
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
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class TecentNetworkApi extends NetworkApi {
    private static volatile TecentNetworkApi sInstance = null;

    public static TecentNetworkApi getInstance(){
        if (null == sInstance){
            synchronized (TecentNetworkApi.class){
                if (null == sInstance) sInstance = new TecentNetworkApi();
            }
        }
        return sInstance;
    }

    private TecentNetworkApi(){

    }

    public static <T> T createService(Class<T> service){
        return getInstance().getRetrofit(service).create(service);
    }

    @Override
    protected   <T> Function<T,T> getAppErrorHandler(){
        return new Function<T, T>() {
            @Override
            public T apply(@NonNull T response) throws Exception {
                if (response instanceof TecentBaseResponse && ((TecentBaseResponse) response).showapiResCode != 0){
                    ExceptionHandle.ServerException serverException = new ExceptionHandle.ServerException();
                    serverException.code = ((TecentBaseResponse) response).showapiResCode;
                    serverException.message = ((TecentBaseResponse) response).showapiResError;
                    throw  serverException;
                }

                return response;
            }
        };
    }

    @Override
    protected String getBaseUrl() {
        return "http://service-o5ikp40z-1255468759.ap-shanghai.apigateway.myqcloud.com/";
    }

    @Override
    protected Interceptor getInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                builder.addHeader("source","source");
                String time = TecentUtil.getTimeStr();
                builder.addHeader("Authorization",TecentUtil.getAuthorization(time));
                builder.addHeader("Date",time);
                return chain.proceed(builder.build());
            }
        };
    }
}
