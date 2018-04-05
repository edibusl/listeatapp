package com.edibusl.listeatapp.components.glistedit;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;

import com.google.common.base.Strings;
import android.widget.Toast;

import com.edibusl.listeatapp.R;
import com.edibusl.listeatapp.model.datatypes.GList;
import com.edibusl.listeatapp.model.datatypes.User;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;

public class GListEditFragment extends Fragment implements GListEditContract.View {
    public static final String LOG_TAG = "GListEditFragment";

    private GListEditContract.Presenter mPresenter;
    private AutoCompleteAdapter mAutoCompleteAdapter;
    private User mSelectedUser;
    private GList mEditedGList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.glist_edit_fragment, container, false);

        //Set auto complete text view
        AutoCompleteTextView autoCompleteTextView = root.findViewById(R.id.autoCompleteUser);
        if(mAutoCompleteAdapter == null){
            mAutoCompleteAdapter = new AutoCompleteAdapter(this.getContext(), android.R.layout.simple_dropdown_item_1line, this.mPresenter);
        }
        autoCompleteTextView.setAdapter(mAutoCompleteAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long index) {
                mSelectedUser = mAutoCompleteAdapter.getUser((int)index);
            }
        });


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
        //Create a GList object (or take an existing one in case of edie mode) and start filling it
        GList gList = (mEditedGList != null ? mEditedGList : new GList());

        //Subject - validation and setting
        EditText editSubject = getView().findViewById(R.id.editSubject);
        String sSubject = editSubject.getText().toString();
        if(Strings.isNullOrEmpty(sSubject)) {
            Toast.makeText(this.getContext(), R.string.glist_edit_no_subject, Toast.LENGTH_SHORT).show();

            return;
        }
        gList.setSubject(sSubject);

        //Description
        String description = ((EditText)(getView().findViewById(R.id.editDescription))).getText().toString();
        gList.setDescription(description);

        //Save / Add
        mPresenter.updateGList(gList, mSelectedUser);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(GListEditContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void glistCreated() {
        Toast.makeText(this.getContext(), R.string.glist_edit_added_success, Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    @Override
    public void glistUpdated() {
        Toast.makeText(this.getContext(), R.string.glist_edit_edited_success, Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    @Override
    public void showGList(GList gList){
        mEditedGList = gList;

        //Subject
        if(gList.getSubject() != null) {
            ((EditText) (getView().findViewById(R.id.editSubject))).setText(gList.getSubject());
        }

        //Comments
        if(gList.getDescription() != null) {
            ((EditText) (getView().findViewById(R.id.editDescription))).setText(gList.getDescription());
        }
    }

    @Override
    public void setEditMode() {
        //Save --> Edit
        ((Button)(getView().findViewById(R.id.btnSave))).setText(R.string.glist_edit_edit_btn_text);
    }
}

class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    ArrayList<String> mlstItems;
    ArrayList<User> mlstUsers;
    GListEditContract.Presenter mPresenter;

    public AutoCompleteAdapter(Context context, int textViewResourceId, GListEditContract.Presenter presenter) {
        super(context, textViewResourceId);
        mlstItems = new ArrayList<>();
        mlstUsers = new ArrayList<>();
        mPresenter = presenter;
    }

    @Override
    public int getCount() {
        return mlstItems.size();
    }

    @Override
    public String getItem(int index) {
        return mlstItems.get(index);
    }

    public User getUser(int index) {
        if (mlstUsers == null || index > mlstUsers.size() - 1) {
            return null;
        }

        return mlstUsers.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter(){
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    mlstItems.clear();
                    mlstUsers = null;

                    try {
                        //Prepare multiple params to be passed to async task
                        GetUsersForAutoCompleteData params = new GetUsersForAutoCompleteData();
                        params.text = constraint.toString();
                        params.presenter = mPresenter;

                        //Execute an AsyncTask to make the request to server.
                        //The execution is blocking and frees up the UI thread for other operations
                        mlstUsers = new GetUsersForAutoComplete().execute(new GetUsersForAutoCompleteData[]{params}).get();

                        //Convert each user item to a string
                        for(User user : mlstUsers){
                            mlstItems.add(user.getUserName());
                        }
                    } catch (Exception e) {
                        Log.e("Error when searching", e.getMessage());
                    }

                    filterResults.values = mlstItems;
                    filterResults.count = mlstItems.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };

        return myFilter;
    }

    private class GetUsersForAutoComplete extends AsyncTask<GetUsersForAutoCompleteData, Void, ArrayList<User>> {
        @Override
        protected ArrayList<User> doInBackground(GetUsersForAutoCompleteData... params) {
            ArrayList<User> filteredUsers;

            try {
                //A blocking call to get the search results
                filteredUsers = (ArrayList<User>)params[0].presenter.searchUser(params[0].text);
            }
            catch (Exception ex) {
                filteredUsers = new ArrayList<>();
            }

            return filteredUsers;
        }

        @Override
        protected void onPostExecute(ArrayList<User> result) {}
    }

    private class GetUsersForAutoCompleteData{
        public String text;
        public GListEditContract.Presenter presenter;
    }
}