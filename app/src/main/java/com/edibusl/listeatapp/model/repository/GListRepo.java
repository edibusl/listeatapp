package com.edibusl.listeatapp.model.repository;


import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.edibusl.listeatapp.helpers.VolleyQueue;
import com.edibusl.listeatapp.model.datatypes.GItem;
import com.edibusl.listeatapp.model.datatypes.GList;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GListRepo {
    //TODO - Move to configs
    private final String BASE_URL = "http://10.100.102.7:9090";

    public void getGListFullInfo(int glistId, @NonNull final AppData.LoadDataCallback callback) {
        //Instantiate the RequestQueue.
        String url = String.format("%s/glist/fullInfo/%s", BASE_URL, String.valueOf(glistId));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(new GList(response));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.getMessage());
                    }
                }
        );

        // Add the request to the RequestQueue.
        VolleyQueue.getInstance().addToRequestQueue(request);
    }
}