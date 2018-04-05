package com.edibusl.listeatapp.mvp;


import com.edibusl.listeatapp.helpers.GeneralUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseModel<T> {
    private static final String LOG_TAG = "BaseModel";

    /**
     * Parse a list of items from a JSONObject
     * Compatible with any model object in the system
     */
    public static <T> List<T> parseList(JSONArray elements, BaseModel classInstance) {
        List<T> lstInstances = new ArrayList<>();
        if(elements == null){
            return lstInstances;
        }

        try {
            for(int i = 0; i < elements.length(); i++){
                lstInstances.add((T)classInstance.createInstance(elements.getJSONObject(i)));
            }
        }
        catch(Exception ex){
            GeneralUtils.printErrorToLog(LOG_TAG, ex);
        }

        return lstInstances;
    }

    public abstract T createInstance(JSONObject fromJson);

    public abstract JSONObject toJson();
}
