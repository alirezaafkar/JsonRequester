package com.alirezaafkar.json.requester;

import com.android.volley.VolleyError;

/**
 * Created by Alireza Afkar on 12/11/15 AD.
 */
public class CommonUtils {
    public static String appendToUrl(String url, String param) {
        String s = url.contains("?") ? "&" : "?";
        return url + String.format(param, s);
    }

    public static boolean isClientError(VolleyError volleyError) {
        int statusCode = volleyError.networkResponse.statusCode;
        return (statusCode >= 400 && statusCode < 500);
    }
}