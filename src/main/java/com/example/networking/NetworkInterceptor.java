package com.example.networking;

import retrofit.RequestInterceptor;

public class NetworkInterceptor implements RequestInterceptor {

    @Override
    public void intercept(final RequestFacade request) {
        request.addHeader("Authorization", "Token MDphYzdhZTIyOC1lMTJlLTExZTQtOGM4ZC01ZjczYmZkZGY2ZmM6Z3pBVGp4MWdDVndoQTJ4R282dlE2RE02TkVmZ2RrTWJVNGFP");
    }
}
