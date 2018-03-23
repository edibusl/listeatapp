package com.edibusl.listeatapp.glist;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.edibusl.listeatapp.model.datatypes.GItem;
import com.edibusl.listeatapp.model.datatypes.GList;
import com.edibusl.listeatapp.model.repository.AppData;
        
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link GListFragment}), retrieves the data and updates the
 * UI as required.
 */
public class GListPresenter implements GListContract.Presenter {
    public static final String LOG_TAG = "GListPresenter";

    private final AppData mAppData;
    private final GListContract.View mGListView;
    private int mCurGListId;

    public GListPresenter(@NonNull AppData appData, @NonNull GListContract.View glistView) {
        mAppData = checkNotNull(appData, "appData cannot be null");
        mGListView = checkNotNull(glistView, "glistView cannot be null!");

        mGListView.setPresenter(this);

        mCurGListId = mAppData.GListRepo().getCurrentGListId();
    }

    @Override
    public void start() {
        loadData();
    }

    @Override
    public void loadData() {
        mGListView.setLoadingIndicator(true);

        mAppData.GListRepo().getGListFullInfo(mCurGListId, new AppData.LoadDataCallback() {
            @Override
            public void onSuccess(Object data) {
                GList gList = (GList)data;
                mGListView.showGItems(gList.getGitems());

                //TODO - Show list info

                mGListView.setLoadingIndicator(false);
            }

            @Override
            public void onError(String error) {
                Log.e(LOG_TAG, error);
                mGListView.setLoadingIndicator(false);
            }
        });
    }

    @Override
    public void gItemClicked(@NonNull GItem gItem) {
        mGListView.showGItemInNewActivity(gItem);
    }
}
