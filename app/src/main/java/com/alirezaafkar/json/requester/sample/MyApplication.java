package com.alirezaafkar.json.requester.sample;

import android.app.Application;

import com.alirezaafkar.json.requester.Requester;

/**
 * Created by Alireza Afkar on 12/12/15 AD.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Requester.Config config = new Requester.Config(getApplicationContext());
        Requester.init(config);
    }
}
