package com.afkar.json.requester;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.NetworkError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * Created by Alireza Afkar on 12/11/15 AD.
 */
public class CommonUtils {
    /**
     * @return Returns true if network is connected
     */
    public static boolean isNetworkAvailable(Context context) {
        if (context == null) return false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * @return Returns true if string is null or empty
     */
    public static boolean isEmptyString(String string) {
        return !(string != null && !string.equals(""));
    }

    /**
     * Checks error status code
     *
     * @param volleyError Error object
     * @return Returns String res if error is Network or ServerError,
     * else Returns -1
     */
    public static int checkVolleyError(VolleyError volleyError) {
        if (volleyError == null) {
            return R.string.network_error;
        } else if (volleyError instanceof TimeoutError ||
                volleyError instanceof NetworkError) {
            return R.string.network_error;
        } else if (volleyError instanceof ServerError) {
            return R.string.server_error;
        } else {
            return Requester.UN_DEFINED_ERROR;
        }
    }
}
