package com.example;

import com.example.dagger.TestApplicationModule;

public class TestExampleApplication extends ExampleApplication {
    public Object[] getModules() {
        final Object[] modules = new Object[1];
        modules[0] = new TestApplicationModule();
        return modules;
    }
}
