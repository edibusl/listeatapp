package com.edibusl.listeatapp.components.stats;

import com.edibusl.listeatapp.mvp.BasePresenter;
import com.edibusl.listeatapp.mvp.BaseView;

import org.json.JSONArray;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface StatsContract {

    interface View extends BaseView<Presenter> {
        void loadStats(JSONArray arrResults);
    }

    interface Presenter extends BasePresenter {
        void start();
    }
}
