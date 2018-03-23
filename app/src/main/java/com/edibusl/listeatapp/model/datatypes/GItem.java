package com.edibusl.listeatapp.model.datatypes;

import android.util.Log;

import com.edibusl.listeatapp.helpers.GeneralUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GItem implements Serializable {
    public static final String LOG_TAG = "GItem";

    private Integer gitem_id;

    private Integer user_id;
    private Integer product_id;
    private Integer glist_id;
    private Integer cart_id;

    private Integer quantity;
    private Integer weight;
    private Date created_time;
    private String comments;
    private boolean is_checked;
    private Product product;

    public GItem(){}

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

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try {
            if (gitem_id != null) {
                json.put("gitem_id", gitem_id);
            }

            if (user_id != null) {
                json.put("user_id", user_id);
            }

            if (product_id != null) {
                json.put("product_id", product_id);
            }

            if (glist_id != null) {
                json.put("glist_id", glist_id);
            }

            if (cart_id != null) {
                json.put("cart_id", cart_id);
            }

            if (quantity != null) {
                json.put("quantity", quantity);
            }

            if (weight != null) {
                json.put("weight", weight);
            }

            if (created_time != null) {
                json.put("created_time", created_time.toString());
            }

            if (comments != null) {
                json.put("comments", comments);
            }

//            if (is_checked != null) {
//                json.put("is_checked", is_checked);
//            }
        } catch(Exception ex){
            Log.e(LOG_TAG, "Error converting gItem to JSONObject: " + ex.getMessage());
        }

        return json;
    }

    public Integer getGitemId() {
        return gitem_id;
    }
    public GItem setGitemId(Integer gitem_id) {
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

    public Integer getUserId() {
        return user_id;
    }
    public void setUserId(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getProductId() {
        return product_id;
    }
    public void setProductId(Integer product_id) {
        this.product_id = product_id;
    }

    public Integer getGlistId() {
        return glist_id;
    }
    public void setGlistId(Integer glist_id) {
        this.glist_id = glist_id;
    }

    public Integer getCartId() {
        return cart_id;
    }
    public void setCartId(Integer cart_id) {
        this.cart_id = cart_id;
    }
}
