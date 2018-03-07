package com.edibusl.listeatapp.glist;

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

        void showGListInfo(GList gList);
    }

    interface Presenter extends BasePresenter {

        void loadData();

        void createGItem(@NonNull GItem gItem);

        void deleteGItem(@NonNull long gItemId);


        /*
        //TODO - Apply search
        void setFiltering(TasksFilterType requestType);
        TasksFilterType getFiltering();
        */
    }
}
