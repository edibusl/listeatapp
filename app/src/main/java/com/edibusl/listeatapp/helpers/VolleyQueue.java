package com.edibusl.listeatapp.helpers;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class VolleyQueue {
    private static VolleyQueue mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private VolleyQueue() {
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
}