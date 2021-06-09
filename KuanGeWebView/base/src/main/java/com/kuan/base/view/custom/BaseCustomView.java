package com.kuan.base.view.custom;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BaseCustomView <T extends ViewDataBinding, S extends BaseCustomViewModel> extends LinearLayout implements ICustomView<S>, View.OnClickListener {
    protected T mBinding;
    protected S mModel;

    public BaseCustomView(Context context) {
        super(context);
        init();
    }

    public BaseCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseCustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void init(){
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (getViewLayoutId() == 0) return;
        mBinding = DataBindingUtil.inflate(layoutInflater,getViewLayoutId(), null, false);
        mBinding.getRoot().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onRootClick(v);
            }
        });
        addView(mBinding.getRoot());
    }

    @Override
    public void setData(S data) {
        mModel = data;
        setDataToView(data);
        if (null != mBinding) mBinding.executePendingBindings();

        onDataUpdated();
    }

    @Override
    public void onClick(View v) {

    }

    protected void onDataUpdated(){

    }

    public T getDataBinding() {
        return mBinding;
    }

    public S getViewModel() {
        return mModel;
    }

    protected abstract int getViewLayoutId();

    protected abstract void onRootClick(View view);

    protected abstract void setDataToView(S data);

}
