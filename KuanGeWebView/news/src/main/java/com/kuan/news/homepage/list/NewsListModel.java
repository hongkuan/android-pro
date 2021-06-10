package com.kuan.news.homepage.list;

import android.util.Log;

import com.google.gson.Gson;
import com.kuan.base.model.IBaseModeListener;
import com.kuan.base.model.MVVMBaseMode;
import com.kuan.base.model.PagingResult;
import com.kuan.base.view.custom.BaseCustomViewModel;
import com.kuan.common.views.picturetitleview.PictureTitleViewModel;
import com.kuan.common.views.titleview.TitleViewModel;
import com.kuan.network.TecentNetworkApi;
import com.kuan.network.observer.BaseObserver;
import com.kuan.news.homepage.api.INewsChannelApi;
import com.kuan.news.homepage.api.NewsListBean;

import java.util.ArrayList;
import java.util.List;

public class NewsListModel extends MVVMBaseMode<NewsListBean, List<BaseCustomViewModel>> {
    private static final String TAG = "NewsListModel";
    private static final String CACHE_KEY = "cache_key_";
    private String mChannelId;
    private String mChannelName;
    private boolean isRefresh;
    private int mCurrPageNum = 1;

    public NewsListModel(String channelId, String channelName) {
        super(true, CACHE_KEY+ channelId, null, 1);
        this.mChannelId = channelId;
        this.mChannelName = channelName;
    }

    public void load(){
        TecentNetworkApi.getInstance().getService(INewsChannelApi.class).getNewsList(mChannelId, mChannelName, Integer.toString(mCurrPageNum))
                .compose(TecentNetworkApi.getInstance().applySchedulers(new BaseObserver<NewsListBean>(this, this)));
    }

    @Override
    public void onSuccess(NewsListBean newsListBean, boolean isFromCache) {
        Log.d(TAG, "onSuccess: data->" + new Gson().toJson(newsListBean));
        List<BaseCustomViewModel> data = new ArrayList<>();
        if (null!= newsListBean && null != newsListBean.showapiResBody && null != newsListBean.showapiResBody.pagebean){
            NewsListBean.Pagebean pageBean = newsListBean.showapiResBody.pagebean;
            PagingResult pagingResult = null;

            if (pageBean.contentlist.size()>0){
                mCurrPageNum = pageBean.currentPage;
                for (NewsListBean.Contentlist contentlist : pageBean.contentlist) {
                    if(null != contentlist.imageurls && contentlist.imageurls.size()>0){
                        PictureTitleViewModel model = new PictureTitleViewModel();
                        model.avatarUrl = contentlist.imageurls.get(0).url;
                        model.title = contentlist.title;
                        model.link = contentlist.link;
                        data.add(model);
                    }else {
                        TitleViewModel model = new TitleViewModel();
                        model.title = contentlist.title;
                        model.link = contentlist.link;
                        data.add(model);
                    }
                }
            }
        }

        notifyResultToListeners(newsListBean, data, isFromCache);
    }

    @Override
    public void onFailure(Throwable e) {
        e.printStackTrace();
        loadFailure(e.getMessage());
    }
}
