package com.edibusl.listeatapp.mvp;


import android.util.Log;

import com.edibusl.listeatapp.helpers.GeneralUtils;
import com.edibusl.listeatapp.model.datatypes.GList;

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
    public static <T> List<T> parseList(JSONObject jsonObject, String fieldName, BaseModel classInstance) {
        List<T> lstInstances = new ArrayList<>();
        if(jsonObject == null){
            return lstInstances;
        }

        try {
            if (!jsonObject.has(fieldName)) {
                return lstInstances;
            }

            //This product list may be returned as a JSONObject if there's a single result
            //or as JSONArray if there are multiple results
            Object rootElem = jsonObject.get(fieldName);
            if(rootElem instanceof JSONObject){
                lstInstances.add((T)classInstance.createInstance((JSONObject)rootElem));
            }else{
                JSONArray elements = (JSONArray)rootElem;
                for(int i = 0; i < elements.length(); i++){
                    lstInstances.add((T)classInstance.createInstance((JSONObject)elements.getJSONObject(i)));
                }
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
