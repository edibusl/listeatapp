package com.edibusl.listeatapp.helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class ConfigsManager {
    private static final String PREFERENCE_FILE_KEY = "preferences_file";
    private Context mCtx;
    private SharedPreferences mSharedPref;

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
        mCtx = context;

        mSharedPref = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
    }

    //Implementing a function for every type instead of casting to/from Object
    //(in order to prevent boxing/unboxing, thus increasing performance)
    public int getInt(String key) {
        return mSharedPref.getInt(key, 0);
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
        return mSharedPref.getLong(key, 0);
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
        return mSharedPref.getString(key, null);
    }

    public void setString(String key, String val) {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putString(key, val);
        editor.commit();
    }
}
