package com.edibusl.listeatapp.components.glist;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.annotation.NonNull;
import android.util.Log;

import com.edibusl.listeatapp.model.datatypes.GItem;
import com.edibusl.listeatapp.model.datatypes.GList;
import com.edibusl.listeatapp.model.repository.AppData;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link GListFragment}), retrieves the data and updates the
 * UI as required.
 */
public class GListPresenter implements GListContract.Presenter {
    public static final String LOG_TAG = "GListPresenter";

    private AppData mAppData;
    private GListContract.View mGListView;
    private Long mCurGListId;
    private ActionBar mActionBar;

    public GListPresenter(@NonNull AppData appData, @NonNull GListContract.View glistView, ActionBar actionBar) {
        mAppData = checkNotNull(appData, "appData cannot be null");
        mGListView = checkNotNull(glistView, "glistView cannot be null!");
        mGListView.setPresenter(this);

        mActionBar = actionBar;
        mCurGListId = mAppData.GListRepo().getCurrentGListId();
    }

    @Override
    public void start() {
        mCurGListId = mAppData.GListRepo().getCurrentGListId();
        if(mCurGListId == null || mCurGListId == 0) {
            //No current glist is set. Redirect to glist manage activity
            mGListView.openGListsManage();
        } else {
            loadData();
        }
    }

    @Override
    public void loadData() {
        mGListView.setLoadingIndicator(true);

        mAppData.GListRepo().getGListFullInfo(mCurGListId, new AppData.LoadDataCallback() {
            @Override
            public void onSuccess(Object data) {
                GList gList = (GList)data;

                mActionBar.setTitle(gList.getSubject());
                mGListView.showGItems(gList.getGitems());
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

    @Override
    public String getProductImageFullPath(String imagePath) {
        return mAppData.ProductRepo().getProductThumbnailUrl(imagePath);
    }
}
