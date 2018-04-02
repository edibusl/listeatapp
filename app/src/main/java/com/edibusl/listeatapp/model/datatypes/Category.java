package com.edibusl.listeatapp.model.datatypes;

import android.util.Log;

import com.edibusl.listeatapp.helpers.GeneralUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Category implements Serializable {
    public static final String LOG_TAG = "Category";

    private Long category_id;
    private String name;
    private String description;

    public static List<Category> parseList(JSONArray jsonObject){
        List<Category> lstCategories = new ArrayList<>();
        if(jsonObject == null){
            return lstCategories;
        }

        try {
            JSONArray products = jsonObject;
            for(int i = 0; i < products.length(); i++){
                lstCategories.add(new Category(products.getJSONObject(i)));
            }
        }
        catch(Exception ex){
            GeneralUtils.printErrorToLog(LOG_TAG, ex);
        }

        return lstCategories;
    }

    public Category(JSONObject fromJson){
        if(fromJson == null){
            return;
        }

        try {
            if(fromJson.has("category_id")){
                this.setCategory_id(fromJson.getLong("category_id"));
            }

            if(fromJson.has("name")){
                this.setName(fromJson.getString("name"));
            }

            if(fromJson.has("description")){
                this.setDescription(fromJson.getString("description"));
            }
        }
        catch(Exception ex){
            GeneralUtils.printErrorToLog(LOG_TAG, ex);
        }
    }


    public Long getCategory_id() {
        return category_id;
    }
    public Category setCategory_id(Long category_id) {
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
