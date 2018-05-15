package com.simple.hybirdnative;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.widget.Toast;

import org.hobart.hybirdnative.DefaultSWebChromeClient;
import org.hobart.hybirdnative.INVOKE;
import org.hobart.hybirdnative.widget.SimpleWrappedWebView;
import org.json.JSONObject;

/**
 * Created by huzeyin on 2018/5/15.
 */

public class DefaultLoadingActivity extends Activity {

    private SimpleWrappedWebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_webview);
        webView = (SimpleWrappedWebView) findViewById(R.id.webView);
        webView.setFunctions(new FunctionSet1());
        webView.setWebChromeClient(new DefaultSWebChromeClient(webView.getSWebViewDelegate()));
        webView.loadUrl("file:///android_asset/test_h5.html");
    }

    public class FunctionSet1 {
        @INVOKE("popToast")
        public void popToast(JSONObject paras) {
            String para1 = paras.optString("para1");
            Toast.makeText(DefaultLoadingActivity.this, para1, Toast.LENGTH_SHORT).show();
        }

        @INVOKE("refresh")
        public void refreshWebView(JSONObject paras) {
            webView.loadUrl(webView.getUrl());
        }

        @INVOKE("closeCurrentActivity")
        public void closeCurrentActivity(JSONObject paras) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
