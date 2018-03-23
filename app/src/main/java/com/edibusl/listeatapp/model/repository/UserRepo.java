package com.edibusl.listeatapp.model.repository;


import com.edibusl.listeatapp.model.datatypes.User;

public class UserRepo {
    private int mCurrentUserId;

    public UserRepo() {
        //TODO - Load user id
        mCurrentUserId = 2;
    }

    public Integer getCurrentUserId() {
        return mCurrentUserId;
    }
    public void setCurrentUserId(int userId) {
        mCurrentUserId = userId;
    }
}
