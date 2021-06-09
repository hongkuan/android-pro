package com.kuan.base.view.recycler;

import android.view.View;

import com.kuan.base.view.custom.BaseCustomViewModel;
import com.kuan.base.view.custom.ICustomView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BaseViewHolder extends RecyclerView.ViewHolder {
    protected ICustomView view;
    public BaseViewHolder(@NonNull ICustomView itemView) {
        super((View) itemView);
        this.view = itemView;
    }

    public void bind(BaseCustomViewModel model){
        view.setData(model);
    }
}
