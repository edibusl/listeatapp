package com.edibusl.listeatapp.model.repository;


import com.edibusl.listeatapp.model.datatypes.User;

public class UserRepo {
    private Long mCurrentUserId;

    public UserRepo() {
        //TODO - Load user id
        mCurrentUserId = 2L;
    }

    public Long getCurrentUserId() {
        return mCurrentUserId;
    }
    public void setCurrentUserId(Long userId) {
        mCurrentUserId = userId;
    }
}
