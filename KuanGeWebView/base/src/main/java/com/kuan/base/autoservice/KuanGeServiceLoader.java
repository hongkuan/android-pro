package com.kuan.base.autoservice;

import android.os.Looper;
import android.util.Log;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * Created by hongkuan on 2021-05-08 0008.
 */
public class KuanGeServiceLoader {
    private static final String TAG = "KuanGeServiceLoader";
    private KuanGeServiceLoader(){

    }

    public static <S> S load(Class<S> service){
        try {
            ServiceLoader<S> serviceLoader = ServiceLoader.load(service);
            Iterator<S> serviceIt = serviceLoader.iterator();
            if( serviceIt.hasNext()){
                S s = serviceIt.next();
                return s;
            }
        }catch (Exception e){
            Log.e(TAG, "load: ", e);
        }
        return null;
    }
}
