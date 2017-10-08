package com.alirezaafkar.json.requester.requesters;

import android.content.Context;
import android.support.annotation.Nullable;

import com.alirezaafkar.json.requester.CacheTime;
import com.alirezaafkar.json.requester.Requester;
import com.alirezaafkar.json.requester.interfaces.ContentType;
import com.alirezaafkar.json.requester.interfaces.Response;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.Map;

import static com.alirezaafkar.json.requester.interfaces.ContentType.TYPE_JSON;

/**
 * Created by Alireza Afkar on 12/11/15 AD.
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class RequestBuilder {
    protected Context context;
    protected int requestCode;
    protected CacheTime cacheTime;
    protected Integer retry, timeOut;
    protected Request.Priority priority;
    protected Map<String, String> header;
    protected String tag, contentType, encoding;
    protected boolean showError, allowNullResponse, shouldCache, ignoreBaseUrl;

    public RequestBuilder(@Nullable Context context) {
        if (context == null) {
            throw new IllegalStateException("Context can not be null");
        }
        this.tag = "";
        this.context = context;
        this.contentType = TYPE_JSON;
        this.retry = Requester.getRetry();
        this.header = Requester.getHeader();
        this.timeOut = Requester.getTimeOut();
        this.encoding = Requester.getEncoding();
        this.priority = Request.Priority.NORMAL;
    }

    public JsonObjectRequester buildObjectRequester(Response.ObjectResponse listener) {
        return buildObjectRequester(getQueue(), listener);
    }

    public JsonArrayRequester buildArrayRequester(Response.ArrayResponse listener) {
        return buildArrayRequester(getQueue(), listener);
    }

    public JsonStringRequester buildStringRequester(Response.StringResponse listener) {
        return buildStringRequester(getQueue(), listener);
    }

    public JsonObjectRequester buildObjectRequester(RequestQueue queue, Response.ObjectResponse listener) {
        return new JsonObjectRequester(queue, this, listener);
    }

    public JsonArrayRequester buildArrayRequester(RequestQueue queue, Response.ArrayResponse listener) {
        return new JsonArrayRequester(queue, this, listener);
    }

    public JsonStringRequester buildStringRequester(RequestQueue queue, Response.StringResponse listener) {
        return new JsonStringRequester(queue, this, listener);
    }

    private RequestQueue getQueue() {
        if (Requester.getRequestQueue() != null) {
            return Requester.getRequestQueue();
        } else {
            return Volley.newRequestQueue(this.context);
        }
    }

    public RequestBuilder requestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    /*
    * Shows toast with error text on Network or Response Error
    * */
    public RequestBuilder showError(boolean showError) {
        this.showError = showError;
        return this;
    }

    /*
    * Consider null or empty response as Success
    * */
    public RequestBuilder allowNullResponse(boolean allowNullResponse) {
        this.allowNullResponse = allowNullResponse;
        return this;
    }

    public RequestBuilder tag(String tag) {
        this.tag = tag;
        return this;
    }

    public RequestBuilder contentType(@ContentType.Type String contentType) {
        this.contentType = contentType;
        return this;
    }

    public RequestBuilder header(Map<String, String> header) {
        this.header = header;
        return this;
    }

    public RequestBuilder addToHeader(String name, String value) {
        if (header != null)
            this.header.put(name, value);
        return this;
    }

    public RequestBuilder priority(Request.Priority priority) {
        this.priority = priority;
        return this;
    }

    public RequestBuilder retry(Integer retry) {
        this.retry = retry;
        return this;
    }

    public RequestBuilder timeOut(Integer timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    public RequestBuilder shouldCache(boolean shouldCache) {
        this.shouldCache = shouldCache;
        return this;
    }

    /**
     * Ignores server cache control
     *
     * @param cacheHitButRefreshed in given time, cache will be hit, but also refreshed on background
     * @param cacheExpired         in given time, this cache entry expires completely
     */
    public RequestBuilder forceCache(long cacheHitButRefreshed, long cacheExpired) {
        return forceCache(new CacheTime(cacheHitButRefreshed, cacheExpired));
    }

    /**
     * Ignores server cache control
     */
    public RequestBuilder forceCache(CacheTime cacheTime) {
        this.cacheTime = cacheTime;
        return this;
    }

    public RequestBuilder paramsEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public RequestBuilder ignoreBaseUrl() {
        this.ignoreBaseUrl = true;
        return this;
    }
}
