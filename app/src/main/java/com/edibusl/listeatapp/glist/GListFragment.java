package com.edibusl.listeatapp.glist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
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

    @Override
    public void setPresenter(GListContract.Presenter presenter) {

    }

    @Override
    public void showGItems(List<GItem> gItems) {

    }

    @Override
    public void showGListInfo(GList gList) {

    }
}
