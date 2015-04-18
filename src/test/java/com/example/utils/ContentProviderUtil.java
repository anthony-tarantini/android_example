package com.example.utils;

import com.example.providers.ExampleContentProvider;

import org.robolectric.shadows.ShadowContentResolver;

public class ContentProviderUtil {
    public static void create() {
        final ExampleContentProvider provider = new ExampleContentProvider();
        provider.onCreate();
        ShadowContentResolver.registerProvider(ExampleContentProvider.AUTHORITY, provider);
    }
}
