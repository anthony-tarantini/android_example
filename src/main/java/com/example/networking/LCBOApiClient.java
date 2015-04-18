package com.example.networking;

import com.example.models.Product;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;

public interface LCBOApiClient {
    @GET("/products")
    List<Product> listProducts(@Query("q") String query);
}
