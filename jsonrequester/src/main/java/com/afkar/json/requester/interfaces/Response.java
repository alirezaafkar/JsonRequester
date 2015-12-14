package com.afkar.json.requester.interfaces;

import android.support.annotation.Nullable;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Alireza Afkar on 12/11/15 AD.
 */
public class Response {
    private interface SimpleResponse {
        void onFinishResponse(int requestCode, @Nullable VolleyError volleyError, String message);

        void onRequestStart(int requestCode);

        void onRequestFinish(int requestCode);
    }

    public interface ObjectResponse extends SimpleResponse {
        void onResponse(int requestCode, @Nullable JSONObject jsonObject);

        void onErrorResponse(int requestCode, VolleyError volleyError, @Nullable JSONObject errorObject);
    }

    public interface ArrayResponse extends SimpleResponse {
        void onResponse(int requestCode, @Nullable JSONArray jsonArray);

        void onErrorResponse(int requestCode, VolleyError volleyError, String error);
    }

    public static class SimpleObjectResponse implements ObjectResponse {
        @Override
        public void onResponse(int requestCode, @Nullable JSONObject jsonObject) {

        }

        @Override
        public void onErrorResponse(int requestCode, VolleyError volleyError, @Nullable JSONObject errorObject) {

        }

        @Override
        public void onFinishResponse(int requestCode, VolleyError volleyError, String message) {

        }

        @Override
        public void onRequestStart(int requestCode) {

        }

        @Override
        public void onRequestFinish(int requestCode) {

        }
    }

    public static class SimpleArrayResponse implements ArrayResponse {
        @Override
        public void onResponse(int requestCode, @Nullable JSONArray jsonArray) {

        }

        @Override
        public void onErrorResponse(int requestCode, VolleyError volleyError, String error) {

        }

        @Override
        public void onFinishResponse(int requestCode, VolleyError volleyError, String error) {

        }

        @Override
        public void onRequestStart(int requestCode) {

        }

        @Override
        public void onRequestFinish(int requestCode) {

        }
    }
}
