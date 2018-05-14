package org.hobart.hybirdnative.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * 这里面不需要兼容android sdk<4.2以下的版本
 * Created by huzeyin on 2018/5/11.
 */
public class FixedWebView extends WebView {

    private boolean mIsDestroy = false;

    public FixedWebView(Context context) {
        super(context);
        init();
    }

    public FixedWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FixedWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mIsDestroy = false;
        removeJavascriptInterface("searchBoxJavaBridge_");
        removeJavascriptInterface("accessibility");
        removeJavascriptInterface("accessibilityTraversal");
    }

    @Override
    public void destroy() {
        mIsDestroy = true;
        super.destroy();
    }

    public boolean isDestroy() {
        return mIsDestroy;
    }

    /**
     * 从native调用javascript方法
     *
     * @param javascript
     */
    public void nativeCallJS(String javascript) {
        if (Build.VERSION.SDK_INT <= 18) {
            loadUrl("javascript:" + javascript);
        } else {
            evaluateJavascript(javascript, null);
        }
    }

    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
    public void jsCallNative(Object object, String name) {
        addJavascriptInterface(object, name);
    }
}
