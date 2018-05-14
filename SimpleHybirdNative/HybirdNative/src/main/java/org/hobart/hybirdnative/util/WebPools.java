package org.hobart.hybirdnative.util;

import android.app.Activity;
import android.content.MutableContextWrapper;

import org.hobart.hybirdnative.widget.SimpleWebView;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by huzeyin on 2018/5/11.
 */

public class WebPools {
    private final Queue<SimpleWebView> mWebViews;
    private Object lock = new Object();
    private static WebPools mWebPools = null;
    private static final AtomicReference<WebPools> mAtomicReference = new AtomicReference<>();
    private static final String TAG = WebPools.class.getSimpleName();

    private WebPools() {
        mWebViews = new LinkedBlockingQueue<>();
    }

    public static WebPools getInstance() {
        for (; ; ) {
            if (mWebPools != null)
                return mWebPools;
            if (mAtomicReference.compareAndSet(null, new WebPools()))
                return mWebPools = mAtomicReference.get();
        }
    }

    public void recycle(SimpleWebView webView) {
        recycleInternal(webView);
    }

    public SimpleWebView acquireWebView(Activity activity) {
        return acquireWebViewInternal(activity);
    }

    private SimpleWebView acquireWebViewInternal(Activity activity) {

        SimpleWebView mWebView = mWebViews.poll();

        if (mWebView == null) {
            synchronized (lock) {
                return new SimpleWebView(new MutableContextWrapper(activity));
            }
        } else {
            MutableContextWrapper mMutableContextWrapper = (MutableContextWrapper) mWebView.getContext();
            mMutableContextWrapper.setBaseContext(activity);
            return mWebView;
        }
    }

    private void recycleInternal(SimpleWebView webView) {
        try {
            if (webView.getContext() instanceof MutableContextWrapper) {
                MutableContextWrapper mContext = (MutableContextWrapper) webView.getContext();
                mContext.setBaseContext(mContext.getApplicationContext());
                mWebViews.offer(webView);
            }
            if (webView.getContext() instanceof Activity) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
