package com.edibusl.listeatapp.model.repository;


import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.edibusl.listeatapp.helpers.VolleyQueue;
import com.edibusl.listeatapp.model.datatypes.GList;
import com.edibusl.listeatapp.model.datatypes.Product;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ProductRepo {
    //TODO - Move to configs
    private final String BASE_URL = "http://10.100.102.7:9090";

    /**
     * A blocking call to get auto complete results of a products list
     * @param text Text search pattern
     * @return A list of products matching the text
     * @throws Exception
     */
    public List<Product> getProductsByAutoComplete(String text) throws Exception{
        //Instantiate the RequestQueue.
        String url = String.format("%s/product/autocomplete/%s", BASE_URL, text);

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
}