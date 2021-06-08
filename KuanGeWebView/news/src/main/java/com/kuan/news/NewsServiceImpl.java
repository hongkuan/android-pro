package com.kuan.news;

import com.google.auto.service.AutoService;
import com.kuan.common.autoservice.INewsService;
import com.kuan.news.homepage.headline.HeadlineNewsFragment;

import androidx.fragment.app.Fragment;

@AutoService(INewsService.class)
public class NewsServiceImpl implements INewsService{

    @Override
    public Fragment getHomeFragment() {
        return HeadlineNewsFragment.newInstance();
    }
}
