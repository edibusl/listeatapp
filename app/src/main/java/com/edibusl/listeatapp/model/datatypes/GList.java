package com.edibusl.listeatapp.model.datatypes;

import android.util.Log;

import com.edibusl.listeatapp.mvp.BaseModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GList extends BaseModel<GList> implements Serializable {
    public static final String LOG_TAG = "GList";

    private long glist_id;
    private String subject;
    private String description;
    private List<User> users = new ArrayList<User>();
    private List<GItem> gitems = new ArrayList<GItem>();

    public static List<GList> parseList(JSONObject jsonObject){
        return BaseModel.parseList(jsonObject, "gList", new GList());
    }

    @Override
    public GList createInstance(JSONObject fromJson) {
        return new GList(fromJson);
    }

    public GList() {}

    public GList(JSONObject fromJson){
        if(fromJson == null){
            return;
        }

        try {
            if (fromJson.has("gList")) {
                parseGList(fromJson.getJSONObject("gList"));
            }
            if(fromJson.has("gItems")){
                //This gItems list may be returned as a JSONObject if there's a single result
                //or as JSONArray if there are multiple results
                Object gItems = fromJson.get("gItems");
                if(gItems instanceof JSONObject){
                    JSONArray arr = new JSONArray();
                    arr.put(0, gItems);
                    parseGItems(arr);
                }else{
                    parseGItems((JSONArray) gItems);
                }
            }

            //If this is a flat glist object, try to parse it immediately
            if(fromJson.has("subject")) {
                parseGList(fromJson);
            }
        }
        catch(Exception ex){
            Log.e(LOG_TAG, ex.toString());
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
            Log.e(LOG_TAG, ex.toString());
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
            Log.e(LOG_TAG, ex.toString());
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
