package com.edibusl.listeatapp.components.product;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.edibusl.listeatapp.model.datatypes.Category;
import com.edibusl.listeatapp.model.datatypes.Product;
import com.edibusl.listeatapp.model.repository.AppData;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class ProductPresenter implements ProductContract.Presenter {
    public static final String LOG_TAG = "ProductContract";

    private final AppData mAppData;
    private final ProductContract.View mProductView;

    public ProductPresenter(@NonNull ProductContract.View productView) {
        mAppData = AppData.getInstance();
        mProductView = checkNotNull(productView, "view cannot be null!");
        mProductView.setPresenter(this);
    }

    @Override
    public void start() {
        //Load all categories
        mAppData.ProductRepo().getAllCategories(new AppData.LoadDataCallback() {
            @Override
            public void onSuccess(Object data) {
                mProductView.setCatgoriesList((List<Category>)data);
            }

            @Override
            public void onError(String error) {
                Log.e(LOG_TAG, "Error when loading categories list");
            }
        });


    }

    @Override
    public void createProduct(@NonNull Product product) {
        checkNotNull(product, "product cannot be null!");

        //Set FK fields
        setProductFKFields(product);

        //Send the request to server
        mAppData.ProductRepo().createProduct(product, new AppData.LoadDataCallback() {
            @Override
            public void onSuccess(Object data) {
                mProductView.itemCreated((Product)data);
            }

            @Override
            public void onError(String error) {
                Log.e(LOG_TAG, "Error when saving product");
            }
        });
    }

    @Override
    public void onImageChosen(int requestCode, int resultCode, Intent imageReturnedIntent) {
        mProductView.handleChosenImage(requestCode, resultCode, imageReturnedIntent);
    }

    public void uploadThumbnailImage(Activity activityContext, final String fileFullPath, final String filename) {
        mAppData.ProductRepo().uploadProductThumbnail(activityContext, fileFullPath, filename);

    }

    private void setProductFKFields(Product product) {
        product.setGlistId(mAppData.GListRepo().getCurrentGListId());
    }
}
