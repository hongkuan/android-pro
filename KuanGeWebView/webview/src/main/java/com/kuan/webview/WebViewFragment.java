package com.kuan.webview;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.kuan.base.loadsir.ErrorCallback;
import com.kuan.base.loadsir.LoadingCallback;
import com.kuan.webview.databinding.FragmentWebviewBinding;
import com.kuan.webview.utils.Constants;
import com.kuan.webview.webviewprocess.webchromeclient.KuanGeWebChromeClient;
import com.kuan.webview.webviewprocess.webviewclient.KuanGeWebViewClient;
import com.kuan.webview.webviewprocess.webviewsetting.WebViewDefaultSettings;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


/**
 * Created by hongkuan on 2021-05-08 0008.
 */
public class WebViewFragment extends Fragment implements WebViewCallback, OnRefreshListener {
    private static final String TAG = "WebViewFragment";
    private FragmentWebviewBinding mViewBinding;
    private String mUrl;
    private boolean mCanNativeRefresh;
    private LoadService mLoadService;
    private boolean isError = false;

    public static WebViewFragment newInstance(String url, boolean canNativeRefresh) {
        Bundle args = new Bundle();
        args.putString(Constants.URL, url);
        args.putBoolean(Constants.CAN_NATIVE_REFRESH, canNativeRefresh);
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (null != args) {
            mUrl = args.getString(Constants.URL);
            mCanNativeRefresh = args.getBoolean(Constants.CAN_NATIVE_REFRESH);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_webview, null, false);
        mViewBinding.webview.setWebViewCallback(this);
        mViewBinding.webview.loadUrl(mUrl);
        mLoadService = LoadSir.getDefault().register(mViewBinding.smartRefresh,
                new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                mLoadService.showCallback(LoadingCallback.class);
                mViewBinding.webview.reload();
            }
        });

        mViewBinding.smartRefresh.setOnRefreshListener(this);
        mViewBinding.smartRefresh.setEnableRefresh(mCanNativeRefresh);
        mViewBinding.smartRefresh.setEnableLoadMore(false);
        return mLoadService.getLoadLayout();
    }

    @Override
    public void onPageStart(String url) {
        if (null != mLoadService) {
            mLoadService.showCallback(LoadingCallback.class);
        }
    }

    @Override
    public void onPageFinished(String url) {
        if (isError) {
            mViewBinding.smartRefresh.setEnableRefresh(true);
        } else {
            mViewBinding.smartRefresh.setEnableRefresh(mCanNativeRefresh);
        }
        Log.d(TAG, "onPageFinished");

        mViewBinding.smartRefresh.finishRefresh();

        if (null != mLoadService) {
            if (isError) {
                mLoadService.showCallback(ErrorCallback.class);
            } else {
                mLoadService.showSuccess();
            }
        }
        isError = false;
    }

    @Override
    public void onPageError() {
        Log.e(TAG, "onPageError");
        isError = true;
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mViewBinding.webview.reload();
    }

    @Override
    public void updateTitle(String title) {
        if (getActivity() instanceof WebViewActivity){
            ((WebViewActivity) getActivity()).updateTitle(title);
        }
    }
}
