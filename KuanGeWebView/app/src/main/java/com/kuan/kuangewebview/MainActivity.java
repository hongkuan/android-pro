package com.kuan.kuangewebview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kuan.base.autoservice.KuanGeServiceLoader;
import com.kuan.common.autoservice.IWebViewService;
import com.kuan.webview.WebViewActivity;

import java.util.Iterator;
import java.util.ServiceLoader;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.start_webview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IWebViewService serviceLoader = KuanGeServiceLoader.load(IWebViewService.class);
                if (null == serviceLoader) return;
                //serviceLoader.startWebViewActivity(MainActivity.this, "https://www.baidu.com","百度", true);
                serviceLoader.startDemoHtml(MainActivity.this);
                //startActivity(new Intent(MainActivity.this, WebViewActivity.class));
            }
        });
    }
}