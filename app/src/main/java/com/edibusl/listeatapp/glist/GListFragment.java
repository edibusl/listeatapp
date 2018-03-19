package com.edibusl.listeatapp.glist;

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
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.edibusl.listeatapp.R;

import java.util.ArrayList;
import java.util.List;

import com.edibusl.listeatapp.model.datatypes.GItem;
import com.edibusl.listeatapp.model.datatypes.GList;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display a grid of {@link GItem}s. User can choose to view all, active or completed tasks.
 */
public class GListFragment extends Fragment implements GListContract.View {
    private GListContract.Presenter mPresenter;
    private GItemsAdapter mListAdapter;
    private LinearLayout mGItemsView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new GItemsAdapter(new ArrayList<GItem>(0), mItemListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.glist_fragment, container, false);

        //Setup glist view
        ListView listView = (ListView) root.findViewById(R.id.gitems_list);
        listView.setAdapter(mListAdapter);
        mGItemsView = (LinearLayout) root.findViewById(R.id.glistLL);

        //Setup floating "+" button - (Add a new gitem)
        FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.fab_add_gitem);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mPresenter.addNewTask();
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

        //TODO - What's that?
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(GListContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showGItems(List<GItem> gItems) {
        mListAdapter.replaceData(gItems);
        mGItemsView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showGListInfo(GList gList) {

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

    private static class GItemsAdapter extends BaseAdapter {
        private List<GItem> mItems;
        private GItemListener mItemListener;

        public GItemsAdapter(List<GItem> gItems, GItemListener itemListener) {
            setList(gItems);
            mItemListener = itemListener;
        }

        public void replaceData(List<GItem> gItems) {
            setList(gItems);
            notifyDataSetChanged();
        }

        private void setList(List<GItem> gItems) {
            mItems = checkNotNull(gItems);
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public GItem getItem(int i) {
            return mItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = view;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                rowView = inflater.inflate(R.layout.glist_gitem, viewGroup, false);
            }

            final GItem gItem = getItem(i);

            TextView titleTV = (TextView) rowView.findViewById(R.id.title);
            titleTV.setText(gItem.getProduct().getName());

            CheckBox completeCB = (CheckBox) rowView.findViewById(R.id.complete);

            // Active/completed task UI
            completeCB.setChecked(gItem.getIsChecked());
            if (gItem.getIsChecked()) {
                rowView.setBackgroundDrawable(viewGroup.getContext()
                        .getResources().getDrawable(R.drawable.glist_checked_touch_feedback));
            } else {
                rowView.setBackgroundDrawable(viewGroup.getContext()
                        .getResources().getDrawable(R.drawable.glist_touch_feedback));
            }

            completeCB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!gItem.getIsChecked()) {
                        mItemListener.onItemCheck(gItem);
                    } else {
                        mItemListener.onItemUncheck(gItem);
                    }
                }
            });

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemListener.onGItemClick(gItem);
                }
            });

            return rowView;
        }
    }

    /**
     * Listener for clicks on tasks in the ListView.
     */
    GItemListener mItemListener = new GItemListener() {
        @Override
        public void onGItemClick(GItem gItem) {
            //mPresenter.openTaskDetails(clickedTask);
        }

        @Override
        public void onItemCheck(GItem gItem) {
            //mPresenter.completeTask(completedTask);
        }

        @Override
        public void onItemUncheck(GItem gItem) {
            //mPresenter.activateTask(activatedTask);
        }
    };

    public interface GItemListener {
        void onGItemClick(GItem gItem);
        void onItemCheck(GItem gItem);
        void onItemUncheck(GItem gItem);
    }
}
