package com.edibusl.listeatapp.gitem;

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
import com.shawnlin.numberpicker.NumberPicker;
import android.widget.RadioButton;
import android.widget.Toast;

import com.edibusl.listeatapp.R;
import com.edibusl.listeatapp.model.datatypes.GItem;
import com.edibusl.listeatapp.model.datatypes.Product;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display a grid of {@link GItem}s. User can choose to view all, active or completed tasks.
 */
public class GItemFragment extends Fragment implements GItemContract.View {
    public static final String LOG_TAG = "GItemFragment";

    private GItemContract.Presenter mPresenter;
    private AutoCompleteAdapter mAutoCompleteAdapter;
    private Product mSelectedProduct;
    private GItem mEditedGItem;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.gitem_fragment, container, false);

        //Set auto complete text view
        AutoCompleteTextView autoCompleteTextView = root.findViewById(R.id.autoCompleteProduct);
        if(mAutoCompleteAdapter == null){
            mAutoCompleteAdapter = new AutoCompleteAdapter(this.getContext(), android.R.layout.simple_dropdown_item_1line, this.mPresenter);
        }
        autoCompleteTextView.setAdapter(mAutoCompleteAdapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long index) {
                mSelectedProduct = mAutoCompleteAdapter.getProduct((int)index);
            }
        });

        //Set listener for radio buttons click
        RadioButton radioQuantity = root.findViewById(R.id.radioQuantity);
        radioQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRadioButtonClicked(view);
            }
        });
        RadioButton radioWeight = root.findViewById(R.id.radioWeight);
        radioWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRadioButtonClicked(view);
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

    private boolean isQuantitySelected()
    {
        RadioButton radioButton = getView().findViewById(R.id.radioQuantity);
        return radioButton.isChecked();
    }

    private void onSaveButtonClicked(){
        //Validate product selected
        if(mSelectedProduct == null) {
            Toast.makeText(this.getContext(), R.string.gitem_missing_product, Toast.LENGTH_SHORT).show();

            return;
        }

        //Create a GItem object (or take an existing one in case of edie mode) and start filling it
        GItem gItem = (mEditedGItem != null ? mEditedGItem : new GItem());

        //Product Id
        gItem.setProductId(mSelectedProduct.getProduct_id());

        //Comments
        String comments = ((EditText)(getView().findViewById(R.id.editComments))).getText().toString();
        gItem.setComments(comments);

        //Weight / Quantity
        if (isQuantitySelected()) {
            NumberPicker picker = getView().findViewById(R.id.editQuantity);
            int quantity = picker.getValue();
            gItem.setQuantity(quantity);
            gItem.setWeight(null);
        } else {
            EditText etWeight = getView().findViewById(R.id.editWeight);
            String sWeight = etWeight.getText().toString();
            if(Strings.isNullOrEmpty(sWeight)){
                Toast.makeText(this.getContext(), R.string.gitem_missing_weight, Toast.LENGTH_SHORT).show();

                return;
            }

            //Parse and set weight
            int weight = Integer.parseInt(sWeight);
            gItem.setWeight(weight);
            gItem.setQuantity(null);
        }

        if(mEditedGItem != null) {
            mPresenter.updateGItem(gItem);
        } else {
            mPresenter.createGItem(gItem);
        }
    }

    private void onRadioButtonClicked(View view)
    {
        switch(view.getId()) {
            case R.id.radioWeight:
                getView().findViewById(R.id.editWeight).setVisibility(View.VISIBLE);
                getView().findViewById(R.id.editQuantity).setVisibility(View.GONE);
                break;
            case R.id.radioQuantity:
                getView().findViewById(R.id.editWeight).setVisibility(View.GONE);
                getView().findViewById(R.id.editQuantity).setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(GItemContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void itemCreated() {
        Toast.makeText(this.getContext(), R.string.gitem_item_added_success, Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    @Override
    public void itemUpdated() {
        Toast.makeText(this.getContext(), R.string.gitem_item_updated_success, Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    @Override
    public void showGItem(GItem gItem){
        mEditedGItem = gItem;

        //Set auto complete
        mSelectedProduct = gItem.getProduct();
        mAutoCompleteAdapter.setSelectedProduct(mSelectedProduct);
        ((AutoCompleteTextView)getView().findViewById(R.id.autoCompleteProduct)).setText(mAutoCompleteAdapter.getItem(0));

        //Set weight OR quantity
        if (gItem.getWeight() != null) {
            getView().findViewById(R.id.editWeight).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.editQuantity).setVisibility(View.GONE);
            ((EditText) (getView().findViewById(R.id.editWeight))).setText(gItem.getWeight().toString());
            ((RadioButton) (getView().findViewById(R.id.radioWeight))).setChecked(true);
            ((RadioButton) (getView().findViewById(R.id.radioQuantity))).setChecked(false);
        } else {
            getView().findViewById(R.id.editWeight).setVisibility(View.GONE);
            getView().findViewById(R.id.editQuantity).setVisibility(View.VISIBLE);
            ((NumberPicker) (getView().findViewById(R.id.editQuantity))).setValue(gItem.getQuantity());
            ((RadioButton) (getView().findViewById(R.id.radioWeight))).setChecked(false);
            ((RadioButton) (getView().findViewById(R.id.radioQuantity))).setChecked(true);
        }

        //Comments
        if(gItem.getComments() != null) {
            ((EditText) (getView().findViewById(R.id.editComments))).setText(gItem.getComments());
        }
    }

    @Override
    public void setEditMode() {
        //Save --> Edit
        ((Button)(getView().findViewById(R.id.btnSave))).setText(R.string.gitem_save_edit);

        //Enable Delete button
        getView().findViewById(R.id.btnDelete).setVisibility(View.VISIBLE);
    }
}

class AutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    ArrayList<String> mlstItems;
    ArrayList<Product> mlstProducts;
    GItemContract.Presenter mPresenter;

    public AutoCompleteAdapter(Context context, int textViewResourceId, GItemContract.Presenter presenter) {
        super(context, textViewResourceId);
        mlstItems = new ArrayList<>();
        mlstProducts = new ArrayList<>();
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

    public Product getProduct(int index) {
        if (mlstProducts == null || index > mlstProducts.size() - 1) {
            return null;
        }

        return mlstProducts.get(index);
    }

    public void setSelectedProduct(Product product) {
        mlstProducts.clear();
        mlstProducts.add(product);
        mlstItems.clear();
        mlstItems.add(productToString(product));

        notifyDataSetChanged();
    }

    private String productToString(Product product) {
        return String.format("%s (%s)", product.getName(), product.getCategory().getName());
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter(){
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    mlstItems.clear();
                    mlstProducts = null;

                    try {
                        //Prepare multiple params to be passed to async task
                        GetProductsForAutoCompleteData params = new GetProductsForAutoCompleteData();
                        params.text = constraint.toString();
                        params.presenter = mPresenter;

                        //Execute an AsyncTask to make the request to server.
                        //The execution is blocking and frees up the UI thread for other operations
                        mlstProducts = new GetProductsForAutoComplete().execute(new GetProductsForAutoCompleteData[]{params}).get();

                        //Convert each product item to a string
                        for(Product product : mlstProducts){
                            mlstItems.add(productToString(product));
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

    private class GetProductsForAutoComplete extends AsyncTask<GetProductsForAutoCompleteData, Void, ArrayList<Product>> {
        @Override
        protected ArrayList<Product> doInBackground(GetProductsForAutoCompleteData... params) {
            ArrayList<Product> filteredProducts;

            try {
                //A blocking call to get the search results
                filteredProducts = (ArrayList<Product>)params[0].presenter.searchProduct(params[0].text);
            }
            catch (Exception ex) {
                filteredProducts = new ArrayList<>();
            }

            return filteredProducts;
        }

        @Override
        protected void onPostExecute(ArrayList<Product> result) {}
    }

    private class GetProductsForAutoCompleteData{
        public String text;
        public GItemContract.Presenter presenter;
    }
}