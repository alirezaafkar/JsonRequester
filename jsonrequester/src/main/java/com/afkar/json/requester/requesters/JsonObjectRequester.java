package com.afkar.json.requester.requesters;

import android.support.annotation.NonNull;
import android.widget.Toast;

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
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import static com.afkar.json.requester.CommonUtils.checkVolleyError;
import static com.afkar.json.requester.CommonUtils.isEmptyString;
import static com.afkar.json.requester.CommonUtils.isNetworkAvailable;

/**
 * Created by Alireza Afkar on 12/11/15 AD.
 */

public class JsonObjectRequester implements com.android.volley.Response.Listener<JSONObject>, com.android.volley.Response.ErrorListener {
    protected RequestBuilder mBuilder;
    protected RequestQueue mQueue;
    protected Response.ObjectResponse mCallBack;

    protected JsonObjectRequester() {
    }

    protected JsonObjectRequester(RequestQueue queue, RequestBuilder builder, Response.ObjectResponse callBack) {
        mQueue = queue;
        mBuilder = builder;
        mCallBack = callBack;
    }

    private JSONObject getNullJson() {
        return null;
    }

    @SuppressWarnings("unused")
    public void request(int method, @NonNull String url) {
        request(method, url, getNullJson());
    }

    @SuppressWarnings("unused")
    public void request(int method, @NonNull String url, @NonNull String body) {
        mBuilder.body = body;
        request(method, url, getNullJson());
    }

    public void request(int method, @NonNull String url, JSONObject jsonObject) {
        if (mCallBack != null)
            mCallBack.onRequestStart(mBuilder.requestCode);

        if (!isNetworkAvailable(mBuilder.context)) {
            sendFinish(R.string.network_error);
            return;
        }

        String endOfUrl = Requester.getEndOfUrl();
        if (endOfUrl != null) {
            String s = url.contains("?") ? "&" : "?";
            url += String.format(endOfUrl, s);
        }

        JsonObjectRequest request = new JsonObjectRequest(method,
                url, jsonObject, JsonObjectRequester.this, JsonObjectRequester.this) {

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
            public byte[] getBody() {
                if (!isEmptyString(mBuilder.body))
                    return mBuilder.body.getBytes();
                return super.getBody();
            }

            @Override
            protected String getParamsEncoding() {
                return "UTF-8";
            }

            @Override
            public Priority getPriority() {
                return mBuilder.priority;
            }

            @Override
            protected com.android.volley.Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    if (!(response != null && response.data != null))
                        return com.android.volley.Response.error(null);

                    String jsonString = new String(response.data, "UTF-8");
                    if (jsonString.length() == 0 || jsonString.equalsIgnoreCase("null")) {
                        if (mBuilder.allowNullResponse)
                            return com.android.volley.Response.success(null
                                    , HttpHeaderParser.parseCacheHeaders(response));
                    }

                    return com.android.volley.Response.success(new JSONObject(jsonString)
                            , HttpHeaderParser.parseCacheHeaders(response));

                } catch (UnsupportedEncodingException e) {
                    return com.android.volley.Response.error(new ParseError(e));
                } catch (JSONException je) {
                    if (mBuilder.shouldCache && mQueue.getCache() != null)
                        mQueue.getCache().clear();
                    return com.android.volley.Response.error(new ParseError(je));
                }
            }
        };

        if (mBuilder.retry == null)
            mBuilder.retry = DefaultRetryPolicy.DEFAULT_MAX_RETRIES;

        request.setShouldCache(mBuilder.shouldCache);
        request.setRetryPolicy(new DefaultRetryPolicy(mBuilder.timeOut
                , mBuilder.retry, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(mBuilder.tag);
        mQueue.add(request);
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        if (jsonObject == null && !mBuilder.allowNullResponse)
            sendFinish(R.string.error_happened);
        else {
            sendResponse(jsonObject);
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        int error = checkVolleyError(volleyError);
        if (error == Requester.UN_DEFINED_ERROR)
            sendError(volleyError);
        else
            sendFinish(error, volleyError);
    }

    @SuppressWarnings("unused")
    public void setCallback(Response.ObjectResponse callback) {
        mCallBack = callback;
    }

    private void sendResponse(JSONObject jsonObject) {
        if (mCallBack == null) return;
        mCallBack.onResponse(mBuilder.requestCode, jsonObject);
        mCallBack.onRequestFinish(mBuilder.requestCode);
    }

    private void sendError(VolleyError volleyError) {
        if (mCallBack == null) return;
        try {
            JSONObject errorObject = new JSONObject(new
                    String(volleyError.networkResponse.data));
            mCallBack.onErrorResponse(mBuilder.requestCode, volleyError, errorObject);
            mCallBack.onRequestFinish(mBuilder.requestCode);
        } catch (JSONException e) {
            sendFinish(R.string.error_happened, volleyError);
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