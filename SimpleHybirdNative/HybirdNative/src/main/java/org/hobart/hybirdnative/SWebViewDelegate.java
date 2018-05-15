package org.hobart.hybirdnative;

import android.content.Context;
import android.webkit.WebChromeClient;

import org.hobart.hybirdnative.loading.SLoadingErrorView;
import org.hobart.hybirdnative.loading.SLoadingView;
import org.hobart.hybirdnative.widget.SimpleWebView;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by huzeyin on 2018/5/14.
 */

public class SWebViewDelegate {

    private SimpleWebView webView;
    private Context mContext;
    private SWebChromeClient webChromeClient;
    private SWebViewClient webViewClient;
    private WebViewCallBack mWebViewCallBack;
    private Map<String, Target> methodCache = new LinkedHashMap<>();

    public SWebViewDelegate(Context context, WebViewCallBack callback, Object... owners) {

        this(context, callback, null, null, owners);
    }

    public SWebViewDelegate(Context context, WebViewCallBack callback, SLoadingView loadingView, SLoadingErrorView emptyView, Object... owners) {
        this.mContext = context;
        setFunctions(owners);
        mWebViewCallBack = callback;
        webView = new SimpleWebView(context);
        webChromeClient = new SWebChromeClient(this);
        webViewClient = new SWebViewClient(context, loadingView, emptyView);
        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(webChromeClient);
        webView.requestFocusFromTouch();
        webView.setInitialScale(0);
        webView.setVerticalScrollBarEnabled(false);
    }

    public void setWebViewCallBack(WebViewCallBack callBack) {
        mWebViewCallBack = callBack;
    }

    public void setFunctions(Object... owners) {
        for (Object owner : owners) {
            for (Method method : owner.getClass().getDeclaredMethods()) {
                Annotation annotation = method.getAnnotation(INVOKE.class);
                if (null != annotation) {
                    methodCache.put(((INVOKE) annotation).value(), new Target(owner, method));
                }
            }
        }
    }

    public void setLoadingView(SLoadingView loadingView) {

        webViewClient.setLoadingView(loadingView);
    }

    public void setSLoadingErrorView(SLoadingErrorView errorView) {

        webViewClient.setLoadingErrorView(errorView);
    }

    public void setWebChromeClient(WebChromeClient webChromeClient) {
        webView.setWebChromeClient(webChromeClient);
    }

    public Context getContext() {
        return mContext;
    }

    public String getWebCache() {
        if (null != webView) return webView.getWebCache();
        return "";
    }

    public WebViewCallBack getWebViewCallBack() {
        return mWebViewCallBack;
    }

    public void loadUrl(final String url) {
        if (webView != null) {
            webView.loadUrl(url);
        }
    }

    public void nativeCallJS(String javascript) {
        if (null != webView)
            webView.nativeCallJS(javascript);
    }

    public void addJavascriptInterface(Object object, String name) {
        if (null != webView)
            webView.addJavascriptInterface(object, name);
    }


    public SimpleWebView getWebView() {
        return webView;
    }

    public void onPause() {
        if (null != webView) {
            webView.onPause();
        }
    }

    public void onResume() {
        if (null != webView) {
            webView.onResume();
        }
    }

    public void onDestroy() {
        if (null != webView) {
            webView.onDestroy();
            webView = null;
        }
    }

    public void handleJsPrompt(String message) throws JSONException {

        JSONObject jsObj = new JSONObject(message);
        String strTarget = jsObj.getString("invoke");
        JSONObject jsParas = jsObj.getJSONObject("paras");

        Target target = methodCache.get(strTarget);

        if (target != null) {
            try {
                target.method.invoke(target.owner, jsParas);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Target {
        Method method;
        Object owner;

        public Target(Object owner, Method method) {
            this.owner = owner;
            this.method = method;
        }
    }
}
