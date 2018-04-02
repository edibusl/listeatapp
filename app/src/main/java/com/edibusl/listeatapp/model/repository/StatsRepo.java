package com.edibusl.listeatapp.model.repository;


import android.app.Activity;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.edibusl.listeatapp.helpers.VolleyQueue;
import com.edibusl.listeatapp.mvp.BaseRepository;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class StatsRepo extends BaseRepository {
    public void getStatsByCategory(Long userId, @NonNull final AppData.LoadDataCallback callback) {
        //Instantiate the RequestQueue.
        String url = String.format("%s/stats/bycategory/%s", getBaseUrl(), userId);

        RequestFuture<JSONArray> future = RequestFuture.newFuture();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            if (response != null) {
                                callback.onSuccess(response);
                            } else {
                                callback.onSuccess(null);
                            }
                        } catch (Exception ex) {
                            callback.onError(ex.getMessage());
                        }
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