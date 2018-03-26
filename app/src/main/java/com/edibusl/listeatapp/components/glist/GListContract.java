package com.edibusl.listeatapp.components.glist;

import android.support.annotation.NonNull;

import com.edibusl.listeatapp.mvp.BaseView;
import com.edibusl.listeatapp.mvp.BasePresenter;
import com.edibusl.listeatapp.model.datatypes.GList;
import com.edibusl.listeatapp.model.datatypes.GItem;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface GListContract {

    interface View extends BaseView<Presenter> {

        void showGItems(List<GItem> gItems);

        void showGItemInNewActivity(GItem gItem);

        void setLoadingIndicator(final boolean active);

        void openGListsManage();
    }

    interface Presenter extends BasePresenter {

        void loadData();

        void gItemClicked(@NonNull GItem gItem);

        String getProductImageFullPath(String imagePath);

        /*
        //TODO - Apply search
        void setFiltering(TasksFilterType requestType);
        TasksFilterType getFiltering();
        */
    }
}
