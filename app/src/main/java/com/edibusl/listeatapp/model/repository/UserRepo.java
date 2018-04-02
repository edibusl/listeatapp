package com.edibusl.listeatapp.model.repository;


import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.edibusl.listeatapp.helpers.ConfigsManager;
import com.edibusl.listeatapp.helpers.VolleyQueue;
import com.edibusl.listeatapp.model.datatypes.User;
import com.edibusl.listeatapp.mvp.BaseRepository;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class UserRepo extends BaseRepository {
    private Long mCurrentUserId;

    public UserRepo() {
        long currentUserId = ConfigsManager.getInstance().getLong(ConfigsManager.KEY_CURRENT_USER_ID);
        mCurrentUserId = (currentUserId == 0) ? null : currentUserId;
    }

    public Long getCurrentUserId() {
        return mCurrentUserId;
    }
    public void setCurrentUserId(Long userId) {
        mCurrentUserId = userId;
        ConfigsManager.getInstance().setLong(ConfigsManager.KEY_CURRENT_USER_ID, mCurrentUserId);
    }

    /**
     * A blocking call to get auto complete results of a users list
     * @param text Text search pattern
     * @return A list of users matching the text
     * @throws Exception
     */
    public List<User> getUsersByAutoComplete(String text) throws Exception{
        //Instantiate the RequestQueue.
        String url = String.format("%s/user/autocomplete/%s", getBaseUrl(), text);

        RequestFuture<JSONArray> future = RequestFuture.newFuture();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, future, future);

        // Add the request to the RequestQueue.
        VolleyQueue.getInstance().addToRequestQueue(request);

        try {
            JSONArray response = future.get();
            List<User> lstUsers = User.parseList(response);

            return lstUsers;
        } catch (Exception e) {
            throw e;
        }
    }

    public void login(User user, @NonNull final AppData.LoadDataCallback callback) {
        //Instantiate the RequestQueue.
        String url = String.format("%s/user/login", getBaseUrl());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, user.toJson(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Update current user
                        try {
                            //Parse user id and save it
                            setCurrentUserId(response.getLong("user_id"));


                            //Notify caller
                            callback.onSuccess(null);
                        }catch(Exception ex) {
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

    public void logout() {
        setCurrentUserId(null);
        AppData.getInstance().GListRepo().setCurrentGListId(null);
    }
}
