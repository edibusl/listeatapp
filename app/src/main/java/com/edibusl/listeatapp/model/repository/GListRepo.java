package com.edibusl.listeatapp.model.repository;


import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.edibusl.listeatapp.helpers.ConfigsManager;
import com.edibusl.listeatapp.helpers.GeneralUtils;
import com.edibusl.listeatapp.helpers.VolleyQueue;
import com.edibusl.listeatapp.model.datatypes.GItem;
import com.edibusl.listeatapp.model.datatypes.GList;
import com.edibusl.listeatapp.mvp.BaseRepository;

import org.json.JSONArray;
import org.json.JSONObject;

public class GListRepo extends BaseRepository {
    public static final String LOG_TAG = "GListRepo";
    private Long mCurrentGListId;

    public GListRepo() {
        long savedCurrentGListId = ConfigsManager.getInstance().getLong(ConfigsManager.KEY_CURRENT_GLIST_ID);
        mCurrentGListId = savedCurrentGListId;
    }

    public Long getCurrentGListId() {
        return mCurrentGListId;
    }
    public void setCurrentGListId(Long gListId){
        mCurrentGListId = gListId;
        ConfigsManager.getInstance().setLong(ConfigsManager.KEY_CURRENT_GLIST_ID, gListId);
    }

    public void getGListFullInfo(Long glistId, @NonNull final AppData.LoadDataCallback callback) {
        //Instantiate the RequestQueue.
        String url = String.format("%s/glist/fullInfo/%s", getBaseUrl(), String.valueOf(glistId));

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

    public void getAllUserGLists(Long userId, @NonNull final AppData.LoadDataCallback callback) {
        //Instantiate the RequestQueue.
        String url = String.format("%s/glist/all/%s", getBaseUrl(), String.valueOf(userId));

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
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
        updateEntity("gitem", gItem, gItem.getGitemId(), gItem.getGlistId(), callback);
    }

    public void deleteGItem(Long gItemId, @NonNull final AppData.LoadDataCallback callback) {
        deleteEntity("gitem", gItemId, null, callback);
    }

    public void updateGList(GList gList, @NonNull final AppData.LoadDataCallback callback) {
        updateEntity("glist", gList, gList.getGlist_id(), AppData.getInstance().UserRepo().getCurrentUserId(), callback);
    }

    public void deleteGList(Long gListId, @NonNull final AppData.LoadDataCallback callback) {
        deleteEntity("glist", gListId, AppData.getInstance().UserRepo().getCurrentUserId(), callback);
    }

    public void addUserToGList(Long userId, Long glistId, @NonNull final AppData.LoadDataCallback callback) {
        //Instantiate the RequestQueue.
        String url = String.format("%s/glist/addUserToGList", getBaseUrl());

        //Request data
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("user_id", userId);
            requestData.put("glist_id", glistId);
        } catch(Exception ex) {
            GeneralUtils.printErrorToLog(LOG_TAG, ex);
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, requestData,
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

        // Add the request to the RequestQueue.
        VolleyQueue.getInstance().addToRequestQueue(request);
    }

    public void purchase(Long glistId, @NonNull final AppData.LoadDataCallback callback) {
        //Instantiate the RequestQueue.
        String url = String.format("%s/glist/purchase/%s", getBaseUrl(), glistId);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
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

        // Add the request to the RequestQueue.
        VolleyQueue.getInstance().addToRequestQueue(request);
    }
}