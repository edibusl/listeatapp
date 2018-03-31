package com.edibusl.listeatapp.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.MenuItem;

import com.edibusl.listeatapp.R;
import com.edibusl.listeatapp.components.glistmanage.GListManageActivity;
import com.edibusl.listeatapp.components.settings.SettingsActivity;
import com.edibusl.listeatapp.components.start.StartActivity;
import com.edibusl.listeatapp.components.stats.StatsActivity;
import com.edibusl.listeatapp.model.repository.AppData;

import static com.google.common.base.Preconditions.checkNotNull;

public class ActivityUtils {
    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     *
     */
    public static void addFragmentToActivity (@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static Pair<ActionBar, DrawerLayout> createInnerFragment(AppCompatActivity activity, Fragment newFragment) {
        //Create the View - Set the inner fragment and attach it to this activity
        Fragment fragment = (Fragment)activity.getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            // Create the fragment
            fragment = newFragment;
            addFragmentToActivity(activity.getSupportFragmentManager(), fragment, R.id.contentFrame);
        }

        //Set up the toolbar
        Toolbar toolbar = (Toolbar)activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        ActionBar ab = activity.getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        //Set up the navigation drawer
        DrawerLayout mDrawerLayout = (DrawerLayout)activity.findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView)activity.findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView, activity, mDrawerLayout);
        }

        return new Pair<>(ab, mDrawerLayout);
    }

    public static void setupDrawerContent(NavigationView navigationView, final Context packageContext, final DrawerLayout drawerLayout) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        Intent intent;
                        switch (menuItem.getItemId()) {
                            case R.id.glist_navigation_menu_item:
                                intent = new Intent(packageContext, GListManageActivity.class);
                                packageContext.startActivity(intent);
                                break;
                            case R.id.settings_navigation_menu_item:
                                intent = new Intent(packageContext, SettingsActivity.class);
                                packageContext.startActivity(intent);
                                break;
                            case R.id.statistics_navigation_menu_item:
                                intent = new Intent(packageContext, StatsActivity.class);
                                packageContext.startActivity(intent);
                                break;
                            case R.id.logout_navigation_menu_item:
                                AppData.getInstance().UserRepo().logout();
                                intent = new Intent(packageContext, StartActivity.class);
                                packageContext.startActivity(intent);
                                break;
                            default:
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();

                        return true;
                    }
                });
    }
}
