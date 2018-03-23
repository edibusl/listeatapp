package com.edibusl.listeatapp.model.datatypes;

import android.util.Log;

import com.edibusl.listeatapp.helpers.GeneralUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Product implements Serializable {
    public static final String LOG_TAG = "Product";

    private int product_id;
    private String name;
    private String description;
    private String image_path;
    private Category category;

    public static List<Product> parseList(JSONObject jsonObject){
        List<Product> lstProducts = new ArrayList<>();
        if(jsonObject == null){
            return lstProducts;
        }

        try {
            if (!jsonObject.has("product")) {
                return lstProducts;
            }

            //This product list may be returned as a JSONObject if there's a single result
            //or as JSONArray if there are multiple results
            Object product = jsonObject.get("product");
            if(product instanceof JSONObject){
                lstProducts.add(new Product((JSONObject)product));
            }else{
                JSONArray products = (JSONArray)product;
                for(int i = 0; i < products.length(); i++){
                    lstProducts.add(new Product(products.getJSONObject(i)));
                }
            }
        }
        catch(Exception ex){
            Log.e(LOG_TAG, ex.toString());
        }

        return lstProducts;
    }

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
