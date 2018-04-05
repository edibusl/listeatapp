package com.edibusl.listeatapp.components.glist;

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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.edibusl.listeatapp.components.gitem.GItemActivity;
import com.edibusl.listeatapp.components.glistmanage.GListManageActivity;
import com.edibusl.listeatapp.model.datatypes.Category;
import com.edibusl.listeatapp.model.datatypes.GItem;
import com.google.common.base.Strings;
import com.squareup.picasso.Picasso;

import static com.google.common.base.Preconditions.checkNotNull;

public class GListFragment extends Fragment implements GListContract.View {
    private GListContract.Presenter mPresenter;
    private GItemsAdapter mListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new GItemsAdapter(new ArrayList<GItem>(0), mItemListener, mPresenter);
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

        //Setup progress indicator
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

        //Set listener for purchase button
        ((Button)(root.findViewById(R.id.btnPurchase))).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Ask the user if her's sure about it
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                mPresenter.onPurchaseClicked();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked - don't do anything
                                break;
                        }
                    }
                };

                //Show the Yes/No dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(R.string.glist_purchase_sure).setPositiveButton(getString(R.string.glist_purchase_sure_yes), dialogClickListener)
                        .setNegativeButton(getString(R.string.glist_purchase_sure_no), dialogClickListener).show();
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
    public void setPresenter(GListContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void showGItems(List<GItem> gItems) {
        ((TextView)getView().findViewById(R.id.tvTotalItems)).setText(Integer.toString(gItems.size()));
        mListAdapter.replaceData(gItems);
    }

    @Override
    public void openGListsManage() {
        //Start the GListManager activity to choose a glist
        Intent intent = new Intent(getContext(), GListManageActivity.class);
        startActivity(intent);
    }

    @Override
    public void showGItemInNewActivity(GItem gItem) {
        //Start the GItem activity and pass this object to the activity
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

    @Override
    public void showPurchaseMade() {
        Toast.makeText(this.getContext(), R.string.glist_purchase_made_message, Toast.LENGTH_SHORT).show();
    }

    private static class GItemsAdapter extends BaseAdapter {
        private List<Object> mItems;
        private GItemListener mItemListener;
        private GListContract.Presenter mItemPresenter;

        public GItemsAdapter(List<GItem> gItems, GItemListener itemListener, GListContract.Presenter presenter) {
            setList(gItems);
            mItemListener = itemListener;
            mItemPresenter = presenter;
        }

        public void replaceData(List<GItem> gItems) {
            setList(gItems);
            notifyDataSetChanged();
        }

        private void setList(List<GItem> gItems) {
            mItems = new ArrayList<>();

            if(gItems == null){
                return;
            }

            Set<Long> categoryIds = new HashSet<>();
            for (GItem gItem : gItems){
                Long categoryId = gItem.getProduct().getCategory().getCategory_id();
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
        public View getView(int i, View rowView, ViewGroup viewGroup) {
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
                //Product name
                ((TextView) rowView.findViewById(R.id.tvTitle)).setText(gItem.getProduct().getName());

                //Quantity / Weight
                if (gItem.getQuantity() != null) {
                    ((TextView) rowView.findViewById(R.id.tvQuantity)).setText(gItem.getQuantity().toString());
                    ((TextView) rowView.findViewById(R.id.tvLabel)).setText(R.string.glist_item_quantity_label);
                } else if (gItem.getWeight() != null) {
                    ((TextView) rowView.findViewById(R.id.tvQuantity)).setText(gItem.getWeight().toString());
                    ((TextView) rowView.findViewById(R.id.tvLabel)).setText(R.string.glist_item_weight_label);
                } else {
                    ((LinearLayout) rowView.findViewById(R.id.llQuantity)).setVisibility(View.INVISIBLE);
                }

                //Description
                String comments = gItem.getComments();
                if(!Strings.isNullOrEmpty(comments)) {
                    TextView tvComments = rowView.findViewById(R.id.tvDescription);
                    tvComments.setText(comments);
                    tvComments.setVisibility(View.VISIBLE);
                }

                //Thumbnail
                String imageUrl = gItem.getProduct().getImage_path();
                if(!Strings.isNullOrEmpty(imageUrl)) {
                    ImageView imgViewThumbnail = rowView.findViewById(R.id.imgThumbnail);
                    imgViewThumbnail.setVisibility(View.VISIBLE);

                    //Download image from url, cache it and save into image view (Using Picasso)
                    String url = mItemPresenter.getProductImageFullPath(imageUrl);
                    Picasso.get().load(url).into(imgViewThumbnail);
                }

                //Complete checkbox
                CheckBox completeCB = (CheckBox) rowView.findViewById(R.id.cbComplete);
                completeCB.setChecked(gItem.getIsChecked());

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

                //Register to clicks
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
            mPresenter.checkGItem(gItem, true);
        }

        @Override
        public void onItemUncheck(GItem gItem) {
            mPresenter.checkGItem(gItem, false);
        }
    };

    public interface GItemListener {
        void onGItemClick(GItem gItem);
        void onItemCheck(GItem gItem);
        void onItemUncheck(GItem gItem);
    }
}
