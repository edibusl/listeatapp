package com.edibusl.listeatapp.model.repository;


import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.edibusl.listeatapp.helpers.VolleyQueue;
import com.edibusl.listeatapp.model.datatypes.User;
import com.edibusl.listeatapp.mvp.BaseRepository;

import org.json.JSONObject;

import java.util.List;

public class UserRepo extends BaseRepository {
    private Long mCurrentUserId;

    public UserRepo() {
        //TODO - Load user id
        mCurrentUserId = 2L;
    }

    public Long getCurrentUserId() {
        return mCurrentUserId;
    }
    public void setCurrentUserId(Long userId) {
        mCurrentUserId = userId;
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

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, future, future);

        // Add the request to the RequestQueue.
        VolleyQueue.getInstance().addToRequestQueue(request);

        try {
            JSONObject response = future.get();
            List<User> lstUsers = User.parseList(response);

            return lstUsers;
        } catch (Exception e) {
            throw e;
        }
    }
}
