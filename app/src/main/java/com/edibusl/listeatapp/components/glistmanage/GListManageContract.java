package com.edibusl.listeatapp.components.glistmanage;

import android.support.annotation.NonNull;

import com.edibusl.listeatapp.model.datatypes.GList;
import com.edibusl.listeatapp.mvp.BasePresenter;
import com.edibusl.listeatapp.mvp.BaseView;

import java.util.List;

public interface GListManageContract {

    interface View extends BaseView<Presenter> {

        void showGListItems(List<GList> gLists);

        void showGListEditInNewActivity(GList gList);

        void setLoadingIndicator(final boolean active);

        void glistDeleted();
    }

    interface Presenter extends BasePresenter {

        void loadData();

        void glistClicked(@NonNull GList item);

        void deleteGListClicked(@NonNull GList item);
    }
}
