package com.kuan.kuangeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kuan.base.autoservice.KuanGeServiceLoader;
import com.kuan.common.autoservice.INetwrokService;
import com.kuan.common.autoservice.IWebViewService;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.start_webview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IWebViewService service = KuanGeServiceLoader.load(IWebViewService.class);
                if (null != service){
                    service.startWebViewActivity(MainActivity.this, "https://www.baidu.com","百度", true);
                }
            }
        });

        findViewById(R.id.start_network).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                INetwrokService netwrokService = KuanGeServiceLoader.load(INetwrokService.class);
                netwrokService.getNewChannels();
            }
        });
    }
}