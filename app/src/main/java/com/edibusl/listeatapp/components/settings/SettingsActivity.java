package com.edibusl.listeatapp.components.settings;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.MenuItem;

import com.edibusl.listeatapp.R;
import com.edibusl.listeatapp.components.glist.GListFragment;
import com.edibusl.listeatapp.helpers.ActivityUtils;
import com.edibusl.listeatapp.model.repository.AppData;

public class SettingsActivity extends AppCompatActivity {
    private SettingsPresenter mSettingsPresenter;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the main content view of this activity
        setContentView(R.layout.settings_activity);

        //Create fragment and add it to activity
        SettingsFragment fragment = new SettingsFragment();
        Pair<ActionBar, DrawerLayout> pair = ActivityUtils.createInnerFragment(this, fragment);
        ActionBar ab = pair.first;
        mDrawerLayout = pair.second;

        //Create the Presenter
        mSettingsPresenter = new SettingsPresenter(AppData.getInstance(), fragment);

        //Set up the toolbar
        ab.setTitle(R.string.settings_title_add);
    }
}
