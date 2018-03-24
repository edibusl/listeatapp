package com.edibusl.listeatapp.model.repository;


import android.support.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.edibusl.listeatapp.helpers.VolleyQueue;
import com.edibusl.listeatapp.model.datatypes.GItem;
import com.edibusl.listeatapp.model.datatypes.GList;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GListRepo {
    //TODO - Move to configs
    private final String BASE_URL = "http://10.100.102.7:9090";

    private int mCurrentGListId = 0;

    public GListRepo() {
        //TODO - Load from mCurrentGListId from saved config
        mCurrentGListId = 21;
    }

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

    public void getAllUserGLists(int userId, @NonNull final AppData.LoadDataCallback callback) {
        //Instantiate the RequestQueue.
        String url = String.format("%s/glist/all/%s", BASE_URL, String.valueOf(userId));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(GList.parseList(response));
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

    public void updateGItem(GItem gItem, @NonNull final AppData.LoadDataCallback callback) {
        //Instantiate the RequestQueue.
        String url = String.format("%s/gitem", BASE_URL);

        //Decide about the request method according to edit mode / new mode
        Integer gItemId = gItem.getGitemId();
        int method = (gItemId == null || gItemId == 0 ? Request.Method.POST : Request.Method.PUT);

        JsonObjectRequest request = new JsonObjectRequest(method, url, gItem.toJson(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.getMessage());
                    }
                }
        );
        request.setRetryPolicy(VolleyQueue.getInstance().getRetryPolicy());

        // Add the request to the RequestQueue.
        VolleyQueue.getInstance().addToRequestQueue(request);
    }

    public void deleteGItem(long gItemId, @NonNull final AppData.LoadDataCallback callback) {
        //Instantiate the RequestQueue.
        String url = String.format("%s/gitem/%s", BASE_URL, gItemId);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(null);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.getMessage());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //Set content-type header since a DELETE method without it will fail
                return VolleyQueue.getInstance().getDefaultHeaders();
            }
        };
        request.setRetryPolicy(VolleyQueue.getInstance().getRetryPolicy());

        // Add the request to the RequestQueue.
        VolleyQueue.getInstance().addToRequestQueue(request);
    }

    public int getCurrentGListId() {
        return mCurrentGListId;
    }
    public void setCurrentGListId(int gListId){
        mCurrentGListId = gListId;
    }
}