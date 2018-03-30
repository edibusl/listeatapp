package com.edibusl.listeatapp.components.glistedit;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.MenuItem;

import com.edibusl.listeatapp.R;
import com.edibusl.listeatapp.components.glist.GListFragment;
import com.edibusl.listeatapp.helpers.ActivityUtils;
import com.edibusl.listeatapp.model.datatypes.GList;
import com.edibusl.listeatapp.model.repository.AppData;

public class GListEditActivity extends AppCompatActivity {
    private GListEditPresenter mGListEditPresenter;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the main content view of this activity
        setContentView(R.layout.glist_edit_activity);

        //Create fragment and add it to activity
        GListEditFragment fragment = new GListEditFragment();
        Pair<ActionBar, DrawerLayout> pair = ActivityUtils.createInnerFragment(this, fragment);
        ActionBar ab = pair.first;
        mDrawerLayout = pair.second;

        //Handle Edit Mode
        //Get the Intent that started this activity and extract the GList
        GList gList = null;
        if(getIntent().hasExtra("GList")) {
            gList = (GList)getIntent().getSerializableExtra("GList");
        }

        //Create the Presenter
        mGListEditPresenter = new GListEditPresenter(fragment, gList);

        //Set up the toolbar
        if (gList == null) {
            ab.setTitle(R.string.glist_edit_title_add);
        } else {
            ab.setTitle(R.string.glist_edit_title_edit);
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
}
