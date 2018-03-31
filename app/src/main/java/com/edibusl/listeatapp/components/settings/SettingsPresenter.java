package com.edibusl.listeatapp.components.settings;

import android.support.annotation.NonNull;

import com.edibusl.listeatapp.helpers.ConfigsManager;
import com.edibusl.listeatapp.model.repository.AppData;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link SettingsFragment}), retrieves the data and updates the
 * UI as required.
 */
public class SettingsPresenter implements SettingsContract.Presenter {
    public static final String LOG_TAG = "SettingsPresenter";

    private final AppData mAppData;
    private final SettingsContract.View mSettingsView;

    public SettingsPresenter(@NonNull SettingsContract.View view) {
        mAppData = AppData.getInstance();
        mSettingsView = checkNotNull(view, "view cannot be null!");
        mSettingsView.setPresenter(this);
    }

    @Override
    public void start() {
        mSettingsView.loadSettings(ConfigsManager.getInstance().getString(ConfigsManager.KEY_SERVER_URL));
    }

    @Override
    public void updateSettings(String serverUrl) {
        checkNotNull(serverUrl, "serverUrl cannot be null!");

        ConfigsManager.getInstance().setString(ConfigsManager.KEY_SERVER_URL, serverUrl);
        mSettingsView.close();
    }
}
