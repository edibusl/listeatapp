package com.edibusl.listeatapp.components.glistedit;

import android.support.annotation.NonNull;

import com.edibusl.listeatapp.model.datatypes.GList;
import com.edibusl.listeatapp.model.datatypes.Product;
import com.edibusl.listeatapp.mvp.BasePresenter;
import com.edibusl.listeatapp.mvp.BaseView;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface GListEditContract {

    interface View extends BaseView<Presenter> {
        void setEditMode();
        void showGList(GList gList);
        void glistCreated();
        void glistUpdated();
    }

    interface Presenter extends BasePresenter {
        void createGList(@NonNull GList gList);
        void updateGList(@NonNull GList gList);
        List<Product> searchProduct(String text);
    }
}
