package com.kuan.news.homepage.list;

import android.view.View;
import android.view.ViewGroup;

import com.kuan.news.homepage.base.BaseCustomViewModel;
import com.kuan.news.homepage.base.ICustomView;
import com.kuan.news.homepage.picturetitleview.PictureTitleView;
import com.kuan.news.homepage.picturetitleview.PictureTitleViewModel;
import com.kuan.news.homepage.titleview.TitleView;
import com.kuan.news.homepage.titleview.TitleViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    private List<BaseCustomViewModel> mData;
    private final int VIEW_TYPE_PICTURE_TITLE = 1;
    private final int VIEW_TYPE_TITLE = 2;

    public void setData(List<BaseCustomViewModel> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ICustomView view = null;
        if (viewType == VIEW_TYPE_PICTURE_TITLE) view = new PictureTitleView(parent.getContext());
        if (viewType == VIEW_TYPE_TITLE) view = new TitleView(parent.getContext());
        if (null == view)return null;
        return new ViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        BaseCustomViewModel mode = mData.get(position);
        if (mode instanceof PictureTitleViewModel)return VIEW_TYPE_PICTURE_TITLE;
        if (mode instanceof TitleViewModel) return VIEW_TYPE_TITLE;
        return -1;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        if (null != mData && mData.size()>0)return mData.size();
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final ICustomView view;

        public ViewHolder(@NonNull ICustomView itemView) {
            super((View) itemView);
            this.view = itemView;
        }

        public void bind(BaseCustomViewModel model){
            this.view.setData(model);
        }
    }
}
