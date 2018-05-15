package org.hobart.hybirdnative.widget;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import org.hobart.hybirdnative.SWConfig;
import org.hobart.hybirdnative.util.FileUtils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by huzeyin on 2018/5/11.
 */

public class SimpleWebView extends RelativeLayout {

    private static final String TAG = "SimpleWebView";
    private FixedWebView mWebView = null;
    private String mWebCache;

    public SimpleWebView(Context context) {
        super(context);
        init();
    }

    public SimpleWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SimpleWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mWebView = new FixedWebView(getContext());
        addView(mWebView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        intSet();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    private void intSet() {
        WebSettings settings = mWebView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);

        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        // Set the nav dump for HTC 2.x devices (disabling for ICS, deprecated entirely for Jellybean 4.2)
        try {
            Method gingerbread_getMethod = WebSettings.class.getMethod("setNavDump", new Class[]{boolean.class});

            String manufacturer = Build.MANUFACTURER;
            Log.d(TAG, "SimpleWebView is running on device made by: " + manufacturer);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB &&
                    Build.MANUFACTURER.contains("HTC")) {
                gingerbread_getMethod.invoke(settings, true);
            }
        } catch (NoSuchMethodException e) {
            Log.d(TAG, "We are on a modern version of Android, we will deprecate HTC 2.3 devices in 2.8");
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "Doing the NavDump failed with bad arguments");
        } catch (IllegalAccessException e) {
            Log.d(TAG, "This should never happen: IllegalAccessException means this isn't Android anymore");
        } catch (InvocationTargetException e) {
            Log.d(TAG, "This should never happen: InvocationTargetException means this isn't Android anymore.");
        }
        //We don't save any form data in the application
        settings.setSaveFormData(false);
        settings.setSavePassword(false);
        // Jellybean rightfully tried to lock this down. Too bad they didn't give us a whitelist
        // while we do this
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowUniversalAccessFromFileURLs(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.setMediaPlaybackRequiresUserGesture(false);
        }
        // Enable database
        // We keep this disabled because we use or shim to get around DOM_EXCEPTION_ERROR_16
        String databasePath = getContext().getApplicationContext().getDir("web_cache", Context.MODE_PRIVATE).getPath();
        mWebCache = databasePath;
        settings.setDatabaseEnabled(true);
        settings.setDatabasePath(databasePath);
        settings.setGeolocationDatabasePath(databasePath);
        // Enable DOM storage
        settings.setDomStorageEnabled(true);
        // Enable built-in geolocation
        settings.setGeolocationEnabled(true);
        // Enable AppCache
        // Fix for CB-2282
        settings.setAppCacheMaxSize(5 * 1048576);
        settings.setAppCachePath(databasePath);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 本地需求。
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        // 在 DEBUG 模式下开启调试模式，方便 H5调试，如果无需要删掉
        if (SWConfig.DEBUG) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mWebView.setWebContentsDebuggingEnabled(true);
            }
        }
        setFocusableInTouchMode(true);
        setClickable(true);
    }

    public String getWebCache() {
        return mWebCache;
    }

    public void setFocusableInTouchMode(boolean enable) {
        mWebView.setFocusableInTouchMode(enable);
    }

    public void setClickable(boolean value) {
        mWebView.setClickable(value);
    }

    public void setSupportZoom(boolean value) {
        mWebView.getSettings().setSupportZoom(value);
    }

    public void setBuiltInZoomControls(boolean value) {
        mWebView.getSettings().setBuiltInZoomControls(value);
    }

    public void setCacheMode(int value) {
        mWebView.getSettings().setCacheMode(value);
    }

    public void setWebViewClient(WebViewClient value) {
        mWebView.setWebViewClient(value);
    }

    public void setWebChromeClient(WebChromeClient webChromeClient) {
        mWebView.setWebChromeClient(webChromeClient);
    }

    public void setInitialScale(int scaleInPercent) {
        mWebView.setInitialScale(scaleInPercent);
    }

    public void loadUrl(String url) {
        if (null != mWebView && !isDestroy() && getSettings() != null) {
            mWebView.loadUrl(url);
        } else {
            Log.d(TAG, "loadUrl mWebView is null");
        }
    }

    public boolean isDestroy() {
        return mWebView.isDestroy();
    }

    public WebSettings getSettings() {
        return mWebView.getSettings();
    }

    public void restoreState(Bundle savedInstanceState) {
        if (null != mWebView)
            mWebView.restoreState(savedInstanceState);
    }

    public void saveState(Bundle outState) {
        if (null != mWebView)
            mWebView.saveState(outState);
    }

    public boolean canGoBack() {
        if (null != mWebView) return mWebView.canGoBack();
        return false;
    }

    public void goBack() {
        if (null != mWebView) mWebView.goBack();
    }

    public void onPause() {
        if (null != mWebView) {
            callHiddenWebViewMethod("onPause");
            mWebView.pauseTimers();
        }
    }

    public void onResume() {
        if (null != mWebView) {
            callHiddenWebViewMethod("onResume");
            mWebView.resumeTimers();
        }
    }

    public WebView getWebView() {
        return mWebView;
    }

    private void callHiddenWebViewMethod(String name) {
        // credits: http://stackoverflow.com/questions/3431351/how-do-i-pause-flash-content-in-an-android-webview-when-my-activity-isnt-visible
        if (mWebView != null) {
            try {
                Method method = WebView.class.getMethod(name);
                method.invoke(mWebView);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public void nativeCallJS(String javascript) {
        if (null != mWebView)
            mWebView.nativeCallJS(javascript);
    }

    public void addJavascriptInterface(Object object, String name) {
        if (null != mWebView)
            mWebView.jsCallNative(object, name);
    }

    public void onDestroy() {
        FileUtils.delete(new File(mWebCache));
        try {
            if (mWebView != null) {
                mWebView.clearCache(true);
                mWebView.clearHistory();
                clearCookies(getContext());
                ViewParent parent = mWebView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(mWebView);
                }
                mWebView.setWebChromeClient(null);
                mWebView.setWebViewClient(null);
                mWebView.stopLoading();
                mWebView.destroyDrawingCache();
                mWebView.destroy();
                mWebView = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearCookies(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().removeSessionCookies(null);
            CookieManager.getInstance().flush();
        } else {
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
    }
}
