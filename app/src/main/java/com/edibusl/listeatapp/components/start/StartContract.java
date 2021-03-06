package com.edibusl.listeatapp.components.start;

import com.edibusl.listeatapp.mvp.BasePresenter;
import com.edibusl.listeatapp.mvp.BaseView;

public interface StartContract {

    interface View extends BaseView<Presenter> {
        void showLoginError();
        void moveToGListActivity();
    }

    interface Presenter extends BasePresenter {
        void authenticate();
    }
}
