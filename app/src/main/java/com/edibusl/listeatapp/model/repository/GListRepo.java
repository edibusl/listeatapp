package com.edibusl.listeatapp.model.repository;


import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.edibusl.listeatapp.helpers.ConfigsManager;
import com.edibusl.listeatapp.helpers.VolleyQueue;
import com.edibusl.listeatapp.model.datatypes.GItem;
import com.edibusl.listeatapp.model.datatypes.GList;
import com.edibusl.listeatapp.model.datatypes.Product;
import com.edibusl.listeatapp.model.datatypes.User;
import com.edibusl.listeatapp.mvp.BaseRepository;

import org.json.JSONObject;

import java.util.List;

public class GListRepo extends BaseRepository {
    public static final String LOG_TAG = "GListRepo";

    //TODO - Move to configs
    private final String BASE_URL = "http://10.100.102.7:9090";
    private final String CURRENT_GLIST_ID_KEY = "current_glist_id";

    private Long mCurrentGListId;

    public GListRepo() {
        long savedCurrentGListId = ConfigsManager.getInstance().getLong(CURRENT_GLIST_ID_KEY);
        mCurrentGListId = savedCurrentGListId;
        mBaseUrl = BASE_URL;
    }

    public Long getCurrentGListId() {
        return mCurrentGListId;
    }
    public void setCurrentGListId(Long gListId){
        mCurrentGListId = gListId;
        ConfigsManager.getInstance().setLong(CURRENT_GLIST_ID_KEY, gListId);
    }

    public void getGListFullInfo(Long glistId, @NonNull final AppData.LoadDataCallback callback) {
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

    public void getAllUserGLists(Long userId, @NonNull final AppData.LoadDataCallback callback) {
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
        String url = String.format("%s/glist/addUserToGList", BASE_URL);

        //Request data
        JSONObject requestData = new JSONObject();
        try {
            requestData.put("user_id", userId);
            requestData.put("glist_id", glistId);
        } catch(Exception ex) {
            Log.e(LOG_TAG, ex.getMessage());
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
}