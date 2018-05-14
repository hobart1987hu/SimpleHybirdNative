package com.simple.hybirdnative;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.hobart.hybirdnative.INVOKE;
import org.hobart.hybirdnative.SWebViewDelegate;
import org.hobart.hybirdnative.WebViewCallBack;
import org.hobart.hybirdnative.widget.DefaultSWebChromeClient;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private SWebViewDelegate mSWebViewDelegate;
    private LinearLayout webContainer;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webContainer = (LinearLayout) findViewById(R.id.webContainer);

        mSWebViewDelegate = new SWebViewDelegate(this, new WebViewCallBack() {
            @Override
            public void showWebView() {

            }

            @Override
            public void onReceivedTitle(String title) {

            }
        }, null, null, new FunctionSet1());
        mSWebViewDelegate.setWebChromeClient(new DefaultSWebChromeClient(mSWebViewDelegate));
        webContainer.addView(mSWebViewDelegate.getWebView());
        mUrl = "file:///android_asset/test_h5.html";
        mSWebViewDelegate.loadUrl(mUrl);
    }

    public class FunctionSet1 {
        @INVOKE("popToast")
        public void popToast(JSONObject paras) {
            String para1 = paras.optString("para1");
            Toast.makeText(MainActivity.this, para1, Toast.LENGTH_SHORT).show();
        }

        @INVOKE("refresh")
        public void refreshWebView(JSONObject paras) {
            mSWebViewDelegate.loadUrl(mUrl);
        }

        @INVOKE("closeCurrentActivity")
        public void closeCurrentActivity(JSONObject paras) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSWebViewDelegate.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSWebViewDelegate.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mSWebViewDelegate.getWebView().canGoBack()) {
                mSWebViewDelegate.getWebView().goBack();
                return true;
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
