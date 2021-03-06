package org.hobart.hybirdnative;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.hobart.hybirdnative.interceptor.CommonInterceptor;
import org.hobart.hybirdnative.loading.SLoadingErrorView;
import org.hobart.hybirdnative.loading.SLoadingView;
import org.hobart.hybirdnative.widget.FixedWebView;

/**
 * Created by huzeyin on 2018/5/14.
 */

public class SWebViewClient extends WebViewClient {

    private static final String TAG = "SWebViewClient";

    private Context mContext;
    private boolean isCurrentlyLoading;
    private boolean mIsError;
    private SLoadingView mLoadingView;
    private SLoadingErrorView mErrorView;
    private String mLoadUrl;
    private CommonInterceptor mInterceptor;

    public void setLoadingView(SLoadingView loadingView) {

        mLoadingView = loadingView;
    }

    public void setLoadingErrorView(SLoadingErrorView errorView) {

        mErrorView = errorView;
    }

    public SWebViewClient(Context context) {
        this(context, null, null);
    }

    public SWebViewClient(Context context, SLoadingView loadingView, SLoadingErrorView errorView) {
        mContext = context;
        mLoadingView = loadingView;
        mErrorView = errorView;
        mInterceptor = new CommonInterceptor(context);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //TODO:如果加载的不是本地文件，需要判断网络问题
        if (!isWebViewValid(view)) {
            return true;
        }
        if (mInterceptor.interceptorUrl(url)) {
            return true;
        }
        Log.d(TAG, "---shouldOverrideUrlLoading----load url : ->" + url);
        view.loadUrl(url);
        return true;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if (Build.VERSION.SDK_INT >= 21) {
            final String url = request.getUrl().toString();
            if (mInterceptor.interceptorUrl(url)) {
                return true;
            }
            Log.d(TAG, "---shouldOverrideUrlLoading----load url : ->" + url);
            view.loadUrl(url);
            return true;
        }
        return super.shouldOverrideUrlLoading(view, request);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (!isWebViewValid(view)) {
            return;
        }
        //TODO: 这边网络问题判断处理
//        if (mContext != null && !NetworkUtil.isNetworkAvailable(mContext)) {
//            mIsError = true;
//            if (null != mErrorView)
//                mErrorView.show();
//            return;
//        }

        Log.d(TAG, "----onPageStarted load web view ----");
        mLoadUrl = url;
        mIsError = false;
        startTime = System.currentTimeMillis();
        isCurrentlyLoading = true;
        if (null != mLoadingView)
            mLoadingView.show();
        if (null != mErrorView)
            mErrorView.hide();
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        Log.d(TAG, "----onPageFinished ----");

        if (SWConfig.DEBUG_LOADING) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (null != mLoadingView)
                        mLoadingView.hide();
                }
            }, 1 * 1000);
        } else {
            if (null != mLoadingView)
                mLoadingView.hide();
        }

        if (!isCurrentlyLoading && !url.startsWith("about:")) {
            return;
        }
        isCurrentlyLoading = false;

        if (!isWebViewValid(view)) {
            return;
        }

        if (!mIsError) {
            statistics();
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        if (!isCurrentlyLoading) {
            return;
        }
        super.onReceivedError(view, errorCode, description, failingUrl);
        if (!isWebViewValid(view)) {
            return;
        }
        mIsError = true;
        view.loadUrl("about:blank");
        if (null != mLoadingView)
            mLoadingView.hide();
        if (null != mErrorView)
            mErrorView.show();
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        //TODO:在这里可以根据情况进行处理
        mIsError = true;
        handler.proceed();
        super.onReceivedSslError(view, handler, error);
    }

    private boolean isWebViewValid(WebView view) {
        if (view != null && !((FixedWebView) view).isDestroy()) {
            return true;
        }
        return false;
    }

    private long startTime;

    private void statistics() {
        if (!isValidStatistics(mLoadUrl)) {
            return;
        }
        Log.d(TAG, "加载本次url:=>" + mLoadUrl + "\n 耗时" + ((System.currentTimeMillis() - startTime) / 1000.0f) + "秒");
    }

    private boolean isValidStatistics(String url) {
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        if (url.equals("about:blank")) {
            return false;
        }
        return true;
    }
}
