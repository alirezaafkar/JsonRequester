package com.afkar.json.requester;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Alireza Afkar on 12/12/15 AD.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        Requester.init(requestQueue);
    }
}
