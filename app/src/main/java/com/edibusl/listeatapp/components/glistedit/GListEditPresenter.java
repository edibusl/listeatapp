package com.edibusl.listeatapp.components.glistedit;

import android.support.annotation.NonNull;
import android.util.Log;

import com.edibusl.listeatapp.components.glistedit.GListEditContract;
import com.edibusl.listeatapp.components.glistedit.GListEditFragment;
import com.edibusl.listeatapp.model.datatypes.GList;
import com.edibusl.listeatapp.model.datatypes.GList;
import com.edibusl.listeatapp.model.datatypes.Product;
import com.edibusl.listeatapp.model.repository.AppData;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link GListEditFragment}), retrieves the data and updates the
 * UI as required.
 */
public class GListEditPresenter implements GListEditContract.Presenter {
    public static final String LOG_TAG = "GListEditPresenter";

    private final AppData mAppData;
    private final GListEditContract.View mGListEditView;

    private GList mGList;

    public GListEditPresenter(@NonNull AppData appData, @NonNull GListEditContract.View glistView, GList gList) {
        mAppData = checkNotNull(appData, "appData cannot be null");
        mGListEditView = checkNotNull(glistView, "glistView cannot be null!");
        mGListEditView.setPresenter(this);
        mGList = gList;
    }

    @Override
    public void start() {
        if(mGList != null) {
            mGListEditView.setEditMode();
            mGListEditView.showGList(mGList);
        }
    }

    @Override
    public void createGList(@NonNull GList gList) {
        checkNotNull(gList, "gList cannot be null!");

        //Send the request to server
        mAppData.GListRepo().updateGList(gList, new AppData.LoadDataCallback() {
            @Override
            public void onSuccess(Object data) {
                mGListEditView.glistCreated();
            }

            @Override
            public void onError(String error) {
                Log.e(LOG_TAG, "Error when saving glist");
            }
        });
    }

    @Override
    public void updateGList(@NonNull GList gList) {
        checkNotNull(gList, "gList cannot be null!");

        //Send the request to server
        mAppData.GListRepo().updateGList(gList, new AppData.LoadDataCallback() {
            @Override
            public void onSuccess(Object data) {
                mGListEditView.glistUpdated();
            }

            @Override
            public void onError(String error) {
                Log.e(LOG_TAG, "Error when updating glist");
            }
        });
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
