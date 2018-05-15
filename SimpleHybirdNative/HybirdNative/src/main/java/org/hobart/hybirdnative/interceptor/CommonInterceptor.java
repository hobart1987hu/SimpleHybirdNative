package org.hobart.hybirdnative.interceptor;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by huzeyin on 2018/5/15.
 */

public class CommonInterceptor {

    protected final Context mContext;

    public CommonInterceptor(Context context) {
        mContext = context;
    }

    /**
     * 在这里面可以进行更多的拦截处理
     *
     * @param url
     * @return
     */
    public boolean interceptorUrl(String url) {
        if (TextUtils.isEmpty(url)) return false;
        if (url.startsWith("tel:")) {
            startIntent(Uri.parse(url));
            return true;
        }
        return false;
    }

    public void startIntent(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}


