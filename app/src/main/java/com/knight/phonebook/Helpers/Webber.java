package com.knight.phonebook.Helpers;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class Webber extends Application {

    public static final String TAG = Webber.class.getSimpleName();
    private RequestQueue requestQueue;
    private static Webber webService;

    @Override
    public void onCreate() {

        super.onCreate();
        webService = this;

    }

    public static synchronized Webber getWebService() {

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
