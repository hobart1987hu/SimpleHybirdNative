package org.hobart.hybirdnative.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;

import org.hobart.hybirdnative.SWebViewDelegate;
import org.hobart.hybirdnative.WebViewCallBack;
import org.hobart.hybirdnative.loading.SLoadingErrorView;
import org.hobart.hybirdnative.loading.SLoadingView;

/**
 * Created by huzeyin on 2018/5/15.
 */

public class SimpleWrappedWebView extends FrameLayout {

    private SWebViewDelegate mSWebViewDelegate;
    private String mUrl;

    public SimpleWrappedWebView(@NonNull Context context) {
        super(context);
        init();
    }

    public SimpleWrappedWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SimpleWrappedWebView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mSWebViewDelegate = new SWebViewDelegate(getContext(), null);
        addView(mSWebViewDelegate.getWebView());
    }

    public String getUrl() {
        return mUrl;
    }

    public SWebViewDelegate getSWebViewDelegate() {
        return mSWebViewDelegate;
    }

    public void setWebChromeClient(WebChromeClient webChromeClient) {
        mSWebViewDelegate.setWebChromeClient(webChromeClient);
    }

    public void setWebViewCallBack(WebViewCallBack callBack) {
        mSWebViewDelegate.setWebViewCallBack(callBack);
    }

    public void setFunctions(Object... owners) {
        mSWebViewDelegate.setFunctions(owners);
    }

    public void setLoadingView(SLoadingView loadingView) {
        addView((View) loadingView);
        mSWebViewDelegate.setLoadingView(loadingView);
    }

    public void setLoadingErrorView(SLoadingErrorView errorView) {
        addView((View) errorView);
        mSWebViewDelegate.setSLoadingErrorView(errorView);
    }

    public void loadUrl(String url) {
        mUrl = url;
        mSWebViewDelegate.loadUrl(url);
    }

    public void onPause() {
        mSWebViewDelegate.onPause();
    }

    public void onResume() {
        mSWebViewDelegate.onResume();
    }

    public void onDestroy() {
        removeAllViews();
        mSWebViewDelegate.onDestroy();
    }

    public void nativeCallJS(String javascript) {
        mSWebViewDelegate.nativeCallJS(javascript);
    }

    public void addJavascriptInterface(Object object, String name) {
        mSWebViewDelegate.addJavascriptInterface(object, name);
    }

    public boolean canGoBack() {
        return mSWebViewDelegate.getWebView().canGoBack();
    }

    public void goBack() {
        mSWebViewDelegate.getWebView().goBack();
    }
}
