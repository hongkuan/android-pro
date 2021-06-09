package com.kuan.news.homepage.list;

import android.view.ViewGroup;

import com.kuan.base.view.custom.BaseCustomViewModel;
import com.kuan.base.view.custom.ICustomView;
import com.kuan.base.view.recycler.BaseViewHolder;
import com.kuan.common.views.picturetitleview.PictureTitleView;
import com.kuan.common.views.picturetitleview.PictureTitleViewModel;
import com.kuan.common.views.titleview.TitleView;
import com.kuan.common.views.titleview.TitleViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsListAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<BaseCustomViewModel> mData;
    private final int VIEW_TYPE_PICTURE_TITLE = 1;
    private final int VIEW_TYPE_TITLE = 2;

    public void setData(List<BaseCustomViewModel> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ICustomView view = null;
        if (viewType == VIEW_TYPE_PICTURE_TITLE) view = new PictureTitleView(parent.getContext());
        if (viewType == VIEW_TYPE_TITLE) view = new TitleView(parent.getContext());
        if (null == view)return null;
        return new BaseViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        BaseCustomViewModel mode = mData.get(position);
        if (mode instanceof PictureTitleViewModel)return VIEW_TYPE_PICTURE_TITLE;
        if (mode instanceof TitleViewModel) return VIEW_TYPE_TITLE;
        return -1;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        if (null != mData && mData.size()>0)return mData.size();
        return 0;
    }

}
