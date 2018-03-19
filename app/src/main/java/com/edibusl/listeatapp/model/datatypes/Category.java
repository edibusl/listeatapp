package com.edibusl.listeatapp.model.datatypes;

import android.util.Log;

import org.json.JSONObject;

public class Category {
    public static final String LOG_TAG = "Category";

    private int category_id;
    private String name;
    private String description;

    public Category(JSONObject fromJson){
        if(fromJson == null){
            return;
        }

        try {
            if(fromJson.has("category_id")){
                this.setCategory_id(fromJson.getInt("category_id"));
            }

            if(fromJson.has("name")){
                this.setName(fromJson.getString("name"));
            }

            if(fromJson.has("description")){
                this.setDescription(fromJson.getString("description"));
            }
        }
        catch(Exception ex){
            Log.e(LOG_TAG, ex.toString());
        }
    }

    public int getCategory_id() {
        return category_id;
    }
    public Category setCategory_id(int category_id) {
        this.category_id = category_id;
        return this;
    }

    public String getName() {
        return name;
    }
    public Category setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }
    public Category setDescription(String description) {
        this.description = description;
        return this;
    }
}
