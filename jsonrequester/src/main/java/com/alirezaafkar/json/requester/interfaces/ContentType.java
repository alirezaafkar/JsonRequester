package com.alirezaafkar.json.requester.interfaces;

import android.support.annotation.StringDef;

/**
 * Created by Alireza Afkar on 12/11/15 AD.
 */
public class ContentType {
    @StringDef({TYPE_JSON, TYPE_FORM})
    public @interface Type {
    }

    public static final String TYPE_JSON = "application/json";
    public static final String TYPE_FORM = "application/x-www-form-urlencoded";
}
