package com.edibusl.listeatapp.components.stats;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.edibusl.listeatapp.R;
import com.edibusl.listeatapp.helpers.GeneralUtils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class StatsFragment extends Fragment implements StatsContract.View {
    public static final String LOG_TAG = "StatsFragment";

    private StatsContract.Presenter mPresenter;
    private EditText mEditServerUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.stats_fragment, container, false);
        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(StatsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void loadStats(JSONArray arrResults){
        //Handle no data scenario
        if(arrResults == null) {
            Toast.makeText(this.getContext(), R.string.stats_no_data, Toast.LENGTH_SHORT).show();

            return;
        }

        try {
            //Parse response data
            PieChart pieChart = getView().findViewById(R.id.pieChart);
            List<PieEntry> entries = new ArrayList<>();
            for (int i = 0; i < arrResults.length(); i++) {
                JSONObject record = arrResults.getJSONObject(i);
                String label = record.getString("categoryName");
                int value = record.getInt("count");
                entries.add(new PieEntry((float)value, label));
            }

            //Load data into pie chart
            PieDataSet set = new PieDataSet(entries, getString(R.string.stats_group_by_text));
            set.setColors(ColorTemplate.PASTEL_COLORS);
            set.setValueTextSize(20);
            PieData data = new PieData(set);
            pieChart.setData(data);
            pieChart.invalidate();
        } catch (Exception ex) {
            GeneralUtils.printErrorToLog(LOG_TAG, ex);
        }
    }
}

