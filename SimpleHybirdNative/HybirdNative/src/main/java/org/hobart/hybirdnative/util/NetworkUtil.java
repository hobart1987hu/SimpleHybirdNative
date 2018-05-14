package org.hobart.hybirdnative.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class NetworkUtil {

    private NetworkUtil() {
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connManager.getActiveNetworkInfo();
        return isNetworkActive(info);
    }

    private static boolean isNetworkActive(NetworkInfo info) {
        if ((info != null) && info.isConnected() && info.isAvailable()) {
            return true;
        }
        return false;
    }
}
