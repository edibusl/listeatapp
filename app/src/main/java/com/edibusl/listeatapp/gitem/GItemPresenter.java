package com.edibusl.listeatapp.gitem;

import android.support.annotation.NonNull;
import android.util.Log;

import com.edibusl.listeatapp.model.datatypes.GItem;
import com.edibusl.listeatapp.model.datatypes.Product;
import com.edibusl.listeatapp.model.repository.AppData;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link GItemFragment}), retrieves the data and updates the
 * UI as required.
 */
public class GItemPresenter implements GItemContract.Presenter {
    public static final String LOG_TAG = "GItemPresenter";

    private final AppData mAppData;
    private final GItemContract.View mGItemView;

    private GItem mGItem;

    public GItemPresenter(@NonNull AppData appData, @NonNull GItemContract.View glistView, GItem gItem) {
        mAppData = checkNotNull(appData, "appData cannot be null");
        mGItemView = checkNotNull(glistView, "glistView cannot be null!");
        mGItemView.setPresenter(this);
        mGItem = gItem;
    }

    @Override
    public void start() {
        if(mGItem != null) {
            mGItemView.setEditMode();
            mGItemView.showGItem(mGItem);
        }
    }

    @Override
    public void createGItem(@NonNull GItem gItem) {
        checkNotNull(gItem, "gItem cannot be null!");

        //Set FK fields
        setGItemFKFields(gItem);

        //Send the request to server
        mAppData.GListRepo().updateGItem(gItem, new AppData.LoadDataCallback() {
            @Override
            public void onSuccess(Object data) {
                mGItemView.itemCreated();
            }

            @Override
            public void onError(String error) {
                Log.e(LOG_TAG, "Error when saving gitem");
            }
        });
    }

    @Override
    public void updateGItem(@NonNull GItem gItem) {
        checkNotNull(gItem, "gItem cannot be null!");

        //Set FK fields
        setGItemFKFields(gItem);

        //Send the request to server
        mAppData.GListRepo().updateGItem(gItem, new AppData.LoadDataCallback() {
            @Override
            public void onSuccess(Object data) {
                mGItemView.itemUpdated();
            }

            @Override
            public void onError(String error) {
                Log.e(LOG_TAG, "Error when updating gitem");
            }
        });
    }

    @Override
    public void deleteGItem(long gItemId) {
        //Send the request to server
        mAppData.GListRepo().deleteGItem(gItemId, new AppData.LoadDataCallback() {
            @Override
            public void onSuccess(Object data) {
                mGItemView.itemDeleted();
            }

            @Override
            public void onError(String error) {
                Log.e(LOG_TAG, "Error when deleting gitem");
            }
        });
    }

    @Override
    public void setProduct(Product product) {
        if(product == null) {
            return;
        }

        mGItemView.setProduct(product);
    }

    private void setGItemFKFields(GItem gItem) {
        gItem.setGlistId(mAppData.GListRepo().getCurrentGListId());
        gItem.setUserId(mAppData.UserRepo().getCurrentUserId());
    }

    @Override
    public List<Product> searchProduct(String text) {
        List<Product> products;
        try {
            products = mAppData.ProductRepo().getProductsByAutoComplete(mAppData.GListRepo().getCurrentGListId(), text);
        }
        catch(Exception ex){
            Log.e(LOG_TAG, "Error when searching for a product by auto complete: " + ex.getMessage());
            return new ArrayList<Product>();
        }

        return products;
    }
}
