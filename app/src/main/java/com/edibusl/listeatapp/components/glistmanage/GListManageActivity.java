package com.edibusl.listeatapp.components.glistmanage;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.edibusl.listeatapp.R;
import com.edibusl.listeatapp.helpers.ActivityUtils;
import com.edibusl.listeatapp.model.repository.AppData;

public class GListManageActivity extends AppCompatActivity {
    private GListManagePresenter mGListManagePresenter;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO - Move this to the initializer activity
        //Init AWS singleton
        AWSMobileClient.getInstance().initialize(this).execute();


        //Set the main content view of this activity
        setContentView(R.layout.glist_activity);

        //Create the View - Set the inner fragment and attach it to this activity
        GListManageFragment innerGListManageFragment = (GListManageFragment)getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (innerGListManageFragment == null) {
            // Create the fragment
            innerGListManageFragment = new GListManageFragment();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), innerGListManageFragment, R.id.contentFrame);
        }

        //Create the Presenter
        this.mGListManagePresenter = new GListManagePresenter(AppData.getInstance(), innerGListManageFragment);

        //Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(getResources().getString(R.string.glist_manage_title));

        //Set up the navigation drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
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

    //TODO - Move this function to a common utils func
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.glist_navigation_menu_item:
                                // Do nothing, we're already on that screen
                                break;
                            case R.id.statistics_navigation_menu_item:
                                //TODO
//                                Intent intent = new Intent(TasksActivity.this, StatisticsActivity.class);
//                                startActivity(intent);
                                break;
                            default:
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }
}
