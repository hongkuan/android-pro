package com.kuan.base.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.kuan.base.preference.BasicDataPreferenceUtil;
import com.kuan.base.utils.GenericUtils;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.List;

import androidx.annotation.CallSuper;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class MVVMBaseMode<N, O> implements MVVMDataObserver<N> {
    //和网络模块相关
    private CompositeDisposable mCompositeDisposable;
    //和业务model相关
    protected WeakReference<IBaseModeListener> mBaseModeListeners;
    //缓存相关=
    private String mCachedPreferencesKey;//缓存使用sp 所用的key 如果设置该值就代表使用缓存
    private String mApkPredefinedData;//预制数据
    protected BaseCachedData<N> mCacheData;
    //数据分页相关
    protected int mPageNumber;
    private final boolean isPaging;//是否分页
    private final int INIT_PAGE_NUMBER;//开始第一页pageNumber
    private boolean isLoading = false;

    public MVVMBaseMode(boolean isPaging, String cachePreferenceKey, String apkPredefinedData, int... initPageNumber) {
        this.isPaging = isPaging;
        this.mCachedPreferencesKey = cachePreferenceKey;
        this.mApkPredefinedData = apkPredefinedData;
        if (!TextUtils.isEmpty(mCachedPreferencesKey)) mCacheData = new BaseCachedData<>();
        INIT_PAGE_NUMBER = (null != initPageNumber && initPageNumber.length > 0) ? initPageNumber[0] : 0;
        mPageNumber = INIT_PAGE_NUMBER;
    }

    public boolean isPaging() {
        return isPaging;
    }

    public void addDisposable(Disposable d) {
        if (null == d) return;
        if (null == mCompositeDisposable) mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(d);
    }

    /**
     * 注册model监听
     *
     * @param listener
     */
    public void register(IBaseModeListener listener) {
        if (null != listener) mBaseModeListeners = new WeakReference<>(listener);
    }

    protected void saveDataToPreference(N data) {
        if (null != data) {
            mCacheData.updateTimeInMills = System.currentTimeMillis();
            mCacheData.data = data;
            BasicDataPreferenceUtil.getInstance().setString(mCachedPreferencesKey, new Gson().toJson(mCacheData));
        }
    }

    public void refresh() {
        if (!isLoading) {
            isLoading = true;
            if (isPaging) mPageNumber = INIT_PAGE_NUMBER;
            load();
        }
    }

    public void loadNextPage() {
        if (!isLoading) {
            isLoading = true;
            load();
        }
    }

    public abstract void load();

    @CallSuper
    public void cancel() {
        if (null != mCompositeDisposable && !mCompositeDisposable.isDisposed())
            mCompositeDisposable.dispose();
    }

    /**
     * 子类通过覆盖此方法控制缓存数据是否重新加载。
     *
     * @return
     */
    protected boolean isNeedToUpdate() {
        return true;
    }

    /**
     * 获取缓存和网络数据
     * 先判断是否有缓存
     * 有->拿缓存并跟进刷新逻辑是否那网络数据。
     * 缓存获取失败 拿预制数据 处理完预制数据之后 再获取网络数据 缓存 并且下次启动时优先使用缓存。
     */
    public void getCacheDataAndLoad() {
        isLoading = true;
        if (!TextUtils.isEmpty(mCachedPreferencesKey)) {
            //获取缓存
            String cache = BasicDataPreferenceUtil.getInstance().getString(mCachedPreferencesKey);
            if (!TextUtils.isEmpty(cache)) {
                try {
                    N data = new Gson().fromJson(new JSONObject(cache).getString("data"), (Class<N>) GenericUtils.getGenericType(MVVMBaseMode.this));
                    if (null != data) {
                        onSuccess(data, true);
                        if (isNeedToUpdate()) load();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //获取预制
            if (!TextUtils.isEmpty(mApkPredefinedData)) {
                try {
                    N data = new Gson().fromJson(mApkPredefinedData, (Class<N>) GenericUtils.getGenericType(MVVMBaseMode.this));
                    if (null != data) {
                        onSuccess(data, true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        load();
    }

    /**
     * 先通知监听 再缓存数据 (只缓存第一页数据)
     * @param networkResponseBean
     * @param data
     * @param isFromCache
     */
    protected void notifyResultToListeners(N networkResponseBean, O data, boolean isFromCache) {
        if (null != mBaseModeListeners) {
            IBaseModeListener listener = mBaseModeListeners.get();
            if (null != listener) {
                if (isPaging) {
                    PagingResult pagingResult = isFromCache ?
                            new PagingResult(true, false, true)
                            : new PagingResult(mPageNumber == INIT_PAGE_NUMBER, (null != data) ? ((List) data).isEmpty() : true, (null != data) ? ((List) data).size() > 0 : false);
                    listener.onLoadFinish(data, pagingResult);
                } else {
                    listener.onLoadFinish(data);
                }
            }
        }

        if (isPaging){
            if (mPageNumber == INIT_PAGE_NUMBER && !TextUtils.isEmpty(mCachedPreferencesKey) && !isFromCache) saveDataToPreference(networkResponseBean);
            if (!isFromCache) mPageNumber++;
        } else {
            if (!TextUtils.isEmpty(mCachedPreferencesKey) && !isFromCache)saveDataToPreference(networkResponseBean);
        }

        if (!isFromCache) isLoading = false;
    }


    protected void loadFailure(String errMsg){
        if (null != mBaseModeListeners) {
            IBaseModeListener listener = mBaseModeListeners.get();
            if (null != listener) {
                if (isPaging) listener.onLoadFail(errMsg, new PagingResult(mPageNumber == INIT_PAGE_NUMBER, true, false));
                else listener.onLoadFail(errMsg);
            }
        }
    }

    public boolean isLoading() {
        return isLoading;
    }
}
