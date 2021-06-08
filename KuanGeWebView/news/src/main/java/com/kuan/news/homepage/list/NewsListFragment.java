package com.kuan.news.homepage.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kuan.network.TecentNetworkApi;
import com.kuan.network.observer.BaseObserver;
import com.kuan.news.R;
import com.kuan.news.databinding.FragmentNewsBinding;
import com.kuan.news.homepage.api.INewsChannelApi;
import com.kuan.news.homepage.api.NewsListBean;
import com.kuan.news.homepage.base.BaseCustomViewModel;
import com.kuan.news.homepage.picturetitleview.PictureTitleViewModel;
import com.kuan.news.homepage.titleview.TitleViewModel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

public class NewsListFragment extends Fragment {
    private static final String TAG = "NewsListFragment";
    private static final String ARGS_KEY_CHANNEL_ID = "args_key_channel_id";
    private static final String ARGS_KEY_CHANNEL_NAME = "args_key_channel_name";
    private FragmentNewsBinding mBinding;
    private String mChannelId;
    private String mChannelName;
    private int mCurrPageNum = 1;
    private boolean isRefresh = false;

    private List<BaseCustomViewModel> mListData = new ArrayList<>();
    private NewsListAdapter mAdapter;

    public static NewsListFragment newInstance(String channelId, String channelName) {
        Bundle args = new Bundle();
        args.putString(ARGS_KEY_CHANNEL_ID, channelId);
        args.putString(ARGS_KEY_CHANNEL_NAME, channelName);
        NewsListFragment fragment = new NewsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_news, null, false);
        initData();
        mAdapter = new NewsListAdapter();
        mBinding.listview.setHasFixedSize(true);
        mBinding.listview.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.listview.setAdapter(mAdapter);
        mBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                isRefresh = true;
                mCurrPageNum = 1;
                load();
            }
        });
        mBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isRefresh = false;
                mCurrPageNum++;
                load();
            }
        });
        load();
        return mBinding.getRoot();
    }

    private void initData(){
        Bundle args = getArguments();
        mChannelId = args.getString(ARGS_KEY_CHANNEL_ID);
        mChannelName = args.getString(ARGS_KEY_CHANNEL_NAME);
    }

    private void load(){
        TecentNetworkApi.getInstance().getService(INewsChannelApi.class).getNewsList(mChannelId, mChannelName, Integer.toString(mCurrPageNum))
                .compose(TecentNetworkApi.getInstance().applySchedulers(new BaseObserver<NewsListBean>() {
                    @Override
                    protected void onSuccess(NewsListBean newsListBean) {
                        mBinding.refreshLayout.finishRefresh();
                        mBinding.refreshLayout.finishLoadMore();
                        if (isRefresh) mListData.clear();
                        if (null!= newsListBean && null != newsListBean.showapiResBody && null != newsListBean.showapiResBody.pagebean){
                            NewsListBean.Pagebean pageBean = newsListBean.showapiResBody.pagebean;

                            if (pageBean.contentlist.size()>0){
                                mCurrPageNum = pageBean.currentPage;
                                for (NewsListBean.Contentlist contentlist : pageBean.contentlist) {
                                    if(null != contentlist.imageurls && contentlist.imageurls.size()>0){
                                        PictureTitleViewModel model = new PictureTitleViewModel();
                                        model.avatarUrl = contentlist.imageurls.get(0).url;
                                        model.title = contentlist.title;
                                        model.link = contentlist.link;
                                        mListData.add(model);
                                    }else {
                                        TitleViewModel model = new TitleViewModel();
                                        model.title = contentlist.title;
                                        model.link = contentlist.link;
                                        mListData.add(model);
                                    }
                                }
                                mAdapter.setData(mListData);
                            }
                        }
                    }

                    @Override
                    protected void onFail(Throwable e) {

                    }
                }));
    }
}
