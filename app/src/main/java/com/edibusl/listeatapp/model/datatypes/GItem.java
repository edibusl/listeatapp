package com.edibusl.listeatapp.model.datatypes;

import java.util.Date;

public class GItem {
    private int gitem_id;
    private Integer quantity;
    private Integer weight;
    private Date created_time;
    private String comments;

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
}
