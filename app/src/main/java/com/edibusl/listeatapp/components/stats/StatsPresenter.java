package com.edibusl.listeatapp.components.stats;

import android.support.annotation.NonNull;
import com.edibusl.listeatapp.model.repository.AppData;
import org.json.JSONArray;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link StatsFragment}), retrieves the data and updates the
 * UI as required.
 */
public class StatsPresenter implements StatsContract.Presenter {
    public static final String LOG_TAG = "StatsPresenter";

    private final AppData mAppData;
    private final StatsContract.View mStatsView;

    public StatsPresenter(@NonNull StatsContract.View view) {
        mAppData = AppData.getInstance();
        mStatsView = checkNotNull(view, "view cannot be null!");
        mStatsView.setPresenter(this);
    }

    @Override
    public void start() {
        mAppData.StatsRepo().getStatsByCategory(mAppData.UserRepo().getCurrentUserId(), new AppData.LoadDataCallback() {
            @Override
            public void onSuccess(Object data) {
                mStatsView.loadStats((JSONArray)data);
            }

            @Override
            public void onError(String error) {

            }
        });
    }
}
