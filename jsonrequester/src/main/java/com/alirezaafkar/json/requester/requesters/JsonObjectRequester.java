package com.alirezaafkar.json.requester.requesters;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.alirezaafkar.json.requester.R;
import com.alirezaafkar.json.requester.Requester;
import com.alirezaafkar.json.requester.interfaces.Methods;
import com.alirezaafkar.json.requester.interfaces.Response;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import static com.alirezaafkar.json.requester.CommonUtils.isEmptyString;

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

    /**
     * Send request using {@link Methods Methods.GET}
     */
    @SuppressWarnings("unused")
    public void request(@NonNull String url) {
        request(Methods.GET, url, getNullJson());
    }

    @SuppressWarnings("unused")
    public void request(@Methods.Method int method, @NonNull String url) {
        request(method, url, getNullJson());
    }

    @SuppressWarnings("unused")
    public void request(@Methods.Method int method, @NonNull String url, @NonNull String body) {
        mBuilder.body = body;
        request(method, url, getNullJson());
    }

    public void request(@Methods.Method int method, @NonNull String url, JSONObject jsonObject) {
        if (mCallBack != null)
            mCallBack.onRequestStart(mBuilder.requestCode);

        String param = Requester.getGeneralParam();
        if (param != null) {
            String s = url.contains("?") ? "&" : "?";
            url += String.format(param, s);
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
                return mBuilder.encoding;
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

                    String jsonString = new String(response.data, mBuilder.encoding);
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

        if (mBuilder.timeOut == null)
            mBuilder.timeOut = DefaultRetryPolicy.DEFAULT_TIMEOUT_MS;

        request.setShouldCache(mBuilder.shouldCache);
        request.setRetryPolicy(new DefaultRetryPolicy(mBuilder.timeOut
                , mBuilder.retry, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(mBuilder.tag);
        mQueue.add(request);
    }

    @Override
    public void onResponse(JSONObject jsonObject) {
        if (jsonObject == null && !mBuilder.allowNullResponse) {
            sendFinish(R.string.parsing_error, new ParseError());
        } else {
            sendResponse(jsonObject);
        }
    }

    @Override
    public void onErrorResponse(VolleyError volleyError) {
        if (volleyError instanceof NoConnectionError) {
            sendFinish(R.string.no_connection_error, volleyError);
        } else if (volleyError instanceof NetworkError) {
            sendFinish(R.string.network_error, volleyError);
        } else if (volleyError instanceof TimeoutError) {
            sendFinish(R.string.timeout_error, volleyError);
        } else if (volleyError instanceof ServerError) {
            sendFinish(R.string.server_error, volleyError);
        } else if (volleyError instanceof ParseError) {
            sendFinish(R.string.parsing_error, volleyError);
        } else {
            sendError(volleyError);
        }
    }

    @SuppressWarnings("unused")
    public void setCallback(Response.ObjectResponse callback) {
        mCallBack = callback;
    }

    private void sendResponse(JSONObject jsonObject) {
        if (mCallBack != null) {
            mCallBack.onResponse(mBuilder.requestCode, jsonObject);
            mCallBack.onRequestFinish(mBuilder.requestCode);
        }
    }

    private void sendError(VolleyError volleyError) {
        if (mCallBack == null) return;
        try {
            JSONObject errorObject = new JSONObject(new
                    String(volleyError.networkResponse.data));
            mCallBack.onErrorResponse(mBuilder.requestCode, volleyError, errorObject);
            mCallBack.onRequestFinish(mBuilder.requestCode);
        } catch (JSONException e) {
            sendFinish(R.string.parsing_error, volleyError);
        }
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