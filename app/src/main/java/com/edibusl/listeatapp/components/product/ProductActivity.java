package com.edibusl.listeatapp.components.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.edibusl.listeatapp.R;
import com.edibusl.listeatapp.helpers.ActivityUtils;
import com.edibusl.listeatapp.model.repository.AppData;

public class ProductActivity extends AppCompatActivity {
    private ProductPresenter mProductPresenter;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the main content view of this activity
        setContentView(R.layout.product_activity);

        //Create the View - Set the inner fragment and attach it to this activity
        ProductFragment innerProductFragment = (ProductFragment)getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (innerProductFragment == null) {
            // Create the fragment
            innerProductFragment = new ProductFragment();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), innerProductFragment, R.id.contentFrame);
        }

        //Create the Presenter
        this.mProductPresenter = new ProductPresenter(AppData.getInstance(), innerProductFragment);

        //Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.product_header_title_add);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        mProductPresenter.onImageChosen(requestCode, resultCode, imageReturnedIntent);
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
