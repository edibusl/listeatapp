package com.edibusl.listeatapp.components.settings;

import android.support.annotation.NonNull;

import com.edibusl.listeatapp.model.datatypes.GList;
import com.edibusl.listeatapp.model.datatypes.User;
import com.edibusl.listeatapp.mvp.BasePresenter;
import com.edibusl.listeatapp.mvp.BaseView;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface SettingsContract {

    interface View extends BaseView<Presenter> {
        void loadSettings(String serverUrl);
        void close();
    }

    interface Presenter extends BasePresenter {
        void updateSettings(String serverUrl);
    }
}
