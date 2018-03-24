package com.edibusl.listeatapp.helpers;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;

public class VolleyQueue {
    private static VolleyQueue mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;
    private RetryPolicy mRetryPolicy;

    private final int REQUEST_TIMEOUT_SECONDS = 5;
    private final String VOLLEY_LOG_TAG = "Volley";
    private final boolean DEBUG_MODE = false;

    private VolleyQueue() {
        VolleyLog.setTag(VOLLEY_LOG_TAG);
        VolleyLog.DEBUG = DEBUG_MODE;
        mRetryPolicy = new DefaultRetryPolicy(REQUEST_TIMEOUT_SECONDS * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    }

    public static synchronized VolleyQueue getInstance() {
        if (mInstance == null) {
            mInstance = new VolleyQueue();
        }
        return mInstance;
    }

    public void init(Context context){
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public HashMap<String, String> getDefaultHeaders() throws AuthFailureError{
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json; charset=utf-8");

        return headers;
    }

    public RetryPolicy getRetryPolicy() {
        return mRetryPolicy;
    }
}