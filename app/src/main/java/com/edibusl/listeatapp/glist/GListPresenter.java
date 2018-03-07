package com.edibusl.listeatapp.glist;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.edibusl.listeatapp.model.datatypes.GItem;
import com.edibusl.listeatapp.model.datatypes.GList;
import com.edibusl.listeatapp.model.repository.AppData;
        
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Listens to user actions from the UI ({@link GListFragment}), retrieves the data and updates the
 * UI as required.
 */
public class GListPresenter implements GListContract.Presenter {

    private final AppData mAppData;

    private final GListContract.View mGListView;

    public GListPresenter(@NonNull AppData appData, @NonNull GListContract.View glistView) {
        mAppData = checkNotNull(appData, "appData cannot be null");
        mGListView = checkNotNull(glistView, "glistView cannot be null!");

        mGListView.setPresenter(this);
    }

    @Override
    public void start() {
        loadData();
    }

    @Override
    public void loadData() {
        List<GItem> gItems = new ArrayList<GItem>();
        gItems.add(new GItem().setGitemId(1).setQuantity(1).setCreatedTime(new Date()).setComments("some comments"));
        gItems.add(new GItem().setGitemId(2).setQuantity(2).setCreatedTime(new Date()).setComments("some comments 2"));

        //TODO
        //mGListView.setLoadingIndicator(true);

//        mAppData.getTasks(new TasksDataSource.LoadTasksCallback() {
//            @Override
//            public void onTasksLoaded(List<Task> tasks) {
//                List<Task> tasksToShow = new ArrayList<Task>();
//
//                processTasks(tasksToShow);
//            }
//
//            @Override
//            public void onDataNotAvailable() {
//                //TODO
//                //mGListView.showLoadingTasksError();
//            }
//        });
    }

    @Override
    public void createGItem(@NonNull GItem gItem) {
        checkNotNull(gItem, "gItem cannot be null!");
        
        //TODO - 
        //mAppData.GListRepo().createGItem(gItem)
        
        loadData();
    }

    public void deleteGItem(@NonNull long gItemId) {
        //TODO
    }
}
