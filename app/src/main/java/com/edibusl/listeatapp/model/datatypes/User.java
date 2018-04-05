package com.edibusl.listeatapp.model.datatypes;

import android.util.Log;

import com.edibusl.listeatapp.helpers.GeneralUtils;
import com.edibusl.listeatapp.mvp.BaseModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;


public class User extends BaseModel<User> implements Serializable {
    public static final String LOG_TAG = "User";

    private Long user_id;
    private String username;
    private String name;
    private String profile_image;

    public static List<User> parseList(JSONArray jsonObject){
        return BaseModel.parseList(jsonObject, new User());
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

            if (username != null) {
                json.put("name", name);
            }

            if (profile_image != null) {
                json.put("profile_image", profile_image);
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

    public String getName() {
        return name;
    }
    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getProfileImage() {
        return profile_image;
    }
    public User setProfileImage(String profile_image) {
        this.profile_image = profile_image;
        return this;
    }
}

