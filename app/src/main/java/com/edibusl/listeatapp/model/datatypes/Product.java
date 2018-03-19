package com.edibusl.listeatapp.model.datatypes;

import android.util.Log;

import com.edibusl.listeatapp.helpers.GeneralUtils;

import org.json.JSONObject;

public class Product {
    public static final String LOG_TAG = "Product";

    private int product_id;
    private String name;
    private String description;
    private String image_path;
    private Category category;

    public Product(JSONObject fromJson){
        if(fromJson == null){
            return;
        }

        try {
            if(fromJson.has("product_id")){
                this.setProduct_id(fromJson.getInt("product_id"));
            }

            if(fromJson.has("name")){
                this.setName(fromJson.getString("name"));
            }

            if(fromJson.has("description")){
                this.setDescription(fromJson.getString("description"));
            }

            if(fromJson.has("image_path")){
                this.setImage_path(fromJson.getString("image_path"));
            }

            if(fromJson.has("category")){
                this.setCategory(new Category(fromJson.getJSONObject("category")));
            }
        }
        catch(Exception ex){
            Log.e(LOG_TAG, ex.toString());
        }
    }

    public int getProduct_id() {
        return product_id;
    }
    public Product setProduct_id(int product_id) {
        this.product_id = product_id;
        return this;
    }

    public String getName() {
        return name;
    }
    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }
    public Product setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImage_path() {
        return image_path;
    }
    public Product setImage_path(String image_path) {
        this.image_path = image_path;
        return this;
    }

    public Category getCategory() {
        return category;
    }
    public Product setCategory(Category category) {
        this.category = category;
        return this;
    }
}
