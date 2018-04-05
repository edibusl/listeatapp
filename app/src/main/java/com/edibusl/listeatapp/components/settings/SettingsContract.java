package com.edibusl.listeatapp.components.settings;

import com.edibusl.listeatapp.mvp.BasePresenter;
import com.edibusl.listeatapp.mvp.BaseView;

public interface SettingsContract {

    interface View extends BaseView<Presenter> {
        void loadSettings(String serverUrl);
        void close();
    }

    interface Presenter extends BasePresenter {
        void updateSettings(String serverUrl);
    }
}
