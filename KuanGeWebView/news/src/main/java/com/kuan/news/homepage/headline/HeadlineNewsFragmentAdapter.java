package com.kuan.news.homepage.headline;

import com.kuan.news.homepage.list.NewsListFragment;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class HeadlineNewsFragmentAdapter extends FragmentPagerAdapter {

    private List<ChannelsModel.Channel> mData = new ArrayList<>();

    public HeadlineNewsFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public void setData(List<ChannelsModel.Channel> data){
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (mData.size()> position) return  mData.get(position).channelName;
        return null;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (mData.size()> position) return NewsListFragment.newInstance(mData.get(position).channelId, mData.get(position).channelName);
        return null;
    }

    @Override
    public int getCount() {
        return mData.size();
    }
}
