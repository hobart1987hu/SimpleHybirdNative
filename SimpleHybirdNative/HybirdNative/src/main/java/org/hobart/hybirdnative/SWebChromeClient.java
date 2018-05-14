package org.hobart.hybirdnative;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebStorage;
import android.webkit.WebView;

import org.hobart.hybirdnative.util.FileUtils;
import org.json.JSONException;

import java.io.File;

/**
 * Created by huzeyin on 2018/5/14.
 */

public class SWebChromeClient extends WebChromeClient {

    private static final String TAG = "SWebChromeClient";

    protected SWebViewDelegate mSWebViewDelegate;

    public SWebChromeClient(SWebViewDelegate delegate) {
        mSWebViewDelegate = delegate;
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        Log.d(TAG, consoleMessage.message() + " -- From line "
                + consoleMessage.lineNumber() + " of "
                + consoleMessage.sourceId());
        return super.onConsoleMessage(consoleMessage);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        Log.d(TAG, "newProgress->>" + newProgress);
        if (newProgress > 10) {
            if (mSWebViewDelegate.getWebViewCallBack() != null) {
                mSWebViewDelegate.getWebViewCallBack().showWebView();
            }
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        if (mSWebViewDelegate.getWebViewCallBack() != null) {
            mSWebViewDelegate.getWebViewCallBack().onReceivedTitle(title);
        }
        super.onReceivedTitle(view, title);
    }

    @Override
    public void onReachedMaxAppCacheSize(long requiredStorage, long quota, WebStorage.QuotaUpdater quotaUpdater) {
        try {
            if (!TextUtils.isEmpty(mSWebViewDelegate.getWebCache()))
                FileUtils.delete(new File(mSWebViewDelegate.getWebCache()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        try {
            mSWebViewDelegate.handleJsPrompt(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        result.confirm();
        return true;
    }
}
