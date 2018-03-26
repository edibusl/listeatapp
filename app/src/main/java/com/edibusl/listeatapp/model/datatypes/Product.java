package com.edibusl.listeatapp.model.datatypes;

import android.util.Log;

import com.edibusl.listeatapp.helpers.GeneralUtils;
import com.edibusl.listeatapp.mvp.BaseModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Product extends BaseModel<Product> implements Serializable {
    public static final String LOG_TAG = "Product";

    private Long product_id;
    private Long category_id;
    private Long glist_id;

    private String name;
    private String description;
    private String image_path;
    private Category category;

    public static List<Product> parseList(JSONObject jsonObject){
        return BaseModel.parseList(jsonObject, "product", new Product());
    }

    @Override
    public Product createInstance(JSONObject fromJson) {
        return new Product(fromJson);
    }


    public Product() {}

    public Product(JSONObject fromJson){
        if(fromJson == null){
            return;
        }

        try {
            if(fromJson.has("product_id")){
                this.setProduct_id(fromJson.getLong("product_id"));
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

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            if (product_id != null) {
                json.put("product_id", product_id);
            }

            if (category_id != null) {
                json.put("category_id", category_id);
            }

            if (glist_id != null) {
                json.put("glist_id", glist_id);
            }

            if (name != null) {
                json.put("name", name);
            }

            if (description != null) {
                json.put("description", description);
            }

            if (image_path != null) {
                json.put("image_path", image_path);
            }
        } catch(Exception ex){
            Log.e(LOG_TAG, "Error converting product to JSONObject: " + ex.getMessage());
        }

        return json;
    }

    public Long getProduct_id() {
        return product_id;
    }
    public Product setProduct_id(Long product_id) {
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

    public Long getGlistId() {
        return glist_id;
    }
    public Product setGlistId(Long glist_id) {
        this.glist_id = glist_id;
        return this;
    }

    public Long getCategoryId() {
        return category_id;
    }
    public Product setCategoryId(Long category_id) {
        this.category_id = category_id;
        return this;
    }
}
