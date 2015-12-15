package com.alirezaafkar.json.requester;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Alireza Afkar on 12/11/15 AD.
 */
public class CommonUtils {
    public static boolean isNetworkAvailable(Context context) {
        if (context == null) return false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isEmptyString(String string) {
        return !(string != null && !string.equals(""));
    }

    public static String appendToUrl(String url, String param) {
        String s = url.contains("?") ? "&" : "?";
        return url + String.format(param, s);
    }
}