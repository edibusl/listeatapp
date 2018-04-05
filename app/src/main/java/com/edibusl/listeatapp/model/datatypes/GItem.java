package com.edibusl.listeatapp.model.datatypes;

import android.util.Log;

import com.edibusl.listeatapp.helpers.GeneralUtils;
import com.edibusl.listeatapp.mvp.BaseModel;

import org.json.JSONObject;
import java.io.Serializable;
import java.util.Date;

public class GItem extends BaseModel<GItem> implements Serializable {
    public static final String LOG_TAG = "GItem";

    private Long gitem_id;

    private Long user_id;
    private Long product_id;
    private Long glist_id;
    private Long cart_id;

    private Integer quantity;
    private Integer weight;
    private Date created_date;
    private String comments;
    private Boolean is_checked;
    private Product product;

    public GItem(){}

    public GItem(JSONObject fromJson){
        if(fromJson == null){
            return;
        }

        try {
            if(fromJson.has("gitem_id")){
                this.setGitemId(fromJson.getLong("gitem_id"));
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

            if(fromJson.has("is_checked")){
                this.setIsChecked(fromJson.getBoolean("is_checked"));
            } else {
                this.setIsChecked(false);
            }

            if(fromJson.has("created_date")){
                this.setCreatedDate(GeneralUtils.parseDateFromJsonString(fromJson.getString("created_date")));
            }

            if(fromJson.has("product")){
                this.setProduct(new Product(fromJson.getJSONObject("product")));
            }
        }
        catch(Exception ex){
            GeneralUtils.printErrorToLog(LOG_TAG, ex);
        }
    }

    @Override
    public GItem createInstance(JSONObject fromJson) {
        return new GItem(fromJson);
    }

    @Override
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

            if (created_date != null) {
                json.put("created_date", GeneralUtils.dateToString(created_date));
            }

            if (comments != null) {
                json.put("comments", comments);
            }

            if (is_checked != null) {
                json.put("is_checked", is_checked);
            }
        } catch(Exception ex){
            Log.e(LOG_TAG, "Error converting gItem to JSONObject: ");
            GeneralUtils.printErrorToLog(LOG_TAG, ex);
        }

        return json;
    }

    public Long getGitemId() {
        return gitem_id;
    }
    public GItem setGitemId(Long gitem_id) {
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

    public Date getCreatedDate() {
        return created_date;
    }
    public GItem setCreatedDate(Date created_date) {
        this.created_date = created_date;
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

    public Long getUserId() {
        return user_id;
    }
    public void setUserId(Long user_id) {
        this.user_id = user_id;
    }

    public Long getProductId() {
        return product_id;
    }
    public void setProductId(Long product_id) {
        this.product_id = product_id;
    }

    public Long getGlistId() {
        return glist_id;
    }
    public void setGlistId(Long glist_id) {
        this.glist_id = glist_id;
    }

    public Long getCartId() {
        return cart_id;
    }
    public void setCartId(Long cart_id) {
        this.cart_id = cart_id;
    }
}
