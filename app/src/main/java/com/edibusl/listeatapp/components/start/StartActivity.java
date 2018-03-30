package com.edibusl.listeatapp.components.start;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.edibusl.listeatapp.R;
import com.edibusl.listeatapp.helpers.ActivityUtils;

public class StartActivity extends AppCompatActivity {
    private StartPresenter mStartPresenter;
    private DrawerLayout mDrawerLayout;

    void StartActivity() {
        //Init AWS client with an activity context (it can't be initialized with Application Context)
        AWSMobileClient.getInstance().initialize(this).execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set the main content view of this activity
        setContentView(R.layout.start_activity);

        //Create the View - Set the inner fragment and attach it to this activity
        StartFragment fragment = (StartFragment)getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            // Create the fragment
            fragment = new StartFragment();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), fragment, R.id.contentFrame);
        }

        //Create the Presenter
        mStartPresenter = new StartPresenter(fragment, this);
    }
}
