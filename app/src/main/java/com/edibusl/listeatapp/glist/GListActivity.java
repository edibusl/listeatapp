package com.edibusl.listeatapp.glist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.edibusl.listeatapp.R;
import com.edibusl.listeatapp.helpers.ActivityUtils;
import com.edibusl.listeatapp.model.repository.AppData;

public class GListActivity extends AppCompatActivity {
    private GListPresenter mGListPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the main content view of this activity
        setContentView(R.layout.glist_activity);

        //Create the View - Set the inner fragment and attach it to this activity
        GListFragment innerGListFragment = (GListFragment)getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (innerGListFragment == null) {
            // Create the fragment
            innerGListFragment = new GListFragment();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), innerGListFragment, R.id.contentFrame);
        }

        //Create the Presenter
        mGListPresenter = new GListPresenter(AppData.getInstance(), innerGListFragment);
    }
}
