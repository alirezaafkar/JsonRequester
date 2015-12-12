package com.afkar.json.requester;

import android.support.annotation.Nullable;

import com.android.volley.RequestQueue;

import java.util.HashMap;
import java.util.Map;

import static com.afkar.json.requester.CommonUtils.isEmptyString;

/**
 * Created by Alireza Afkar on 12/11/15 AD.
 */
public class Requester {
    public static final int UN_DEFINED_ERROR = -1;

    private static String sEndOfUrl;
    private static int sTimeOut = 5000;
    private static String sEncoding = "UTF-8";
    private static RequestQueue sRequestQueue;
    private static Map<String, String> sHeader;

    /**
     * @param queue Request queue to add and manage requests
     */
    public static void init(RequestQueue queue) {
        init(queue, null, 0, null, null);
    }

    /**
     * @param queue  Request queue to add and manage requests
     * @param header Request header
     */
    public static void init(RequestQueue queue, Map<String, String> header) {
        init(queue, header, 0, null, null);
    }

    /**
     * @param queue       Request queue to add and manage requests
     * @param header      Request header
     * @param timeOut     Request time out
     * @param encoding    Params encoding
     * @param appendToUrl String for appending to all of URLs (Such as language)
     */
    public static void init(RequestQueue queue, Map<String, String> header,
                            int timeOut, String encoding, String appendToUrl) {
        sRequestQueue = queue;
        if (header != null) sHeader = header;
        if (timeOut >= 0) sTimeOut = timeOut;
        if (!isEmptyString(encoding))
            sEncoding = encoding;
        if (!isEmptyString(appendToUrl))
            sEndOfUrl = appendToUrl;

    }

    @Nullable
    public static RequestQueue getRequestQueue() {
        return sRequestQueue;
    }

    @Nullable
    public static Map<String, String> getHeader() {
        return sHeader;
    }

    public static int getTimeOut() {
        return sTimeOut;
    }

    public static String getEncoding() {
        return sEncoding;
    }

    public static String getEndOfUrl() {
        if (isEmptyString(sEndOfUrl))
            return null;
        return "%s" + sEndOfUrl;
    }

    public static void setRequestQueue(RequestQueue queue) {
        sRequestQueue = queue;
    }

    public static void setHeader(HashMap<String, String> header) {
        sHeader = header;
    }

    public static void setTimeOut(int timeOut) {
        sTimeOut = timeOut;
    }

    public static void setEncoding(String encoding) {
        sEncoding = encoding;
    }

}
