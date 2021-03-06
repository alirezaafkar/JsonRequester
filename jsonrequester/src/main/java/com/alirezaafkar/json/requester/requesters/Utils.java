package com.alirezaafkar.json.requester.requesters;

import com.alirezaafkar.json.requester.CacheTime;
import com.alirezaafkar.json.requester.interfaces.Methods;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Alireza Afkar on 12/11/15 AD.
 */
public class Utils {
    public static String appendToUrl(String url, String param) {
        String s = url.contains("?") ? "&" : "?";
        return url + String.format(param, s);
    }

    public static boolean isBodyEmpty(byte[] body) {
        return !(body != null && body.length > 0);
    }

    public static boolean needsFakeBody(int method, byte[] body) {
        return needsFakeBody(method, body, null);
    }

    public static boolean needsFakeBody(int method, byte[] body, JSONObject json) {
        if (method == Methods.POST || method == Methods.PUT) {
            if (json == null && isBodyEmpty(body)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isClientError(VolleyError volleyError) {
        int statusCode = volleyError.networkResponse.statusCode;
        return (statusCode >= 400 && statusCode < 500);
    }

    protected static Cache.Entry parseIgnoreCacheHeaders(NetworkResponse response, CacheTime cache) {
        long now = System.currentTimeMillis();

        Map<String, String> headers = response.headers;
        long serverDate = 0;
        String serverEtag = null;
        String headerValue;

        headerValue = headers.get("Date");
        if (headerValue != null) {
            serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
        }

        serverEtag = headers.get("ETag");

        final long softExpire = now + cache.getCacheHitButRefreshed();
        final long ttl = now + cache.getCacheExpired();

        Cache.Entry entry = new Cache.Entry();
        entry.data = response.data;
        entry.etag = serverEtag;
        entry.softTtl = softExpire;
        entry.ttl = ttl;
        entry.serverDate = serverDate;
        entry.responseHeaders = headers;

        return entry;
    }
}