package com.edibusl.listeatapp.components.glistmanage;

import android.support.annotation.NonNull;

import com.edibusl.listeatapp.model.datatypes.GItem;
import com.edibusl.listeatapp.model.datatypes.GList;
import com.edibusl.listeatapp.mvp.BasePresenter;
import com.edibusl.listeatapp.mvp.BaseView;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface GListManageContract {

    interface View extends BaseView<Presenter> {

        void showGListItems(List<GList> gLists);

        void showGListEditInNewActivity(GList gList);

        void setLoadingIndicator(final boolean active);
    }

    interface Presenter extends BasePresenter {

        void loadData();

        void glistClicked(@NonNull GList item);
    }
}
