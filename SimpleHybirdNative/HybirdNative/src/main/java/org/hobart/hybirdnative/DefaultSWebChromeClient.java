package org.hobart.hybirdnative;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * Created by huzeyin on 2018/5/14.
 */

public class DefaultSWebChromeClient extends SWebChromeClient {
    private ProgressBar progressBar = null;
    private int barHeight = 4;
    private boolean isAdd = false;

    public DefaultSWebChromeClient(SWebViewDelegate delegate) {
        super(delegate);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        if (newProgress > 80) {
            if (progressBar != null)
                progressBar.setVisibility(View.GONE);
        }
        if (newProgress == 100) {
            if (progressBar != null)
                progressBar.setVisibility(View.GONE);
        } else {
            if (!isAdd) {
                progressBar = (ProgressBar) LayoutInflater.from(mSWebViewDelegate.getContext()).inflate(R.layout.progress_horizontal, null);
                progressBar.setMax(100);
                progressBar.setProgress(0);
                mSWebViewDelegate.getWebView().addView(progressBar, ViewGroup.LayoutParams.FILL_PARENT, barHeight);
                isAdd = true;
            }
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(newProgress);
        }
    }
}
