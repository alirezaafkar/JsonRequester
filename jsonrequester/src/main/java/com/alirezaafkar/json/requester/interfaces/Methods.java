package com.alirezaafkar.json.requester.interfaces;

import android.support.annotation.IntDef;

import com.android.volley.Request;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Alireza Afkar on 15/12/2015.
 */
public class Methods implements Request.Method {
    @IntDef({GET, POST, PUT, DELETE, HEAD, OPTIONS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Method {
    }

    public static final int GET = Request.Method.GET;
    public static final int PUT = Request.Method.PUT;
    public static final int HEAD = Request.Method.HEAD;
    public static final int POST = Request.Method.POST;
    public static final int DELETE = Request.Method.DELETE;
    public static final int OPTIONS = Request.Method.OPTIONS;
}
