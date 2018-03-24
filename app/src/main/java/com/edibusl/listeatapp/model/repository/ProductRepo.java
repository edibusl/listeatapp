package com.edibusl.listeatapp.model.repository;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.edibusl.listeatapp.helpers.AwsUtils;
import com.edibusl.listeatapp.helpers.VolleyQueue;
import com.edibusl.listeatapp.model.datatypes.Category;
import com.edibusl.listeatapp.model.datatypes.GItem;
import com.edibusl.listeatapp.model.datatypes.GList;
import com.edibusl.listeatapp.model.datatypes.Product;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ProductRepo {
    //TODO - Move to configs
    private final String BASE_URL = "http://10.100.102.7:9090";
    private static final String PRODUCT_THUMBNAIL_BASE_URL = "https://s3.eu-central-1.amazonaws.com/listeatapp-userfiles-mobilehub-1030236591/public";

    /**
     * A blocking call to get auto complete results of a products list
     * @param text Text search pattern
     * @return A list of products matching the text
     * @throws Exception
     */
    public List<Product> getProductsByAutoComplete(long gListId, String text) throws Exception{
        //Instantiate the RequestQueue.
        String url = String.format("%s/product/autocomplete/%s/%s", BASE_URL, gListId, text);

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, future, future);

        // Add the request to the RequestQueue.
        VolleyQueue.getInstance().addToRequestQueue(request);

        try {
            JSONObject response = future.get();
            List<Product> lstProducts = Product.parseList(response);

            //return new ArrayList<Product>();
            return lstProducts;
        } catch (Exception e) {
            throw e;
        }
    }

    public void getAllCategories(@NonNull final AppData.LoadDataCallback callback) {
        //Instantiate the RequestQueue.
        String url = String.format("%s/category/all", BASE_URL);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(Category.parseList(response));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.getMessage());
                    }
                }
        );

        // Add the request to the RequestQueue.
        VolleyQueue.getInstance().addToRequestQueue(request);
    }

    public void createProduct(Product product, @NonNull final AppData.LoadDataCallback callback) {
        //Instantiate the RequestQueue.
        String url = String.format("%s/product", BASE_URL);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, product.toJson(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(new Product(response));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.getMessage());
                    }
                }
        );
        request.setRetryPolicy(VolleyQueue.getInstance().getRetryPolicy());

        // Add the request to the RequestQueue.
        VolleyQueue.getInstance().addToRequestQueue(request);
    }

    public void uploadProductThumbnail(Activity activityContext, final String fileFullPath, final String filename) {
        AwsUtils.getInstance().uploadData(activityContext, fileFullPath, filename);
    }

    public String getProductThumbnailUrl(String imageName) {
        return String.format("%s/%s", PRODUCT_THUMBNAIL_BASE_URL, imageName);
    }
}