package com.edibusl.listeatapp.components.gitem;

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
import com.edibusl.listeatapp.model.datatypes.GItem;
import com.edibusl.listeatapp.model.datatypes.Product;
import com.edibusl.listeatapp.model.repository.AppData;

public class GItemActivity extends AppCompatActivity {
    private GItemPresenter mGItemPresenter;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the main content view of this activity
        setContentView(R.layout.gitem_activity);

        //Create the View - Set the inner fragment and attach it to this activity
        GItemFragment innerGItemFragment = (GItemFragment)getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (innerGItemFragment == null) {
            // Create the fragment
            innerGItemFragment = new GItemFragment();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), innerGItemFragment, R.id.contentFrame);
        }

        //Handle Edit Mode
        //Get the Intent that started this activity and extract the GItem
        GItem gItem = null;
        if(getIntent().hasExtra("GItem")) {
            gItem = (GItem)getIntent().getSerializableExtra("GItem");
        }

        //Create the Presenter
        this.mGItemPresenter = new GItemPresenter(AppData.getInstance(), innerGItemFragment, gItem);

        //Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);
        if (gItem == null) {
            ab.setTitle(R.string.gitem_header_title_add);
        } else {
            ab.setTitle(R.string.gitem_header_title_edit);
        }

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data.hasExtra("Product")) {
            Product product = (Product) data.getSerializableExtra("Product");
            mGItemPresenter.setProduct(product);
        }
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
