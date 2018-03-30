package com.edibusl.listeatapp.components.start;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.edibusl.listeatapp.R;
import com.edibusl.listeatapp.components.glist.GListActivity;
import com.edibusl.listeatapp.components.settings.SettingsActivity;

import static com.google.common.base.Preconditions.checkNotNull;

public class StartFragment extends Fragment implements StartContract.View {
    public static final String LOG_TAG = "StartFragment";

    private StartContract.Presenter mPresenter;
    private EditText mEditServerUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.start_fragment, container, false);

        //Set logo image
        ImageView image  = root.findViewById(R.id.imgLogo);
        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_logo));

        //Set listener for Login button click
        Button btnLogin = root.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLoginButtonClicked();
            }
        });

        //Set listener for Settings button click
        Button btnSettings = root.findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSettingsButtonClicked();
            }
        });

        return root;
    }


    private void onLoginButtonClicked(){
        mPresenter.authenticate();
    }

    private void onSettingsButtonClicked(){
        Intent intent = new Intent(getContext(), SettingsActivity.class);
        getActivity().startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(StartContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }


    @Override
    public void showLoginError() {
        Toast.makeText(this.getContext(), R.string.login_failed_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void moveToGListActivity() {
        Intent intent = new Intent(getContext(), GListActivity.class);
        getActivity().startActivity(intent);
    }
}

