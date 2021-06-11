package com.kuan.news.homepage.list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kuan.base.model.IBaseModeListener;
import com.kuan.base.model.PagingResult;
import com.kuan.network.TecentNetworkApi;
import com.kuan.network.observer.BaseObserver;
import com.kuan.news.R;
import com.kuan.news.databinding.FragmentNewsBinding;
import com.kuan.news.homepage.api.INewsChannelApi;
import com.kuan.news.homepage.api.NewsListBean;
import com.kuan.base.view.custom.BaseCustomViewModel;
import com.kuan.common.views.picturetitleview.PictureTitleViewModel;
import com.kuan.common.views.titleview.TitleViewModel;
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

public class NewsListFragment extends Fragment implements IBaseModeListener<List<BaseCustomViewModel>> {
    private static final String TAG = "NewsListFragment";
    private static final String ARGS_KEY_CHANNEL_ID = "args_key_channel_id";
    private static final String ARGS_KEY_CHANNEL_NAME = "args_key_channel_name";
    private FragmentNewsBinding mBinding;
    private String mChannelId;
    private String mChannelName;

    private List<BaseCustomViewModel> mListData = new ArrayList<>();
    private NewsListAdapter mAdapter;
    private NewsListModel mNewsListModel = null;

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
        mNewsListModel = new NewsListModel(mChannelId, mChannelName);
        mNewsListModel.register(this);
        mAdapter = new NewsListAdapter();
        mBinding.listview.setHasFixedSize(true);
        mBinding.listview.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.listview.setAdapter(mAdapter);
        mBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mNewsListModel.refresh();
            }
        });
        mBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mNewsListModel.loadNextPage();
            }
        });
        mNewsListModel.getCacheDataAndLoad();
        return mBinding.getRoot();
    }

    private void initData(){
        Bundle args = getArguments();
        mChannelId = args.getString(ARGS_KEY_CHANNEL_ID);
        mChannelName = args.getString(ARGS_KEY_CHANNEL_NAME);
    }

    @Override
    public void onLoadFinish(List<BaseCustomViewModel> data, PagingResult... pagingResults) {
        mBinding.refreshLayout.finishRefresh();
        mBinding.refreshLayout.finishLoadMore();
        if (pagingResults.length > 0 && pagingResults[0].isFirstPage){
            mListData.clear();
        }

        mListData.addAll(data);
        mAdapter.setData(mListData);
    }

    @Override
    public void onLoadFail(String errMsg, PagingResult... pagingResults) {
        Log.e(TAG, "onLoadFail: errMsg->"+errMsg);
        mBinding.refreshLayout.finishRefresh();
        mBinding.refreshLayout.finishLoadMore();
        Toast.makeText(getContext(), errMsg, Toast.LENGTH_SHORT).show();
    }
}
