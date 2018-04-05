package com.edibusl.listeatapp.helpers;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;

/**
 * A singleton class that holds a single queue for the whole application.
 * This is a queue for all requests that are sent to the server from the app.
 * Additionally, the class provides more functionality related to sending requests using Volley.
 */
public class VolleyQueue {
    private static VolleyQueue mInstance;

    private RequestQueue mRequestQueue;
    private Context mCtx;
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