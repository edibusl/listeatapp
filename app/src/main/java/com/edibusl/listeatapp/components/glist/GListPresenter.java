package com.edibusl.listeatapp.components.glist;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.annotation.NonNull;
import android.util.Log;

import com.edibusl.listeatapp.helpers.GeneralUtils;
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

    public GListPresenter(@NonNull GListContract.View view, ActionBar actionBar) {
        mAppData = AppData.getInstance();
        mGListView = checkNotNull(view, "view cannot be null!");
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
                GeneralUtils.printErrorToLog(LOG_TAG, error);
                mGListView.setLoadingIndicator(false);
            }
        });
    }

    @Override
    public void gItemClicked(@NonNull GItem gItem) {
        mGListView.showGItemInNewActivity(gItem);
    }

    @Override
    public void checkGItem(GItem gItem, final boolean value) {
        gItem.setIsChecked(value);
        setGItemFKFields(gItem);
        mAppData.GListRepo().updateGItem(gItem, new AppData.LoadDataCallback() {
            @Override
            public void onSuccess(Object data) {
                Log.i(LOG_TAG, "checkGItem - success");
            }

            @Override
            public void onError(String error) {
                GeneralUtils.printErrorToLog(LOG_TAG, error);
            }
        });
    }

    @Override
    public String getProductImageFullPath(String imagePath) {
        return mAppData.ProductRepo().getProductThumbnailUrl(imagePath);
    }

    @Override
    public void onPurchaseClicked() {
        mAppData.GListRepo().purchase(mAppData.GListRepo().getCurrentGListId(), new AppData.LoadDataCallback() {
            @Override
            public void onSuccess(Object data) {
                mGListView.showPurchaseMade();
                loadData();
            }

            @Override
            public void onError(String error) {
                GeneralUtils.printErrorToLog(LOG_TAG, error);
            }
        });
    }


    private void setGItemFKFields(GItem gItem) {
        gItem.setGlistId(mAppData.GListRepo().getCurrentGListId());
        gItem.setUserId(mAppData.UserRepo().getCurrentUserId());
        gItem.setProductId(gItem.getProduct().getProduct_id());
    }
}
