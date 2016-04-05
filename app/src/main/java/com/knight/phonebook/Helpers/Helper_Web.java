package com.knight.phonebook.Helpers;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class Helper_Web extends Application {

    public static final String TAG = Helper_Web.class.getSimpleName();
    private RequestQueue requestQueue;
    private static Helper_Web webService;

    @Override
    public void onCreate() {

        super.onCreate();
        webService = this;

    }

    public static synchronized Helper_Web getWebService() {

        return webService;

    }

    public RequestQueue getRequestQueue() {

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request, String Tag) {

        request.setTag(TextUtils.isEmpty(Tag) ? TAG : Tag);
        getRequestQueue().add(request);

    }

    public void cancelPendingRequests(Object tag) {

        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }

    }

}
