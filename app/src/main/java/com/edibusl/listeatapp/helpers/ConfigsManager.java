package com.edibusl.listeatapp.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class ConfigsManager {
    private static final String PREFERENCE_FILE_KEY = "preferences_file";
    public static final String KEY_SERVER_URL = "server_url";
    public static final String KEY_CURRENT_GLIST_ID = "current_glist_id";
    public static final String KEY_CURRENT_USER_ID = "current_user_id";
    public static final String KEY_PRODUCT_THUMBNAIL_BASE_URL = "product_thumbnail_base_url";

    private SharedPreferences mSharedPref;
    private static HashMap<String, Object> mDefaults;
    static
    {
        mDefaults = new HashMap<>();
        mDefaults.put(KEY_SERVER_URL, "http://10.100.102.7:9090");
        mDefaults.put(KEY_PRODUCT_THUMBNAIL_BASE_URL, "https://s3.eu-central-1.amazonaws.com/listeatapp-userfiles-mobilehub-1030236591/public");
    }

    //Singleton
    private static ConfigsManager mInstance;
    private ConfigsManager() {}
    public static synchronized ConfigsManager getInstance() {
        if (mInstance == null) {
            mInstance = new ConfigsManager();
        }
        return mInstance;
    }

    public void init(Context context){
        mSharedPref = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
    }

    //Implementing a function for every type instead of casting to/from Object
    //(in order to prevent boxing/unboxing, thus increasing performance)
    public int getInt(String key) {
        int defaultVal = 0;
        if(mDefaults.containsKey(key)) {
            defaultVal = (int)mDefaults.get(key);
        }
        return mSharedPref.getInt(key, defaultVal);
    }

    public void setInt(String key, Integer val) {
        if(val == null) {
            val = 0;
        }

        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt(key, val);
        editor.commit();
    }

    public long getLong(String key) {
        long defaultVal = 0;
        if(mDefaults.containsKey(key)) {
            defaultVal = (long)mDefaults.get(key);
        }
        return mSharedPref.getLong(key, defaultVal);
    }

    public void setLong(String key, Long val) {
        if(val == null) {
            val = 0L;
        }

        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putLong(key, val);
        editor.commit();
    }

    public String getString(String key) {
        String defaultVal = null;
        if(mDefaults.containsKey(key)) {
            defaultVal = (String)mDefaults.get(key);
        }
        return mSharedPref.getString(key, defaultVal);
    }

    public void setString(String key, String val) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(key, val);
        editor.commit();
    }
}
