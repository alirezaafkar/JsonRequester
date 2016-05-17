package com.alirezaafkar.json.requester;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import static android.text.TextUtils.isEmpty;

/**
 * Created by Alireza Afkar on 12/11/15 AD.
 */
@SuppressWarnings("unused")
public class Requester {
    private static Config config;

    private Requester() {
    }

    public static RequestQueue getRequestQueue() {
        return config.getRequestQueue();
    }

    public static Map<String, String> getHeader() {
        return config.getHeader();
    }


    public static Integer getTimeOut() {
        return config.getTimeOut();
    }

    public static String getEncoding() {
        return config.getEncoding();
    }

    public static String getGeneralParam() {
        String params = config.getGeneralParam();
        return isEmpty(params) ? null : "%s" + params;
    }

    public static Integer getRetry() {
        return config.getRetry();
    }

    public static String getBaseUrl() {
        return config.getBaseUrl();
    }

    public static class Config {
        private Integer retry, timeOut;
        private RequestQueue requestQueue;
        private Map<String, String> header;
        private String baseUrl, generalParam, encoding = "UTF-8";

        public Config(Context context) {
            this.header = new HashMap<>();
            this.requestQueue = Volley.newRequestQueue(context);
        }

        private String getGeneralParam() {
            return generalParam;
        }

        public Config generalParam(String generalParam) {
            this.generalParam = generalParam;
            return build();
        }

        private Integer getTimeOut() {
            return timeOut;
        }

        public Config timeOut(Integer timeOut) {
            this.timeOut = timeOut;
            return build();
        }

        private String getEncoding() {
            return encoding;
        }

        public Config encoding(String encoding) {
            this.encoding = encoding;
            return build();
        }

        private RequestQueue getRequestQueue() {
            return requestQueue;
        }

        public Config requestQueue(RequestQueue requestQueue) {
            this.requestQueue = requestQueue;
            return build();
        }

        private Map<String, String> getHeader() {
            return header;
        }

        public Config header(Map<String, String> header) {
            this.header = header;
            return build();
        }

        private Integer getRetry() {
            return retry;
        }

        public Config retry(Integer retry) {
            this.retry = retry;
            return build();
        }

        private String getBaseUrl() {
            return baseUrl;
        }

        public Config baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return build();
        }

        private Config build() {
            config = this;
            return this;
        }
    }
}
