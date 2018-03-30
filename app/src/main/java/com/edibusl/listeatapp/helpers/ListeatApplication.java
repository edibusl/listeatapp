package com.edibusl.listeatapp.helpers;

import android.app.Application;

import com.amazonaws.mobile.client.AWSMobileClient;

public class ListeatApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        //Init manager singletons with the application context
        VolleyQueue.getInstance().init(this);
        ConfigsManager.getInstance().init(this);
    }
}
