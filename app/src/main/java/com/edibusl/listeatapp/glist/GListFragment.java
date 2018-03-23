package com.edibusl.listeatapp.glist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.edibusl.listeatapp.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.edibusl.listeatapp.gitem.GItemActivity;
import com.edibusl.listeatapp.model.datatypes.Category;
import com.edibusl.listeatapp.model.datatypes.GItem;
import com.edibusl.listeatapp.model.datatypes.GList;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display a grid of {@link GItem}s. User can choose to view all, active or completed tasks.
 */
public class GListFragment extends Fragment implements GListContract.View {
    private final int MENU_CONTEXT_DELETE_ID = 1;

    private GListContract.Presenter mPresenter;
    private GItemsAdapter mListAdapter;

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

        //Setup floating "+" button - (Add a new gitem)
        FloatingActionButton fab = (FloatingActionButton)getActivity().findViewById(R.id.fab_add_gitem);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), GItemActivity.class);
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
    }

    @Override
    public void showGListInfo(GList gList) {
        //TODO
    }

    @Override
    public void showGItemInNewActivity(GItem gItem) {
        //Start the GItem activity and pass this object to there
        Intent intent = new Intent(getContext(), GItemActivity.class);
        intent.putExtra("GItem", gItem);
        startActivity(intent);
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
        private List<Object> mItems;
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
            mItems = new ArrayList<Object>();

            if(gItems == null){
                return;
            }

            Set<Integer> categoryIds = new HashSet<>();
            for (GItem gItem : gItems){
                int categoryId = gItem.getProduct().getCategory().getCategory_id();
                if(!categoryIds.contains(categoryId)){
                    categoryIds.add(categoryId);
                    mItems.add(gItem.getProduct().getCategory());
                }

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
        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = view;

            final Object rowObj = getItem(i);
            GItem gItem = null;
            Category category = null;
            if(rowObj instanceof GItem){
                gItem = (GItem)rowObj;
            }else{
                category = (Category)rowObj;
            }

            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            if(gItem != null) {
                rowView = inflater.inflate(R.layout.glist_gitem, viewGroup, false);
            }else{
                rowView = inflater.inflate(R.layout.glist_gitem_category, viewGroup, false);
            }

            //Set all text of this gitem row
            if(gItem != null) {
                ((TextView) rowView.findViewById(R.id.tvTitle)).setText(gItem.getProduct().getName());
                if (gItem.getQuantity() != null) {
                    ((TextView) rowView.findViewById(R.id.tvQuantity)).setText(gItem.getQuantity().toString());
                    ((TextView) rowView.findViewById(R.id.tvLabel)).setText(R.string.glist_item_quantity_label);
                } else if (gItem.getWeight() != null) {
                    ((TextView) rowView.findViewById(R.id.tvQuantity)).setText(gItem.getWeight().toString());
                    ((TextView) rowView.findViewById(R.id.tvLabel)).setText(R.string.glist_item_weight_label);
                } else {
                    ((LinearLayout) rowView.findViewById(R.id.llQuantity)).setVisibility(View.INVISIBLE);
                }

                CheckBox completeCB = (CheckBox) rowView.findViewById(R.id.cbComplete);

                // Active/completed task UI
                completeCB.setChecked(gItem.getIsChecked());
                if (gItem.getIsChecked()) {
                    rowView.setBackgroundDrawable(viewGroup.getContext()
                            .getResources().getDrawable(R.drawable.glist_checked_touch_feedback));
                } else {
                    rowView.setBackgroundDrawable(viewGroup.getContext()
                            .getResources().getDrawable(R.drawable.glist_touch_feedback));
                }

                final GItem gItemFinal = gItem;
                completeCB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!gItemFinal.getIsChecked()) {
                            mItemListener.onItemCheck(gItemFinal);
                        } else {
                            mItemListener.onItemUncheck(gItemFinal);
                        }
                    }
                });

                rowView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mItemListener.onGItemClick(gItemFinal);
                    }
                });
            } else {
                ((TextView) rowView.findViewById(R.id.tvCategoryTitle)).setText(category.getName());
            }

            return rowView;
        }
    }

    /**
     * Listener for clicks on tasks in the ListView.
     */
    GItemListener mItemListener = new GItemListener() {
        @Override
        public void onGItemClick(GItem gItem) {
            mPresenter.gItemClicked(gItem);
        }

        @Override
        public void onItemCheck(GItem gItem) {
            //TODO
            //mPresenter.completeTask(completedTask);
        }

        @Override
        public void onItemUncheck(GItem gItem) {
            //TODO
            //mPresenter.activateTask(activatedTask);
        }
    };

    public interface GItemListener {
        void onGItemClick(GItem gItem);
        void onItemCheck(GItem gItem);
        void onItemUncheck(GItem gItem);
    }
}
