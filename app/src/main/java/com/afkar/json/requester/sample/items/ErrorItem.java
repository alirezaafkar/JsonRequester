package com.afkar.json.requester.sample.items;

import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by Alireza Afkar on 12/12/15 AD.
 */
public class ErrorItem {
    private String message;

    public static ErrorItem newInstance(JSONObject object) {
        if (object == null) return null;
        return new Gson().fromJson(object.toString(), ErrorItem.class);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
