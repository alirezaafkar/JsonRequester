package com.afkar.json.requester.requesters;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.afkar.json.requester.CommonUtils;
import com.afkar.json.requester.R;
import com.afkar.json.requester.Requester;
import com.afkar.json.requester.interfaces.Response;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Alireza Afkar on 12/11/15 AD.
 */
public class JsonArrayRequester implements com.android.volley.Response.Listener<String>, com.android.volley.Response.ErrorListener {
    private RequestBuilder mBuilder;
    private RequestQueue mQueue;
    private Response.ArrayResponse mCallBack;

    protected JsonArrayRequester() {
    }

    protected JsonArrayRequester(RequestQueue queue, RequestBuilder builder, Response.ArrayResponse callBack) {
        mQueue = queue;
        mBuilder = builder;
        mCallBack = callBack;
    }

    @SuppressWarnings("unused")
    public void request(int method, @NonNull String url) {
        if (mCallBack != null)
            mCallBack.onRequestStart(mBuilder.requestCode);

        if (!CommonUtils.isNetworkAvailable(mBuilder.context)) {
            sendFinish(R.string.network_error);
            return;
        }

        String endOfUrl = Requester.getEndOfUrl();
        if (endOfUrl != null) {
            String s = url.contains("?") ? "&" : "?";
            url += String.format(endOfUrl, s);
        }

        StringRequest request = new StringRequest(method, url, JsonArrayRequester.this, JsonArrayRequester.this) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (mBuilder.header != null)
                    return mBuilder.header;
                return super.getHeaders();
            }

            @Override
            public String getBodyContentType() {
                return mBuilder.contentType;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                if (mBuilder.body != null)
                    return mBuilder.body.getBytes();
                return super.getBody();
            }

            @Override
            protected String getParamsEncoding() {
                return Requester.getEncoding();
            }

            @Override
            public Priority getPriority() {
                return mBuilder.priority;
            }

            @Override
            protected com.android.volley.Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    if (!(response != null && response.data != null))
                        return com.android.volley.Response.error(null);

                    String string = new String(response.data, "UTF-8");
                    return com.android.volley.Response.success(string, HttpHeaderParser
                            .parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return com.android.volley.Response.error(new ParseError(e));
                }
            }
        };

        if (mBuilder.retry == null)
            mBuilder.retry = DefaultRetryPolicy.DEFAULT_MAX_RETRIES;

        request.setShouldCache(mBuilder.shouldCache);
        request.setRetryPolicy(new DefaultRetryPolicy(mBuilder.timeOut,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(mBuilder.tag);
        mQueue.add(request);
    }

    @Override
    public void onResponse(String response) {
        sendResponse(response);
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        int error = CommonUtils.checkVolleyError(volleyError);
        if (error == Requester.UN_DEFINED_ERROR)
            sendError(volleyError);
        else
            sendFinish(error, volleyError);
    }

    @SuppressWarnings("unused")
    public void setCallback(Response.ArrayResponse callback) {
        mCallBack = callback;
    }

    private void sendResponse(String response) {
        if (mCallBack == null) return;
        mCallBack.onRequestFinish(mBuilder.requestCode);
        try {
            JSONArray jsonArray = new JSONArray(response);
            if (jsonArray.length() > 0) {
                mCallBack.onResponse(mBuilder.requestCode, jsonArray);
            } else
                sendFinish(R.string.error_happened);
        } catch (JSONException e) {
            sendFinish(R.string.network_error);
            if (mBuilder.shouldCache && mQueue.getCache() != null)
                mQueue.getCache().clear();
        }
    }

    private void sendError(VolleyError volleyError) {
        if (mCallBack != null) {
            String error = new String(volleyError.networkResponse.data);
            mCallBack.onErrorResponse(mBuilder.requestCode, volleyError, error);
            mCallBack.onRequestFinish(mBuilder.requestCode);
        }
    }

    private void sendFinish(int message) {
        sendFinish(message, null);
    }

    private void sendFinish(int message, VolleyError volleyError) {
        if (mCallBack == null) return;

        mCallBack.onFinishResponse(mBuilder.requestCode, volleyError,
                mBuilder.context.getString(message));
        mCallBack.onRequestFinish(mBuilder.requestCode);

        if (mBuilder.showError)
            Toast.makeText(mBuilder.context,
                    message, Toast.LENGTH_SHORT).show();
    }
}
