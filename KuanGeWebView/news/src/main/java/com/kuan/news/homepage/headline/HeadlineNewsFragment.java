package com.kuan.news.homepage.headline;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.kuan.network.TecentNetworkApi;
import com.kuan.network.observer.BaseObserver;
import com.kuan.news.R;
import com.kuan.news.databinding.FragmentHomeBinding;
import com.kuan.news.homepage.api.INewsChannelApi;
import com.kuan.news.homepage.api.NewsChannelsBean;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

public class HeadlineNewsFragment extends Fragment {

    private FragmentHomeBinding mBinding;
    private HeadlineNewsFragmentAdapter mAdapter;

    public static HeadlineNewsFragment newInstance() {
        Bundle args = new Bundle();
        HeadlineNewsFragment fragment = new HeadlineNewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, null, false);
        mAdapter = new HeadlineNewsFragmentAdapter(getChildFragmentManager());
        mBinding.tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mBinding.viewPager.setAdapter(mAdapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
        mBinding.viewPager.setOffscreenPageLimit(1);
        loadData();
        return mBinding.getRoot();
    }

    private void loadData() {
        TecentNetworkApi.getInstance().getService(INewsChannelApi.class).getNewsChannels()
                .compose(
                        TecentNetworkApi.getInstance()
                                .applySchedulers(new BaseObserver<NewsChannelsBean>() {
            @Override
            protected void onSuccess(NewsChannelsBean newsChannelsBean) {
                if (newsChannelsBean != null
                        && null != newsChannelsBean.showapiResBody.channelList
                        && newsChannelsBean.showapiResBody.channelList.size() > 0) {
                    List<HeadlineNewsFragmentAdapter.ChannelItem> data = new ArrayList<>();
                    for (NewsChannelsBean.ChannelList channelList : newsChannelsBean.showapiResBody.channelList) {
                        HeadlineNewsFragmentAdapter.ChannelItem item = new HeadlineNewsFragmentAdapter.ChannelItem();
                        item.channelId = channelList.channelId;
                        item.channelName = channelList.name;
                        data.add(item);
                    }
                    mAdapter.setData(data);
                }
            }

            @Override
            protected void onFail(Throwable e) {

            }
        }));
    }
}
