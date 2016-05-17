package com.alirezaafkar.json.requester.sample;

import android.app.Application;

import com.alirezaafkar.json.requester.Requester;

/**
 * Created by Alireza Afkar on 12/12/15 AD.
 */
public class MyApplication extends Application {
    private static final String BASE_URL = "https://api.github.com/";

    public static final String FOLLOWERS_API = "user/followers";
    public static final String USER_API = "users/alirezaafkar";
    public static final String GIST_API = "gists/public";

    @Override
    public void onCreate() {
        super.onCreate();
        new Requester.Config(getApplicationContext()).baseUrl(BASE_URL);
    }
}
