package com.edibusl.listeatapp.model.datatypes;

import android.util.Log;

import com.edibusl.listeatapp.helpers.GeneralUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GItem {
    public static final String LOG_TAG = "GItem";

    private int gitem_id;
    private Integer quantity;
    private Integer weight;
    private Date created_time;
    private String comments;
    private boolean is_checked;
    private Product product;

    public GItem(JSONObject fromJson){
        if(fromJson == null){
            return;
        }

        try {
            if(fromJson.has("gitem_id")){
                this.setGitemId(fromJson.getInt("gitem_id"));
            }
            if(fromJson.has("quantity")){
                this.setQuantity(fromJson.getInt("quantity"));
            }
            if(fromJson.has("weight")){
                this.setWeight(fromJson.getInt("weight"));
            }
            if(fromJson.has("comments")){
                this.setComments(fromJson.getString("comments"));
            }
            if(fromJson.has("created_time")){
                this.setCreatedTime(GeneralUtils.parseDateFromJsonString(fromJson.getString("created_time")));
            }

            if(fromJson.has("product_obj")){
                this.setProduct(new Product(fromJson.getJSONObject("product_obj")));
            }

            //TODO - Continue parsing of the Product and Category
            //Then in the fragment show the product name, not the comments
        }
        catch(Exception ex){
            Log.e(LOG_TAG, ex.toString());
        }
    }

    public int getGitemId() {
        return gitem_id;
    }
    public GItem setGitemId(int gitem_id) {
        this.gitem_id = gitem_id;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }
    public GItem setQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public Integer getWeight() {
        return weight;
    }
    public GItem setWeight(Integer weight) {
        this.weight = weight;
        return this;
    }

    public Date getCreatedTime() {
        return created_time;
    }
    public GItem setCreatedTime(Date created_time) {
        this.created_time = created_time;
        return this;
    }

    public String getComments() {
        return comments;
    }
    public GItem setComments(String comments) {
        this.comments = comments;
        return this;
    }

    public boolean getIsChecked() {
        return is_checked;
    }
    public GItem setIsChecked(boolean is_checked) {
        this.is_checked = is_checked;
        return this;
    }

    public Product getProduct(){return product;}
    public GItem setProduct(Product product){
        this.product = product;
        return this;
    }
}
