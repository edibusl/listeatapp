package com.edibusl.listeatapp.model.datatypes;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Category implements Serializable {
    public static final String LOG_TAG = "Category";

    private int category_id;
    private String name;
    private String description;

    public static List<Category> parseList(JSONObject jsonObject){
        List<Category> lstCategories = new ArrayList<>();
        if(jsonObject == null){
            return lstCategories;
        }

        try {
            if (!jsonObject.has("category")) {
                return lstCategories;
            }

            //This category list may be returned as a JSONObject if there's a single result
            //or as JSONArray if there are multiple results
            Object category = jsonObject.get("category");
            if(category instanceof JSONObject){
                lstCategories.add(new Category((JSONObject)category));
            }else{
                JSONArray products = (JSONArray)category;
                for(int i = 0; i < products.length(); i++){
                    lstCategories.add(new Category(products.getJSONObject(i)));
                }
            }
        }
        catch(Exception ex){
            Log.e(LOG_TAG, ex.toString());
        }

        return lstCategories;
    }

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
