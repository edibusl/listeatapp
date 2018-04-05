package com.edibusl.listeatapp.components.glist;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;

import com.edibusl.listeatapp.R;
import com.edibusl.listeatapp.helpers.ActivityUtils;

public class GListActivity extends AppCompatActivity {
    private GListPresenter mGListPresenter;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the main content view of this activity
        setContentView(R.layout.glist_activity);

        //Create fragment and add it to activity
        GListFragment fragment = new GListFragment();
        Pair<ActionBar, DrawerLayout> pair = ActivityUtils.setupActivityAndFragment(this, fragment);
        ActionBar ab = pair.first;
        mDrawerLayout = pair.second;

        //Create the Presenter
        mGListPresenter = new GListPresenter(fragment, ab);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
