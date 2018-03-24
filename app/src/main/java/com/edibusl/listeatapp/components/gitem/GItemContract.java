package com.edibusl.listeatapp.components.gitem;

import android.support.annotation.NonNull;

import com.edibusl.listeatapp.model.datatypes.GItem;
import com.edibusl.listeatapp.model.datatypes.Product;
import com.edibusl.listeatapp.mvp.BasePresenter;
import com.edibusl.listeatapp.mvp.BaseView;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface GItemContract {

    interface View extends BaseView<Presenter> {
        void setEditMode();
        void showGItem(GItem gItem);
        void setProduct(Product product);
        void itemCreated();
        void itemUpdated();
        void itemDeleted();
    }

    interface Presenter extends BasePresenter {
        void createGItem(@NonNull GItem gItem);
        void updateGItem(@NonNull GItem gItem);
        void deleteGItem(long gItemId);
        void setProduct(Product product);
        List<Product> searchProduct(String text);
    }
}
