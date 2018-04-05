package com.edibusl.listeatapp.components.product;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.edibusl.listeatapp.model.datatypes.Category;
import com.edibusl.listeatapp.model.datatypes.Product;
import com.edibusl.listeatapp.mvp.BasePresenter;
import com.edibusl.listeatapp.mvp.BaseView;

import java.util.List;

public interface ProductContract {

    interface View extends BaseView<Presenter> {
        void itemCreated(Product newlyCreatedProduct);
        void setCatgoriesList(List<Category> lstCategories);
        void handleChosenImage(int requestCode, int resultCode, Intent imageReturnedIntent);

    }

    interface Presenter extends BasePresenter {
        void createProduct(@NonNull Product product);
        void onImageChosen(int requestCode, int resultCode, Intent imageReturnedIntent);
        void uploadThumbnailImage(Activity activityContext, final String fileFullPath, final String filename);
    }
}
