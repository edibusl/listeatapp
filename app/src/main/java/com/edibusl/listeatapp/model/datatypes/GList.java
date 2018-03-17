package com.edibusl.listeatapp.model.datatypes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GList {
    private long glist_id;
    private String subject;
    private String description;
    private List<User> users = new ArrayList<User>();
    private List<GItem> gitems = new ArrayList<GItem>();

    public GList(JSONObject fromJson){
        if(fromJson == null){
            return;
        }

        try {
            if (fromJson.has("gList")) {
                parseGList(fromJson.getJSONObject("gList"));
            }
            if(fromJson.has("gItems")){
                parseGItems(fromJson.getJSONArray("gItems"));
            }
        }
        catch(Exception ex){
            //TODO - Write to log
        }
    }

    private void parseGList(JSONObject gList){
        try {
            if(gList.has("subject")){
                this.setGlist_id(gList.getLong("glist_id"));
                this.setSubject(gList.getString("subject"));
                this.setDescription(gList.getString("description"));
            }
        }
        catch(Exception ex){
            //TODO - Write to log
        }
    }

    private void parseGItems(JSONArray gItems){
        try
        {
            for(int i = 0; i < gItems.length(); i++){
                this.gitems.add(new GItem(gItems.getJSONObject(i)));
            }
        }
        catch (Exception ex)
        {
            //TODO - Write to log
        }
    }

    public long getGlist_id() {
        return glist_id;
    }

    public void setGlist_id(long glist_id) {
        this.glist_id = glist_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<GItem> getGitems() {
        return gitems;
    }

    public void setGitems(List<GItem> gitems) {
        this.gitems = gitems;
    }
}
