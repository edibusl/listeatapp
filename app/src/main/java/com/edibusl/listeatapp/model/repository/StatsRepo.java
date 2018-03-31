package com.edibusl.listeatapp.model.repository;


import android.app.Activity;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.edibusl.listeatapp.helpers.AwsUtils;
import com.edibusl.listeatapp.helpers.ConfigsManager;
import com.edibusl.listeatapp.helpers.VolleyQueue;
import com.edibusl.listeatapp.model.datatypes.Category;
import com.edibusl.listeatapp.model.datatypes.Product;
import com.edibusl.listeatapp.mvp.BaseRepository;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class StatsRepo extends BaseRepository {
    public void getStatsByCategory(Long userId, @NonNull final AppData.LoadDataCallback callback) {
        //Instantiate the RequestQueue.
        String url = String.format("%s/stats/bycategory/%s", getBaseUrl(), userId);

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response != null && response.has("statsResult")) {
                                callback.onSuccess(response.getJSONArray("statsResult"));
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