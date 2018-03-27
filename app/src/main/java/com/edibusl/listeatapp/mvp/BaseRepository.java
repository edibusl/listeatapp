package com.edibusl.listeatapp.mvp;

import android.support.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.edibusl.listeatapp.helpers.VolleyQueue;
import com.edibusl.listeatapp.model.repository.AppData;

import org.json.JSONObject;

import java.util.Map;

public abstract class BaseRepository {
    protected String mBaseUrl;

    public void deleteEntity(String entityRestPath, Long id, Long parentId, @NonNull final AppData.LoadDataCallback callback) {
        //Instantiate the RequestQueue.
        String url = String.format("%s/%s/%s", mBaseUrl, entityRestPath, id);

        //Add parent id for Many-To-Many relations
        if(parentId != null) {
            url = String.format("%s/%s", url, parentId);
        }

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
                        callback.onError(error.toString());
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

    public void updateEntity(String entityRestPath, final BaseModel entity, Long id, Long parentId, @NonNull final AppData.LoadDataCallback callback) {
        String url = String.format("%s/%s", mBaseUrl, entityRestPath);

        //Decide about the request method according to edit mode / new mode
        int method = (id == null || id == 0 ? Request.Method.POST : Request.Method.PUT);
        if(method == Request.Method.POST) {
            //Add parent id in case of PUT
            url = String.format("%s/%s", url, parentId.toString());
        }

        JsonObjectRequest request = new JsonObjectRequest(method, url, entity.toJson(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(entity.createInstance(response));
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
}
