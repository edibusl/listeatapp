package com.edibusl.listeatapp.components.glistmanage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.edibusl.listeatapp.R;
import com.edibusl.listeatapp.components.gitem.GItemActivity;
import com.edibusl.listeatapp.components.glistedit.GListEditActivity;
import com.edibusl.listeatapp.model.datatypes.GList;
import com.google.common.base.Strings;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class GListManageFragment extends Fragment implements GListManageContract.View {
    private final int MENU_CONTEXT_DELETE_ID = 1;

    private GListManageContract.Presenter mPresenter;
    private GListsAdapter mListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new GListsAdapter(new ArrayList<GList>(0), mItemListener, mPresenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.glist_manage_fragment, container, false);

        //Setup glist view
        ListView listView = (ListView) root.findViewById(R.id.gitems_list);
        listView.setAdapter(mListAdapter);

        //Setup floating "+" button - (Add a new gitem)
        FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.fab_add_gitem);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), GListEditActivity.class);
                startActivity(intent);
            }
        });

        // Setup progress indicator
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );

        // Set the scrolling view in the SwipeRefreshLayout.
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadData();
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(GListManageContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    public void showGListItems(List<GList> gLists) {
        mListAdapter.replaceData(gLists);
    }

    public void showGListEditInNewActivity(GList gList) {
        //Just close this activity (the current glist_id was already replaced)
        getActivity().finish();
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl = (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });
    }

    @Override
    public void glistDeleted() {
        Toast.makeText(this.getContext(), R.string.glist_manage_item_deleted, Toast.LENGTH_SHORT).show();
        mPresenter.loadData();
    }

    private static class GListsAdapter extends BaseAdapter {
        private List<GList> mItems;
        private GListItemsListener mItemListener;
        private GListManageContract.Presenter mItemPresenter;

        public GListsAdapter(List<GList> gLists, GListItemsListener itemListener, GListManageContract.Presenter presenter) {
            setList(gLists);
            mItemListener = itemListener;
            mItemPresenter = presenter;
        }

        public void replaceData(List<GList> gItems) {
            setList(gItems);
            notifyDataSetChanged();
        }

        private void setList(List<GList> gLists) {
            mItems = new ArrayList<GList>();

            if(gLists == null){
                return;
            }

            for (GList gItem : gLists){
                mItems.add(gItem);
            }
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int i) {
            return mItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View rowView, ViewGroup viewGroup) {
            final GList gList = (GList)getItem(i);
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            rowView = inflater.inflate(R.layout.glist_manage_glist_item, viewGroup, false);

            //Product name
            ((TextView) rowView.findViewById(R.id.tvTitle)).setText(gList.getSubject());

            //Register to Edit button click
            ((ImageView)rowView.findViewById(R.id.btnEdit)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemListener.onItemEdit(gList);
                }
            });

            //Register to Delete button click
            ((ImageView)rowView.findViewById(R.id.btnDelete)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemListener.onItemDelete(gList);
                }
            });

            //Register to clicks
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemListener.onItemClick(gList);
                }
            });

            return rowView;
        }
    }

    /**
     * Listener for clicks on tasks in the ListView.
     */
    GListItemsListener mItemListener = new GListItemsListener() {
        @Override
        public void onItemClick(GList item) {
            mPresenter.glistClicked(item);
        }

        @Override
        public void onItemEdit(GList item) {
            //Start the GListEdit activity and pass this object to the activity
            Intent intent = new Intent(getContext(), GListEditActivity.class);
            intent.putExtra("GList", item);
            startActivity(intent);
        }

        @Override
        public void onItemDelete(final GList item) {
            //Ask the user if her's sure about it
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            mPresenter.deleteGListClicked(item);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked - don't do anything
                            break;
                    }
                }
            };

            //Show the Yes/No dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(R.string.glist_manage_item_delete_sure).setPositiveButton(getString(R.string.glist_manage_item_delete_sure_yes), dialogClickListener)
                    .setNegativeButton(getString(R.string.glist_manage_item_delete_sure_no), dialogClickListener).show();
        }
    };

    public interface GListItemsListener {
        void onItemClick(GList item);
        void onItemEdit(GList item);
        void onItemDelete(GList item);
    }
}
