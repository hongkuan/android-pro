package com.kuan.news.homepage.headline;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.kuan.base.model.IBaseModeListener;
import com.kuan.base.model.PagingResult;
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

public class HeadlineNewsFragment extends Fragment implements IBaseModeListener<List<ChannelsModel.Channel>> {
    private static final String TAG = "HeadlineNewsFragment";
    private FragmentHomeBinding mBinding;
    private HeadlineNewsFragmentAdapter mAdapter;
    private ChannelsModel mChannelsModel;

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

        mChannelsModel = new ChannelsModel();
        mChannelsModel.register(this);
        mChannelsModel.getCacheDataAndLoad();
        return mBinding.getRoot();
    }

    @Override
    public void onLoadFinish(List<ChannelsModel.Channel> data, PagingResult... pagingResults) {
        mAdapter.setData(data);
        mBinding.tabLayout.selectTab(mBinding.tabLayout.getTabAt(mBinding.tabLayout.getSelectedTabPosition()));
    }

    @Override
    public void onLoadFail(String errMsg, PagingResult... pagingResults) {
        Log.e(TAG, "onLoadFail: errMsg->"+errMsg);
        Toast.makeText(getContext(), errMsg, Toast.LENGTH_SHORT).show();
    }
}
