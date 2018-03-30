package com.edibusl.listeatapp.components.glistedit;

import android.support.annotation.NonNull;
import android.util.Log;

import com.edibusl.listeatapp.components.glistedit.GListEditContract;
import com.edibusl.listeatapp.components.glistedit.GListEditFragment;
import com.edibusl.listeatapp.helpers.GeneralUtils;
import com.edibusl.listeatapp.model.datatypes.GList;
import com.edibusl.listeatapp.model.datatypes.GList;
import com.edibusl.listeatapp.model.datatypes.User;
import com.edibusl.listeatapp.model.repository.AppData;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

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

    public GListEditPresenter(@NonNull GListEditContract.View glistView, GList gList) {
        mAppData = AppData.getInstance();
        mGListEditView = checkNotNull(glistView, "view cannot be null!");
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
    public void updateGList(@NonNull final GList gList, final User user) {
        checkNotNull(gList, "gList cannot be null!");

        //Check if we create or update
        boolean isNewTmp = false;
        if(gList.getGlist_id() == null || gList.getGlist_id() == 0) {
            isNewTmp = true;
        }
        final boolean isNewGList = isNewTmp;

        //Send the request to server
        mAppData.GListRepo().updateGList(gList, new AppData.LoadDataCallback() {
            @Override
            public void onSuccess(Object data) {
                if(user == null) {
                    showSuccessMessage(isNewGList);
                } else {
                    //If we create a new glist, then take the glist_id from the response
                    //If we update an existing glist, take the glist_id from the original glist object
                    Long gListId = gList.getGlist_id();
                    if(gListId == null) {
                        GList newGlist = (GList)data;
                        gListId = newGlist.getGlist_id();
                    }

                    //We're not finished yet. Now need to add the user to the glist
                    addUserToGList(gListId, user.getUser_id(), isNewGList);
                }
            }

            @Override
            public void onError(String error) {
                if(isNewGList) {
                    Log.e(LOG_TAG, "Error when creating glist");
                } else {
                    Log.e(LOG_TAG, "Error when updating glist");
                }
            }
        });
    }

    private void addUserToGList(@NonNull Long glistId, @NonNull Long userId, final boolean isNewGList) {
        //Send the request to server
        mAppData.GListRepo().addUserToGList(userId, glistId, new AppData.LoadDataCallback() {
            @Override
            public void onSuccess(Object data) {
                showSuccessMessage(isNewGList);
            }

            @Override
            public void onError(String error) {
                Log.e(LOG_TAG, "Error when adding user to glist");
            }
        });
    }

    private void showSuccessMessage(boolean isNew) {
        if (isNew) {
            mGListEditView.glistCreated();
        } else {
            mGListEditView.glistUpdated();
        }
    }

    @Override
    public List<User> searchUser(String text) {
        List<User> users;
        try {
            users = mAppData.UserRepo().getUsersByAutoComplete(text);
        }
        catch(Exception ex){
            Log.e(LOG_TAG, "Error when searching for a user by auto complete");
            GeneralUtils.printErrorToLog(LOG_TAG, ex);
            return new ArrayList<User>();
        }

        return users;
    }
}
