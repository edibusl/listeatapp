package com.edibusl.listeatapp.components.gitem;

import android.content.Intent;
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
import com.edibusl.listeatapp.components.glist.GListPresenter;
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

        //Create fragment and add it to activity
        GItemFragment fragment = new GItemFragment();
        Pair<ActionBar, DrawerLayout> pair = ActivityUtils.createInnerFragment(this, fragment);
        ActionBar ab = pair.first;
        mDrawerLayout = pair.second;

        //Handle Edit Mode
        //Get the Intent that started this activity and extract the GItem
        GItem gItem = null;
        if(getIntent().hasExtra("GItem")) {
            gItem = (GItem)getIntent().getSerializableExtra("GItem");
        }

        //Create the Presenter
        mGItemPresenter = new GItemPresenter(AppData.getInstance(), fragment, gItem);

        //Set toolbar title according to edit mode
        if (gItem == null) {
            ab.setTitle(R.string.gitem_header_title_add);
        } else {
            ab.setTitle(R.string.gitem_header_title_edit);
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
}
