package com.edibusl.listeatapp.model.repository;

public class AppData {
    //Interface for the use of repositories
    public static interface LoadDataCallback {
        void onSuccess(Object data);
        void onError(String error);
    }

    //Singleton functions
    private static AppData instance = null;
    private AppData(){};
    public static synchronized AppData getInstance(){
        if(instance == null){
            instance = new AppData();
        }
        return instance;
    }


    private GListRepo mGListRepo = new GListRepo();
    private UserRepo mUserRepo = new UserRepo();

    public GListRepo GListRepo(){
        return mGListRepo;
    }

    public UserRepo UserRepo(){
        return mUserRepo;
    }
}
