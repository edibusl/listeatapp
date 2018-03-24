package com.edibusl.listeatapp.product;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filterable;

import com.edibusl.listeatapp.helpers.GeneralUtils;
import com.edibusl.listeatapp.model.datatypes.Category;

import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.edibusl.listeatapp.R;
import com.edibusl.listeatapp.model.datatypes.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Display a grid of {@link Product}s. User can choose to view all, active or completed tasks.
 */
public class ProductFragment extends Fragment implements ProductContract.View {
    public static final String LOG_TAG = "ProductFragment";
    private static final int REQUEST_CAMERA = 1;

    private ProductContract.Presenter mPresenter;
    private AutoCompleteAdapterCategory mAutoCompleteAdapter;
    private Category mSelectedCategory;
    private ImageView mImageThumbnail;
    private String mThumbnailImageFileName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.product_fragment, container, false);

        //Image reference
        mImageThumbnail = root.findViewById(R.id.imgThumbnail);

        //Set auto complete text view
        Spinner spCategory = root.findViewById(R.id.spCategory);
        if(mAutoCompleteAdapter == null){
            mAutoCompleteAdapter = new AutoCompleteAdapterCategory(this.getContext(), android.R.layout.simple_dropdown_item_1line, this.mPresenter);
        }
        spCategory.setAdapter(mAutoCompleteAdapter);
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long index) {
                mSelectedCategory = mAutoCompleteAdapter.getCategory((int)index);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mSelectedCategory = null;
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

        //Set listener for Choose Image button click
        Button btnChooseImage = root.findViewById(R.id.btnChooseImage);
        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChooseImageButtonClicked();
            }
        });

        return root;
    }

    private void onSaveButtonClicked(){
        //Validate product selected
        if(mSelectedCategory == null) {
            Toast.makeText(this.getContext(), R.string.gitem_missing_product, Toast.LENGTH_SHORT).show();

            return;
        }

        //Create a Product object and start filling it
        Product product = new Product();

        //Category
        product.setCategoryId(mSelectedCategory.getCategory_id());

        //Name
        String name = ((EditText)(getView().findViewById(R.id.editName))).getText().toString();
        product.setName(name);

        //Description
        String description = ((EditText)(getView().findViewById(R.id.editDescription))).getText().toString();
        product.setDescription(description);

        //Image
        if(mThumbnailImageFileName != null) {
            product.setImage_path(mThumbnailImageFileName);
        }

        mPresenter.createProduct(product);
    }

    private void onChooseImageButtonClicked() {
        //Take a picture
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        getActivity().startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void handleChosenImage(int requestCode, int resultCode, Intent imageReturnedIntent) {
        if(resultCode != RESULT_OK){
            return;
        }

        String filename = String.format("%s.png", UUID.randomUUID().toString());
        String destFilename = null;

        if(requestCode == REQUEST_CAMERA) {
            //Get generated thumbnail
            Bundle extras = imageReturnedIntent.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            destFilename = String.format("%s/%s", getContext().getFilesDir(), filename);

            //Save the thumbnail in internal storage (local app files)
            GeneralUtils.saveBitmapToFile(imageBitmap, destFilename);

            //Show the picture in the image view
            mImageThumbnail.setImageBitmap(imageBitmap);
        }

        //Upload the file to server
        if(destFilename != null) {
            mPresenter.uploadThumbnailImage(getActivity(), destFilename, filename);
        }

        //Set image filename for later use when saving product
        mThumbnailImageFileName = filename;

        //TODO - Switch the "upload image" button to "remove image"
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(ProductContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public void setCatgoriesList(List<Category> lstCategories) {
        mAutoCompleteAdapter.setData(lstCategories);
    }

    @Override
    public void itemCreated(Product newlyCreatedProduct) {
        Toast.makeText(this.getContext(), R.string.product_added_success, Toast.LENGTH_SHORT).show();

        //Set the selected category to be passed back to gitem fragment
        Intent intent = new Intent();
        intent.putExtra("Product", newlyCreatedProduct);
        getActivity().setResult(RESULT_OK, intent);

        //Close this activity
        getActivity().finish();
    }
}

class AutoCompleteAdapterCategory extends ArrayAdapter<String> implements Filterable {
    ArrayList<String> mlstItems;
    ArrayList<Category> mlstCategories;
    ProductContract.Presenter mPresenter;

    public AutoCompleteAdapterCategory(Context context, int textViewResourceId, ProductContract.Presenter presenter) {
        super(context, textViewResourceId);
        mlstItems = new ArrayList<>();
        mlstCategories = new ArrayList<>();
        mPresenter = presenter;
    }

    public void setData(List<Category> lstCategories) {
        mlstCategories = (ArrayList<Category>)lstCategories;
        mlstItems.clear();
        for(Category category : lstCategories) {
            mlstItems.add(category.getName());
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mlstItems.size();
    }

    @Override
    public String getItem(int index) {
        return mlstItems.get(index);
    }

    public Category getCategory(int index) {
        if (mlstCategories == null || index > mlstCategories.size() - 1) {
            return null;
        }

        return mlstCategories.get(index);
    }
}