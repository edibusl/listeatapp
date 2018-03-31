package com.edibusl.listeatapp.components.stats;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;

import com.edibusl.listeatapp.R;
import com.edibusl.listeatapp.components.stats.StatsFragment;
import com.edibusl.listeatapp.components.stats.StatsPresenter;
import com.edibusl.listeatapp.helpers.ActivityUtils;

public class StatsActivity extends AppCompatActivity {
    private StatsPresenter mStatsPresenter;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the main content view of this activity
        setContentView(R.layout.stats_activity);

        //Create fragment and add it to activity
        StatsFragment fragment = new StatsFragment();
        Pair<ActionBar, DrawerLayout> pair = ActivityUtils.createInnerFragment(this, fragment);
        ActionBar ab = pair.first;
        mDrawerLayout = pair.second;

        //Create the Presenter
        mStatsPresenter = new StatsPresenter(fragment);

        //Set up the toolbar
        ab.setTitle(R.string.stats_title);
    }
}
