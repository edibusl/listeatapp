package com.edibusl.listeatapp.components.glistmanage;

import android.support.annotation.NonNull;

import com.edibusl.listeatapp.helpers.GeneralUtils;
import com.edibusl.listeatapp.model.datatypes.GList;
import com.edibusl.listeatapp.model.repository.AppData;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class GListManagePresenter implements GListManageContract.Presenter {
    public static final String LOG_TAG = "GListManagePresenter";

    private final AppData mAppData;
    private final GListManageContract.View mGListManageView;

    public GListManagePresenter(@NonNull GListManageContract.View glistManageView) {
        mAppData = AppData.getInstance();
        mGListManageView = checkNotNull(glistManageView, "view cannot be null!");

        mGListManageView.setPresenter(this);
    }

    @Override
    public void start() {
        loadData();
    }

    @Override
    public void loadData() {
        mGListManageView.setLoadingIndicator(true);

        mAppData.GListRepo().getAllUserGLists(mAppData.UserRepo().getCurrentUserId(), new AppData.LoadDataCallback() {
            @Override
            public void onSuccess(Object data) {
                mGListManageView.showGListItems((List<GList>)data);
                mGListManageView.setLoadingIndicator(false);
            }

            @Override
            public void onError(String error) {
                GeneralUtils.printErrorToLog(LOG_TAG, error);
                mGListManageView.setLoadingIndicator(false);
            }
        });
    }

    @Override
    public void glistClicked(@NonNull GList gList) {
        mGListManageView.showGListEditInNewActivity(gList);
        mAppData.GListRepo().setCurrentGListId(gList.getGlist_id());
    }

    @Override
    public void deleteGListClicked(@NonNull GList item) {
        mAppData.GListRepo().deleteGList(item.getGlist_id(), new AppData.LoadDataCallback() {
            @Override
            public void onSuccess(Object data) {
                mAppData.GListRepo().setCurrentGListId(null);
                mGListManageView.glistDeleted();
            }

            @Override
            public void onError(String error) {
                GeneralUtils.printErrorToLog(LOG_TAG, error);
            }
        });
    }
}
