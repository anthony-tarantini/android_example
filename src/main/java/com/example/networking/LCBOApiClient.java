package com.example.networking;

import com.example.models.ProductResponse;

import retrofit.http.GET;
import retrofit.http.Query;

public interface LCBOApiClient {
    @GET("/products")
    ProductResponse listProducts(@Query("q") String query);
}
