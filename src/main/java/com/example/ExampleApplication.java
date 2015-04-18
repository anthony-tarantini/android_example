package com.example;

import android.app.Application;

import com.example.dagger.ApplicationModule;
import com.example.dagger.Injector;

import dagger.ObjectGraph;

public class ExampleApplication extends Application implements Injector {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "com.example.providers.ExampleDatabase";

    private ObjectGraph mGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        createGraph();
    }

    protected void createGraph() {
        mGraph = ObjectGraph.create(getModules());
    }

    public Object[] getModules() {
        final Object[] modules = new Object[1];
        modules[0] = new ApplicationModule(this);
        return modules;
    }

    @Override
    public void inject(Object object) {
        mGraph.inject(object);
    }
}
