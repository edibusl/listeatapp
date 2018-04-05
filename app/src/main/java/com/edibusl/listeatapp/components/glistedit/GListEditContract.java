package com.edibusl.listeatapp.components.glistedit;

import android.support.annotation.NonNull;

import com.edibusl.listeatapp.model.datatypes.GList;
import com.edibusl.listeatapp.model.datatypes.User;
import com.edibusl.listeatapp.mvp.BasePresenter;
import com.edibusl.listeatapp.mvp.BaseView;

import java.util.List;

public interface GListEditContract {

    interface View extends BaseView<Presenter> {
        void setEditMode();
        void showGList(GList gList);
        void glistCreated();
        void glistUpdated();
    }

    interface Presenter extends BasePresenter {
        void updateGList(@NonNull GList gList, User user);
        List<User> searchUser(String text);
    }
}
