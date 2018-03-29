package com.edibusl.listeatapp.components.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.edibusl.listeatapp.R;
import com.edibusl.listeatapp.components.settings.SettingsContract;
import com.edibusl.listeatapp.model.datatypes.GList;
import com.edibusl.listeatapp.model.datatypes.User;
import com.google.common.base.Strings;

import static com.google.common.base.Preconditions.checkNotNull;

public class SettingsFragment extends Fragment implements SettingsContract.View {
    public static final String LOG_TAG = "SettingsFragment";

    private SettingsContract.Presenter mPresenter;
    private EditText mEditServerUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.settings_fragment, container, false);

        mEditServerUrl = root.findViewById(R.id.editServerUrl);

        //Set listener for Save button click
        Button btnSave = root.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });


        return root;
    }


    private void onSaveButtonClicked(){
        //Save
        mPresenter.updateSettings(mEditServerUrl.getText().toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(SettingsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void loadSettings(String serverUrl){
        //Subject
        if(serverUrl != null) {
            mEditServerUrl.setText(serverUrl);
        }
    }

    @Override
    public void close() {
        Toast.makeText(this.getContext(), R.string.settings_saved_text, Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }
}

