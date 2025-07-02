package com.example.javahybridsample.activity.sample;

import android.os.Bundle;
import android.webkit.WebChromeClient;

import androidx.lifecycle.ViewModelProvider;

import com.example.javahybridsample.activity.base.BaseActivity;
import com.example.javahybridsample.databinding.ActivitySampleWebviewBinding;
import com.example.javahybridsample.web.WebBridge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 샘플 웹뷰를 띄우고 브릿지를 연결하는 액티비티입니다.
 * 작성자: banseogg
 */
public class SampleWebviewActivity extends BaseActivity {
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    private ActivitySampleWebviewBinding binding;
    private SampleViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySampleWebviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String url = getIntent().getStringExtra("url");

        viewModel = new ViewModelProvider(this).get(SampleViewModel.class);

        WebBridge webBridge = new WebBridge(binding.webView, this);

        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.setWebContentsDebuggingEnabled(true);
        binding.webView.setWebChromeClient(new WebChromeClient());
        binding.webView.addJavascriptInterface(webBridge, WebBridge.DEFAULT_WEB_BRIDGE_NAME);
        binding.webView.loadUrl(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.webView.destroy();
    }
}
