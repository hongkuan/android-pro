package com.kuan.webview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.kuan.webview.databinding.ActivityWebviewBinding;
import com.kuan.webview.utils.Constants;

/**
 * Created by hongkuan on 2021-05-08 0008.
 */
public class WebViewActivity extends AppCompatActivity {

    private ActivityWebviewBinding mViewBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_webview);
        Intent intent = getIntent();
        mViewBinding.title.setText(intent.getStringExtra(Constants.TITLE));
        mViewBinding.actionBar.setVisibility(intent.getBooleanExtra(Constants.IS_SHOW_ACTION_BAR,
                true) ? View.VISIBLE : View.GONE);

        mViewBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = WebViewFragment.newInstance(intent.getStringExtra(Constants.URL), true);
        fragmentManager.beginTransaction().add(R.id.webview_fragment, fragment).commit();
    }

    public void updateTitle(String title) {
        mViewBinding.title.setText(title);
    }

}
