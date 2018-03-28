package com.edibusl.listeatapp.model.datatypes;

import android.util.Log;

import com.edibusl.listeatapp.helpers.GeneralUtils;
import com.edibusl.listeatapp.mvp.BaseModel;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;


public class User extends BaseModel<User> implements Serializable {
    public static final String LOG_TAG = "User";

    private Long user_id;
    private String username;

    public static List<User> parseList(JSONObject jsonObject){
        return BaseModel.parseList(jsonObject, "user", new User());
    }

    @Override
    public User createInstance(JSONObject fromJson) {
        return new User(fromJson);
    }

    public User(){}

    public User(JSONObject fromJson){
        if(fromJson == null){
            return;
        }

        try {
            if(fromJson.has("user_id")){
                this.setUser_id(fromJson.getLong("user_id"));
            }

            if(fromJson.has("username")){
                this.setUserName(fromJson.getString("username"));
            }
        }
        catch(Exception ex){
            GeneralUtils.printErrorToLog(LOG_TAG, ex);
        }
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            if (user_id != null) {
                json.put("user_id", user_id);
            }

            if (username != null) {
                json.put("username", username);
            }
        } catch(Exception ex){
            Log.e(LOG_TAG, "Error converting user to JSONObject: ");
            GeneralUtils.printErrorToLog(LOG_TAG, ex);
        }

        return json;
    }

    public Long getUser_id() {
        return user_id;
    }
    public User setUser_id(Long user_id) {
        this.user_id = user_id;
        return this;
    }

    public String getUserName() {
        return username;
    }
    public User setUserName(String username) {
        this.username = username;
        return this;
    }
}

